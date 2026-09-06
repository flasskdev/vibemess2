<?php
/** Token-based authentication. Requires migrations/20260907_security_settings.sql. */
final class VibeSecurity {
    private $db;
    public function __construct(PDO $db) { $this->db = $db; }
    private function row($sql, array $args) {
        $q = $this->db->prepare($sql); $q->execute($args); return $q->fetch(PDO::FETCH_ASSOC);
    }
    private function run($sql, array $args = []) {
        $q = $this->db->prepare($sql); $q->execute($args); return $q;
    }
    public function validate($token, $userId = null, $deviceId = null) {
        if (!is_string($token) || !preg_match('/^[a-f0-9]{64}$/D', $token)) return false;
        $row = $this->row('SELECT a.*, u.is_banned, u.is_freezed FROM auth_sessions a JOIN users u ON u.id=a.user_id WHERE token_hash=? AND expires_at>NOW()', [hash('sha256', $token)]);
        if (!$row || $row['is_banned'] || $row['is_freezed']) return false;
        if ($userId !== null && (int)$row['user_id'] !== (int)$userId) return false;
        if ($deviceId !== null && !hash_equals($row['device_id'], (string)$deviceId)) return false;
        return $row;
    }
    public function issue($userId, $deviceId) {
        if (!is_string($deviceId) || strlen($deviceId)<8 || strlen($deviceId)>255) throw new InvalidArgumentException('Invalid device');
        $user = $this->row('SELECT id FROM users WHERE id=? AND is_banned=0 AND is_freezed=0', [$userId]);
        if (!$user) throw new RuntimeException('Account unavailable');
        $token = bin2hex(random_bytes(32));
        $this->run('DELETE FROM auth_sessions WHERE user_id=? AND device_id=?', [$userId, $deviceId]);
        $this->run('INSERT INTO auth_sessions (token_hash,user_id,device_id,expires_at) VALUES (?,?,?,DATE_ADD(NOW(), INTERVAL 30 DAY))', [hash('sha256',$token),$userId,$deviceId]);
        return $token;
    }
    public function afterEmail($userId, $deviceId, $isNew) {
        if (!is_string($deviceId) || strlen($deviceId)<8 || strlen($deviceId)>255) throw new InvalidArgumentException('Update the app');
        $s = $this->row('SELECT password_hash,hint FROM user_security WHERE user_id=?', [$userId]);
        if ($s && $s['password_hash']) {
            $token = bin2hex(random_bytes(32));
            $this->run('DELETE FROM auth_challenges WHERE expires_at<NOW() OR (user_id=? AND device_id=?)', [$userId,$deviceId]);
            $this->run('INSERT INTO auth_challenges (token_hash,user_id,device_id,expires_at) VALUES (?,?,?,DATE_ADD(NOW(), INTERVAL 5 MINUTE))', [hash('sha256',$token),$userId,$deviceId]);
            return ['type'=>'verify_code_result','success'=>false,'requires_two_factor'=>true,'challenge_token'=>$token,'hint'=>$s['hint']];
        }
        return ['type'=>'verify_code_result','success'=>true,'user_id'=>$userId,'is_new_user'=>$isNew,'session_token'=>$this->issue($userId,$deviceId)];
    }
    private function verifyPassword($userId, $password, $s) {
        if ($s['locked_until'] && strtotime($s['locked_until'])>time()) return false;
        if (!is_string($password) || strlen($password)>256 || !password_verify($password, $s['password_hash'])) {
            $this->run('UPDATE user_security SET failed_attempts=IF(locked_until IS NOT NULL AND locked_until<=NOW(),1,failed_attempts+1), locked_until=IF(failed_attempts>=5,DATE_ADD(NOW(),INTERVAL 5 MINUTE),NULL) WHERE user_id=?', [$userId]);
            return false;
        }
        $this->run('UPDATE user_security SET failed_attempts=0,locked_until=NULL WHERE user_id=?', [$userId]);
        return true;
    }
    public function challenge(array $data) {
        $this->db->beginTransaction();
        try {
            $c = $this->row('SELECT * FROM auth_challenges WHERE token_hash=? AND expires_at>NOW() FOR UPDATE', [hash('sha256', (string)($data['challenge_token']??''))]);
            if (!$c) { $this->db->commit(); return ['type'=>'verify_code_result','success'=>false,'challenge_expired'=>true,'message'=>'Срок проверки истёк. Войдите заново.']; }
            $s = $this->row('SELECT * FROM user_security WHERE user_id=? FOR UPDATE', [$c['user_id']]);
            if (!$s || !$s['password_hash'] || !$this->verifyPassword($c['user_id'],$data['password']??'', $s)) {
                $this->db->commit(); return ['type'=>'verify_code_result','success'=>false,'message'=>'Неверный пароль или временная блокировка. После 5 ошибок подождите 5 минут.'];
            }
            $this->run('DELETE FROM auth_challenges WHERE token_hash=?', [$c['token_hash']]);
            $token=$this->issue((int)$c['user_id'],$c['device_id']);
            $this->db->commit();
            return ['type'=>'verify_code_result','success'=>true,'user_id'=>(int)$c['user_id'],'is_new_user'=>false,'session_token'=>$token];
        } catch (Throwable $e) { if ($this->db->inTransaction()) $this->db->rollBack(); throw $e; }
    }
    public function settings($userId, array $data, $currentToken) {
        $this->db->beginTransaction();
        try {
            $this->run('INSERT IGNORE INTO user_security (user_id) VALUES (?)',[$userId]);
            $s=$this->row('SELECT * FROM user_security WHERE user_id=? FOR UPDATE',[$userId]);
            if ($data['type']==='set_two_factor') {
                if ($s['password_hash'] && !$this->verifyPassword($userId,$data['current_password']??'', $s)) {
                    $this->db->commit(); return ['type'=>'two_factor_result','success'=>false,'message'=>'Неверный пароль или временная блокировка.'];
                }
                $op=$data['operation']??'set'; $hash=$s['password_hash'];
                if ($op==='disable') { $hash=null; $hint=null; }
                elseif ($op==='set') {
                    $p=$data['password']??'';
                    if (!is_string($p) || strlen($p)<6 || strlen($p)>72) {
                        $this->db->rollBack(); return ['type'=>'two_factor_result','success'=>false,'message'=>'Пароль должен занимать от 6 до 72 байт UTF-8.'];
                    }
                    $hash=password_hash($p,PASSWORD_DEFAULT); $hint=mb_substr((string)($data['hint']??''),0,128);
                    if ($hint!=='' && mb_stripos($hint,$p)!==false) { $this->db->rollBack(); return ['type'=>'two_factor_result','success'=>false,'message'=>'Не включайте пароль в подсказку.']; }
                } elseif ($op==='hint' && $hash) { $hint=mb_substr((string)($data['hint']??''),0,128); }
                else { $this->db->rollBack(); return ['type'=>'two_factor_result','success'=>false,'message'=>'Недопустимая операция.']; }
                $this->run('UPDATE user_security SET password_hash=?,hint=?,failed_attempts=0,locked_until=NULL WHERE user_id=?',[$hash,$hint,$userId]);
                // Changing protection revokes all OTHER sessions and pending challenges.
                if ($op!=='hint') {
                    $this->run('DELETE FROM auth_sessions WHERE user_id=? AND token_hash<>?',[$userId,hash('sha256',$currentToken)]);
                    $this->run('DELETE FROM sessions WHERE user_id=? AND device_id NOT IN (SELECT device_id FROM auth_sessions WHERE user_id=?)',[$userId,$userId]);
                    $this->run('DELETE FROM auth_challenges WHERE user_id=?',[$userId]);
                }
                $s=['password_hash'=>$hash,'hint'=>$hint];
            }
            $this->db->commit();
            return ['type'=>'two_factor_result','success'=>true,'enabled'=>!empty($s['password_hash']),'hint'=>$s['hint']];
        } catch (Throwable $e) { if ($this->db->inTransaction()) $this->db->rollBack(); throw $e; }
    }
    public function notifications($userId, array $data) {
        if ($data['type']==='set_notification_settings') {
            $this->run('INSERT INTO notification_settings (user_id,mute_all,auto_mute_new) VALUES (?,?,?) ON DUPLICATE KEY UPDATE mute_all=VALUES(mute_all),auto_mute_new=VALUES(auto_mute_new)',[$userId,!empty($data['mute_all'])?1:0,!empty($data['auto_mute_new'])?1:0]);
        }
        $s=$this->row('SELECT mute_all,auto_mute_new FROM notification_settings WHERE user_id=?',[$userId]);
        return ['type'=>'notification_settings_result','success'=>true,'mute_all'=>!empty($s['mute_all']),'auto_mute_new'=>!empty($s['auto_mute_new'])];
    }
    /** Must run before delivering a message, also for bot messages. */
    public function registerContact($userId,$peerId) {
        $q=$this->run('INSERT IGNORE INTO notification_contacts (user_id,peer_id) SELECT id,? FROM users WHERE id=?',[$peerId,$userId]);
        if ($q->rowCount()>0) {
            $s=$this->row('SELECT auto_mute_new FROM notification_settings WHERE user_id=?',[$userId]);
            if (!empty($s['auto_mute_new'])) $this->run('INSERT IGNORE INTO muted_users (user_id,muted_id) VALUES (?,?)',[$userId,$peerId]);
        }
    }
}
