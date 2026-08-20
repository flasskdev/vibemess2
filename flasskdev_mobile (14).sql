-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: mysql-flasskdev.alwaysdata.net
-- Generation Time: Aug 18, 2026 at 02:28 PM
-- Server version: 10.11.18-MariaDB
-- PHP Version: 8.4.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flasskdev_mobile`
--

-- --------------------------------------------------------

--
-- Table structure for table `banned_devices`
--

CREATE TABLE `banned_devices` (
  `device_id` varchar(255) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `banned_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blocked_users`
--

CREATE TABLE `blocked_users` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `blocked_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `blocked_users`
--

INSERT INTO `blocked_users` (`id`, `user_id`, `blocked_id`, `created_at`) VALUES
(7, 105, 106, '2026-08-18 08:43:11');

-- --------------------------------------------------------

--
-- Table structure for table `bots`
--

CREATE TABLE `bots` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL,
  `bot_token` varchar(128) DEFAULT NULL,
  `is_verified` tinyint(1) NOT NULL DEFAULT 0,
  `about` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bots`
--

INSERT INTO `bots` (`id`, `name`, `username`, `bot_token`, `is_verified`, `about`) VALUES
(1, 'VibeCat', 'vibe', 'VIBEBOT-7q9XkL2mP8wR4zY5tB1vN3hF6dC0sJ9aH2fG5bK8xQ1mL4pZ7tW0yC3vN6hF9dC2sJ5aH8fG1bK4xQ7mL0pZ3tW6yC9vN2hF5dC8sJ1aH4fG7bK0xQ3mL6pZ', 1, 'Люблю помогать <3'),
(2, 'Spam Info', 'SpamInfo', NULL, 1, 'Информация о статусе SpamBlock.'),
(3, 'Vibe Notify', 'VibeNotify', NULL, 1, 'Уведомления от Vibe.');

-- --------------------------------------------------------

--
-- Table structure for table `bot_callbacks`
--

CREATE TABLE `bot_callbacks` (
  `id` int(11) NOT NULL,
  `bot_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `data` text NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bot_callbacks`
--

INSERT INTO `bot_callbacks` (`id`, `bot_id`, `user_id`, `message_id`, `data`, `created_at`) VALUES
(1, 1, 106, 12416, 'btn_ping', '2026-08-18 11:23:12'),
(2, 1, 106, 12416, 'btn_ping', '2026-08-18 11:23:17'),
(3, 1, 106, 12416, 'btn_ping', '2026-08-18 11:23:18'),
(4, 1, 106, 12416, 'btn_ping', '2026-08-18 11:23:18'),
(5, 1, 106, 12416, 'btn_ping', '2026-08-18 11:23:19'),
(6, 1, 106, 12416, 'btn_ping', '2026-08-18 11:23:20'),
(7, 1, 106, 12416, 'btn_info', '2026-08-18 11:23:20'),
(8, 1, 106, 12416, 'btn_ping', '2026-08-18 11:36:06'),
(9, 1, 106, 12416, 'btn_info', '2026-08-18 11:36:07'),
(10, 1, 106, 12416, 'btn_ping', '2026-08-18 11:36:09'),
(11, 1, 106, 12416, 'btn_ping', '2026-08-18 11:36:10'),
(12, 1, 106, 12416, 'btn_info', '2026-08-18 11:50:19'),
(13, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:20'),
(14, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:24'),
(15, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:24'),
(16, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:25'),
(17, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:26'),
(18, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:26'),
(19, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:27'),
(20, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:27'),
(21, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:28'),
(22, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:29'),
(23, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:29'),
(24, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:29'),
(25, 1, 106, 12416, 'btn_ping', '2026-08-18 11:50:30'),
(26, 1, 106, 12416, 'btn_info', '2026-08-18 11:50:43'),
(27, 1, 106, 12416, 'btn_info', '2026-08-18 11:50:43'),
(28, 1, 106, 12416, 'btn_info', '2026-08-18 11:50:44'),
(29, 1, 106, 12416, 'btn_ping', '2026-08-18 11:51:51'),
(30, 1, 106, 12416, 'btn_ping', '2026-08-18 11:51:52'),
(31, 1, 106, 12416, 'btn_info', '2026-08-18 11:56:29'),
(32, 1, 106, 12416, 'btn_info', '2026-08-18 11:56:29'),
(33, 1, 106, 12416, 'btn_ping', '2026-08-18 11:56:31'),
(34, 1, 106, 12416, 'btn_help', '2026-08-18 11:56:32'),
(35, 1, 106, 12416, 'btn_help', '2026-08-18 11:56:32'),
(36, 1, 106, 12416, 'btn_help', '2026-08-18 11:56:33'),
(37, 1, 106, 12416, 'btn_ping', '2026-08-18 11:59:48'),
(38, 1, 106, 12416, 'btn_info', '2026-08-18 12:00:44'),
(39, 1, 106, 12416, 'btn_info', '2026-08-18 12:00:45'),
(40, 1, 106, 12416, 'btn_ping', '2026-08-18 12:01:04'),
(41, 1, 106, 12416, 'btn_ping', '2026-08-18 12:01:04'),
(42, 1, 106, 12416, 'btn_ping', '2026-08-18 12:01:05'),
(43, 1, 106, 12416, 'btn_ping', '2026-08-18 12:01:05'),
(44, 1, 106, 12416, 'btn_ping', '2026-08-18 12:01:06'),
(45, 1, 106, 12416, 'btn_ping', '2026-08-18 12:01:06'),
(46, 1, 106, 12416, 'btn_help', '2026-08-18 12:01:08'),
(47, 1, 106, 12416, 'btn_ping', '2026-08-18 12:16:19');

-- --------------------------------------------------------

--
-- Table structure for table `bot_callback_answers`
--

CREATE TABLE `bot_callback_answers` (
  `id` int(11) NOT NULL,
  `callback_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `text` text DEFAULT NULL,
  `show_alert` tinyint(1) DEFAULT 0,
  `is_delivered` tinyint(1) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `bot_updates`
--

CREATE TABLE `bot_updates` (
  `id` int(11) NOT NULL,
  `bot_id` int(11) NOT NULL,
  `update_type` varchar(32) NOT NULL,
  `reference_id` int(11) NOT NULL,
  `payload` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`payload`)),
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `message_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chat_actions`
--

CREATE TABLE `chat_actions` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `sender_type` varchar(20) NOT NULL,
  `action` varchar(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `chat_actions`
--

INSERT INTO `chat_actions` (`id`, `sender_id`, `receiver_id`, `sender_type`, `action`, `created_at`) VALUES
(1, 1, 106, 'bot', 'typing', '2026-08-17 11:51:53'),
(2, 1, 106, 'bot', 'typing', '2026-08-17 11:52:21'),
(3, 1, 106, 'bot', 'typing', '2026-08-17 11:56:23'),
(4, 1, 106, 'bot', 'typing', '2026-08-17 11:56:36'),
(5, 1, 106, 'bot', 'typing', '2026-08-17 11:56:48'),
(6, 1, 106, 'bot', 'typing', '2026-08-17 11:56:57'),
(7, 1, 106, 'bot', 'typing', '2026-08-17 11:57:56'),
(8, 1, 106, 'bot', 'typing', '2026-08-17 11:58:11'),
(9, 1, 106, 'bot', 'typing', '2026-08-17 12:01:56'),
(10, 1, 106, 'bot', 'typing', '2026-08-17 12:02:15'),
(11, 1, 106, 'bot', 'typing', '2026-08-18 00:33:03'),
(12, 1, 106, 'bot', 'typing', '2026-08-18 00:33:20'),
(13, 1, 106, 'bot', 'typing', '2026-08-18 00:33:44'),
(14, 1, 106, 'bot', 'typing', '2026-08-18 10:59:30'),
(15, 1, 106, 'bot', 'typing', '2026-08-18 11:00:32'),
(16, 1, 106, 'bot', 'typing', '2026-08-18 11:01:05'),
(17, 1, 106, 'bot', 'typing', '2026-08-18 11:02:54'),
(18, 1, 106, 'bot', 'typing', '2026-08-18 11:11:34'),
(19, 1, 106, 'bot', 'typing', '2026-08-18 11:22:06'),
(20, 1, 103, 'bot', 'typing', '2026-08-18 12:17:42'),
(21, 1, 106, 'bot', 'typing', '2026-08-18 12:17:44'),
(22, 1, 106, 'bot', 'typing', '2026-08-18 12:17:46'),
(23, 1, 106, 'bot', 'typing', '2026-08-18 12:17:47'),
(24, 1, 106, 'bot', 'typing', '2026-08-18 12:17:49'),
(25, 1, 106, 'bot', 'typing', '2026-08-18 12:17:50'),
(26, 1, 103, 'bot', 'typing', '2026-08-18 12:17:56'),
(27, 1, 106, 'bot', 'typing', '2026-08-18 12:17:58'),
(28, 1, 106, 'bot', 'typing', '2026-08-18 12:18:00'),
(29, 1, 106, 'bot', 'typing', '2026-08-18 12:18:02'),
(30, 1, 106, 'bot', 'typing', '2026-08-18 12:18:04'),
(31, 1, 106, 'bot', 'typing', '2026-08-18 12:18:05'),
(32, 1, 106, 'bot', 'typing', '2026-08-18 12:18:08'),
(33, 1, 106, 'bot', 'typing', '2026-08-18 12:18:10'),
(34, 1, 106, 'bot', 'typing', '2026-08-18 12:18:12'),
(35, 1, 106, 'bot', 'typing', '2026-08-18 12:18:14'),
(36, 1, 106, 'bot', 'typing', '2026-08-18 12:18:16'),
(37, 1, 103, 'bot', 'typing', '2026-08-18 12:18:21'),
(38, 1, 103, 'bot', 'typing', '2026-08-18 12:19:37'),
(39, 1, 106, 'bot', 'typing', '2026-08-18 12:19:39'),
(40, 1, 106, 'bot', 'typing', '2026-08-18 12:19:40'),
(41, 1, 106, 'bot', 'typing', '2026-08-18 12:19:42'),
(42, 1, 106, 'bot', 'typing', '2026-08-18 12:19:44'),
(43, 1, 106, 'bot', 'typing', '2026-08-18 12:19:46'),
(44, 1, 106, 'bot', 'typing', '2026-08-18 12:19:47'),
(45, 1, 106, 'bot', 'typing', '2026-08-18 12:19:50'),
(46, 1, 106, 'bot', 'typing', '2026-08-18 12:19:51');

-- --------------------------------------------------------

--
-- Table structure for table `device_tokens`
--

CREATE TABLE `device_tokens` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `device_tokens`
--

INSERT INTO `device_tokens` (`id`, `user_id`, `token`, `created_at`) VALUES
(854, 106, 'cIwhmvmHSzaDwYVklFY_43:APA91bGkyy3yylKUp2mqJzD0-7VqJRhYi8gTu_6ByZG02OXtI1koB8IuB0VIjmDvkMixoYtGJHzm3qcz-3ptdPGRvjl0Uzw1J9lGe4OKwT1htVtSr2QoAao', '2026-08-17 10:08:00'),
(972, 105, 'cEBs9RRIRr2lC1Fr15OccA:APA91bHrCnk96N7fRgy-d_lytWV7SZpIhT9EWsK-equ72fMpi00fRV4ZfM2y0Zchjhg2Kh4UzuP4JT-WWT4SRtuiEuUElmsPALc37QSRsX9MAeKrC4Og2wA', '2026-08-18 08:52:30'),
(1022, 106, 'eNKWVUTjSeqNxg_GNcy36b:APA91bERA0SleOa7NsQKEqxBzzauarkDY8sqerecTSYvpPuvG_DqlKHhH05qDa8MwA4j6UgG8M3EsImPvt-uGiM7-Ij71kt32gtzdHeVAgJFQUJX35FYPUo', '2026-08-18 12:27:11');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `sender_type` enum('user','bot') NOT NULL DEFAULT 'user',
  `content` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_read` tinyint(1) NOT NULL DEFAULT 0,
  `reply_to_id` int(11) DEFAULT NULL,
  `is_edited` tinyint(1) NOT NULL DEFAULT 0,
  `deleted_by_sender` tinyint(1) DEFAULT 0,
  `deleted_by_receiver` tinyint(1) DEFAULT 0,
  `forwarded_from_id` int(11) DEFAULT NULL,
  `attachments` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`attachments`)),
  `reactions` text DEFAULT NULL,
  `reply_markup` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`reply_markup`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `sender_id`, `receiver_id`, `sender_type`, `content`, `timestamp`, `is_read`, `reply_to_id`, `is_edited`, `deleted_by_sender`, `deleted_by_receiver`, `forwarded_from_id`, `attachments`, `reactions`, `reply_markup`) VALUES
(1, 93, 94, 'user', 'приветик', '2026-07-27 15:17:58', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(2, 93, 96, 'user', 'приветик', '2026-07-28 15:17:58', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(3, 96, 93, 'user', 'привет', '2026-07-28 15:52:13', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(4, 96, 93, 'user', 'ку', '2026-07-28 15:52:16', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(5, 96, 93, 'user', 'дарова ебать алвдвк', '2026-07-28 15:58:02', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(7, 97, 98, 'user', 'дарова ебать алвдвк', '2025-07-09 15:58:02', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(8, 97, 98, 'user', 'ку', '2026-07-26 16:25:25', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(9, 97, 98, 'user', 'дарова', '2026-07-28 16:25:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(10, 97, 98, 'user', 'ем', '2026-07-28 16:25:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(11, 98, 97, 'user', 'xj nffrjt', '2026-07-28 16:26:27', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12, 97, 98, 'user', 'ем чооооо', '2026-07-28 16:26:34', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(13, 97, 98, 'user', 'еммм чо блять', '2026-07-28 16:26:44', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(14, 97, 98, 'user', 'пиздец кнш', '2026-07-28 16:26:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(15, 97, 98, 'user', 'вьвдвлв', '2026-07-28 16:26:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(16, 97, 98, 'user', 'вдвдбв', '2026-07-28 16:26:52', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(17, 97, 98, 'user', 'пое', '2026-07-28 16:32:05', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(18, 97, 98, 'user', 'пр', '2026-07-28 16:49:50', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(19, 97, 98, 'user', 'пр', '2026-07-28 16:49:52', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(20, 97, 98, 'user', 'воул', '2026-07-28 16:49:54', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(21, 97, 98, 'user', 'ку', '2026-07-28 16:50:01', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(22, 97, 98, 'user', 'ку', '2026-07-28 16:50:07', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(23, 97, 98, 'user', 'ксс', '2026-07-28 16:50:09', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(24, 97, 98, 'user', 'авп', '2026-07-28 16:50:11', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(25, 97, 98, 'user', 'чча', '2026-07-28 16:50:19', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(26, 97, 98, 'user', 'пссп', '2026-07-28 16:50:21', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(27, 98, 97, 'user', 'fds', '2026-07-28 17:08:19', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(28, 98, 97, 'user', 'ssdf', '2026-07-28 17:08:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(29, 98, 97, 'user', 'kjkjkk', '2026-07-28 17:08:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(30, 97, 98, 'user', 'пр', '2026-07-28 17:09:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(31, 97, 98, 'user', 'далбаеб', '2026-07-28 17:09:26', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(32, 98, 97, 'user', 'jjj', '2026-07-28 17:09:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(33, 97, 98, 'user', 'аллвла', '2026-07-28 17:09:46', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(34, 97, 98, 'user', 'прпрар', '2026-07-28 17:26:26', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(35, 97, 98, 'user', 'ададад', '2026-07-28 17:26:30', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(36, 97, 98, 'user', 'чьвлала', '2026-07-28 17:26:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(37, 97, 98, 'user', 'ьалсла', '2026-07-28 17:26:34', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(38, 97, 98, 'user', 'аьлаа', '2026-07-28 17:26:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(39, 97, 98, 'user', 'ьала', '2026-07-28 17:26:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(40, 97, 98, 'user', 'аьа', '2026-07-28 17:26:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(41, 97, 98, 'user', 'аьа', '2026-07-28 17:26:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(42, 97, 98, 'user', 'а', '2026-07-28 17:26:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(43, 97, 98, 'user', 'а', '2026-07-28 17:26:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(44, 97, 98, 'user', 'а', '2026-07-28 17:26:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(45, 97, 98, 'user', 'а', '2026-07-28 17:26:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(46, 97, 98, 'user', 'а', '2026-07-28 17:26:38', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(47, 98, 97, 'user', 'jjjhhjhj', '2026-07-28 17:55:16', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(52, 1, 103, 'bot', 'Добро пожаловать в семейство Vibe!\nЗадавай мне любые вопросы, я подскажу что да как!\nМяу.', '2026-07-28 21:06:16', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(53, 103, 1, 'user', 'привет', '2026-07-28 21:33:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(54, 1, 104, 'bot', 'Добро пожаловать в семейство Vibe!\nЗадавай мне любые вопросы, я подскажу что да как!\nМяу.', '2026-07-28 22:09:13', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(55, 1, 105, 'bot', 'Добро пожаловать в семейство Vibe!\nЗадавай мне любые вопросы, я подскажу что да как!\nМяу.', '2026-07-28 22:19:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(57, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(58, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(59, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(60, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(61, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(62, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(63, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(64, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(65, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(66, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(67, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(68, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(70, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(71, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(72, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(73, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(74, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(75, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(76, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(77, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(78, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(79, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(80, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(81, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(82, 1, 106, 'bot', 'Добро пожаловать в семейство Vibe!\r\nЗадавай мне любые вопросы, я подскажу что да как!\r\nМяу.', '2026-07-28 22:28:21', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(83, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(84, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(85, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(86, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(87, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(88, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(89, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(90, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(91, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(92, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(93, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(94, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(96, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(97, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(98, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(99, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(100, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(101, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(102, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(103, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(104, 106, 1, 'user', 'чо', '2026-07-29 00:57:57', 0, 58, 0, 0, 0, NULL, NULL, NULL, NULL),
(105, 106, 1, 'user', 'привет', '2026-07-29 00:27:21', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(106, 106, 1, 'user', 'пон', '2026-07-29 00:29:10', 0, 59, 0, 0, 0, NULL, NULL, NULL, NULL),
(107, 1, 107, 'bot', 'Добро пожаловать в семейство Vibe!\nЗадавай мне любые вопросы, я подскажу что да как!\nМяу.', '2026-07-29 01:47:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(108, 107, 106, 'user', 'пссп', '2026-07-28 16:50:21', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(109, 106, 107, 'user', 'еммм чо', '2026-07-29 01:50:11', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(110, 107, 106, 'user', 'hghghg', '2026-07-29 01:50:28', 1, 109, 0, 0, 0, NULL, NULL, NULL, NULL),
(111, 107, 106, 'user', 'hhh', '2026-07-29 01:51:09', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(112, 107, 106, 'user', 'jjhj', '2026-07-29 01:51:21', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(113, 107, 106, 'user', 'kjjk', '2026-07-29 01:51:34', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(114, 107, 106, 'user', 'jjjj', '2026-07-29 01:51:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(115, 107, 106, 'user', 'rmm', '2026-07-29 01:51:54', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(116, 107, 106, 'user', 'ool', '2026-07-29 01:52:01', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(117, 106, 107, 'user', 'чо те надо', '2026-07-29 01:52:14', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(118, 106, 107, 'user', 'еммм', '2026-07-29 02:09:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(119, 106, 107, 'user', 'члвдвлала', '2026-07-29 02:11:48', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(120, 106, 107, 'user', 'алалалалалалалалаак', '2026-07-29 02:12:07', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(121, 106, 107, 'user', 'пизда назуй', '2026-07-29 02:16:23', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(122, 106, 107, 'user', 'пон', '2026-07-29 02:16:44', 1, 116, 0, 0, 0, NULL, NULL, NULL, NULL),
(123, 107, 106, 'user', 'tvvlll', '2026-07-29 13:35:29', 1, NULL, 1, 0, 0, NULL, NULL, NULL, NULL),
(124, 107, 106, 'user', 'jkjkjjk', '2026-07-29 13:38:31', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(125, 107, 106, 'user', 'lklklkkl', '2026-07-29 13:39:01', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(126, 107, 106, 'user', '65656', '2026-07-29 13:39:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(127, 107, 106, 'user', '78878', '2026-07-29 13:39:56', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(128, 107, 106, 'user', '90909', '2026-07-29 13:39:58', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(129, 107, 106, 'user', '878788', '2026-07-29 14:11:25', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(130, 107, 106, 'user', '-888', '2026-07-29 14:11:30', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(131, 107, 106, 'user', '363645', '2026-07-29 14:11:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(133, 106, 107, 'user', 'привет', '2026-07-29 14:11:56', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(134, 107, 106, 'user', '887889', '2026-07-29 14:12:05', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(135, 107, 106, 'user', '09090', '2026-07-29 14:12:09', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(136, 106, 107, 'user', 'ем чо', '2026-07-29 14:12:11', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(137, 106, 107, 'user', 'емч чочоо', '2026-07-29 14:12:14', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(138, 106, 107, 'user', 'алвдв', '2026-07-29 14:12:19', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(139, 107, 106, 'user', '7887', '2026-07-29 14:12:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(140, 107, 106, 'user', '099090', '2026-07-29 14:12:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(141, 107, 106, 'user', '787878', '2026-07-29 14:41:55', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(142, 107, 106, 'user', '877878', '2026-07-29 14:42:05', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(143, 107, 106, 'user', '777', '2026-07-29 14:42:48', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(144, 107, 106, 'user', 'ghfhgffgfgfg', '2026-07-29 14:43:00', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(145, 107, 106, 'user', '90989', '2026-07-29 14:44:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(146, 107, 106, 'user', '7876768', '2026-07-29 14:44:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(147, 107, 106, 'user', '889899', '2026-07-29 14:46:08', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(148, 106, 107, 'user', 'пр', '2026-07-29 15:59:48', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(149, 106, 107, 'user', 'пон', '2026-07-29 15:59:54', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(150, 107, 106, 'user', '8998889', '2026-07-29 16:00:14', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(151, 107, 106, 'user', '99898', '2026-07-29 16:03:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(152, 107, 106, 'user', '8999877887999', '2026-07-29 16:07:21', 1, NULL, 1, 0, 0, NULL, NULL, NULL, NULL),
(153, 107, 106, 'user', 'Todo 29.07\n1. сделать закреп сообщений для себя или для обоих сразу\n2. сделать редактирование сообщений\n3. сделать удаление сообщений\n4. сдедать пересылку сообщений\n5. сделать черновик\n6. сделать жалобы на сообщения (а также возможность добавить несколько сообщений к жалобе сразу (до 10 шт.))\n7. в админ панели сделать рассмотрение жалоб по категориям а также справа от категорий писать количество новых жалоб\nа также сделать архив жалоб\n8. сделать теневой бан на жалобы чтобы не спамили\n9. сделать возможность наказывать спамблоком (на время, не сможет в течении этого времени писать кому-то первый) и банить аккаунт (навсегда)\n10. сделать отправку фото\n11. сделать отправку кода для входа в аккаунт и на сам аккаунт в ботаTodo 29.07\n1. сделать закреп сообщений для себя или для обоих сразу\n2. сделать редактирование сообщений\n3. сделать удаление сообщений\n4. сдедать пересылку сообщений\n5. сделать черновик\n6. сделать жалобы на сообщения (а также возможность добавить несколько сообщений к жалобе сразу (до 10 шт.))\n7. в админ панели сделать рассмотрение жалоб по категориям а также справа от категорий писать количество новых жалоб\nа также сделать архив жалоб\n8. сделать теневой бан на жалобы чтобы не спамили\n9. сделать возможность наказывать спамблоком (на время, не сможет в течении этого времени писать кому-то первый) и банить аккаунт (навсегда)\n10. сделать отправку фото\n11. сделать отправку кода для входа в аккаунт и на сам аккаунт в бота', '2026-07-29 16:08:27', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(154, 107, 106, 'user', '88', '2026-07-29 16:08:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(155, 107, 106, 'user', '9889', '2026-07-29 16:08:43', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(156, 107, 106, 'user', '777000', '2026-07-29 16:09:19', 1, NULL, 1, 0, 0, NULL, NULL, NULL, NULL),
(157, 107, 106, 'user', '988998778', '2026-07-29 16:09:32', 1, NULL, 1, 0, 1, NULL, NULL, NULL, NULL),
(158, 106, 107, 'user', 'вовлвлак', '2026-07-29 17:05:00', 1, NULL, 0, 1, 0, NULL, NULL, NULL, NULL),
(159, 107, 106, 'user', '87878', '2026-07-29 17:07:43', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(160, 107, 106, 'user', '909009009', '2026-07-29 17:07:48', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(161, 107, 106, 'user', '888', '2026-07-29 17:08:16', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(162, 107, 106, 'user', '78778', '2026-07-29 17:19:05', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(163, 107, 106, 'user', '788787', '2026-07-29 17:20:45', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(164, 107, 106, 'user', '888', '2026-07-29 17:49:03', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(165, 107, 106, 'user', '888', '2026-07-29 17:51:38', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(166, 107, 106, 'user', '000', '2026-07-29 17:51:45', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(167, 107, 106, 'user', '0000', '2026-07-29 17:51:54', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(168, 106, 107, 'user', 'а', '2026-07-29 19:07:38', 1, NULL, 0, 1, 0, NULL, NULL, NULL, NULL),
(170, 106, 107, 'user', 'вькалвда', '2026-07-29 19:08:07', 1, NULL, 0, 1, 0, NULL, NULL, NULL, NULL),
(173, 106, 107, 'user', '777000', '2026-07-29 19:32:39', 1, NULL, 0, 0, 0, 107, NULL, NULL, NULL),
(174, 106, 107, 'user', '777000', '2026-07-29 19:34:22', 1, NULL, 0, 0, 0, 107, NULL, NULL, NULL),
(175, 107, 106, 'user', '34344', '2026-07-29 20:36:04', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(176, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|34344', '2026-07-29 21:19:27', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(177, 106, 107, 'user', 'а', '2026-07-29 21:20:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(178, 107, 106, 'bot', '$$SYSTEM$$PINNED_MESSAGE|asdasaasdasaasdasaasdasaasdasasd|777000', '2026-07-29 22:33:57', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(179, 107, 106, 'bot', '$$SYSTEM$$PINNED_MESSAGE|asdasaasdasaasdasaasdasaasdasasd|вовлвлак', '2026-07-29 22:35:13', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(180, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|а', '2026-07-29 22:40:08', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(181, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|а', '2026-07-29 22:40:22', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(182, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|777000', '2026-07-29 22:40:29', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(183, 106, 107, 'user', 'ку', '2026-07-29 22:40:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(184, 106, 107, 'user', 'кууу', '2026-07-29 22:40:38', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(185, 107, 106, 'bot', '$$SYSTEM$$PINNED_MESSAGE|asdasaasdasaasdasaasdasaasdasasd|ку', '2026-07-29 22:48:45', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(186, 107, 106, 'bot', '$$SYSTEM$$PINNED_MESSAGE|asdasaasdasaasdasaasdasaasdasasd|кууу', '2026-07-29 22:48:52', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(187, 106, 107, 'user', 'дарова', '2026-07-29 23:11:43', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(189, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|дарова', '2026-07-29 23:12:03', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(190, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|9899898', '2026-07-29 23:12:10', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(191, 106, 107, 'user', 'а', '2026-08-01 17:43:59', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(192, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|а', '2026-08-01 18:04:00', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(193, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|а', '2026-08-01 18:04:06', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(194, 106, 107, 'bot', '$$SYSTEM$$PINNED_MESSAGE|dalbaeb|а', '2026-08-01 18:04:13', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(195, 106, 107, 'user', 'приветик, дружок пиражок', '2026-08-01 21:00:00', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(197, 107, 106, 'user', '9899898', '2026-08-02 17:55:36', 1, NULL, 0, 0, 0, 107, NULL, NULL, NULL),
(198, 107, 106, 'user', 'pizdec..', '2026-08-02 17:56:25', 1, 197, 0, 0, 0, NULL, NULL, NULL, NULL),
(199, 106, 107, 'user', 'прпрарппрппппрп', '2026-08-02 21:50:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(200, 106, 107, 'user', 'кууу', '2026-08-02 21:51:07', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(201, 106, 107, 'user', 'пр', '2026-08-02 21:51:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(202, 106, 107, 'user', 'аавв', '2026-08-02 21:51:25', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(203, 106, 107, 'user', 'аввв', '2026-08-02 21:51:32', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(204, 107, 106, 'user', '99', '2026-08-07 17:37:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(205, 107, 106, 'user', '99', '2026-08-07 17:56:57', 1, NULL, 0, 0, 0, 107, NULL, NULL, NULL),
(206, 107, 106, 'user', '89', '2026-08-07 18:05:59', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(207, 107, 106, 'user', 'аввв', '2026-08-08 06:37:30', 1, NULL, 0, 0, 0, 106, NULL, NULL, NULL),
(208, 107, 106, 'user', '12211', '2026-08-09 02:47:48', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(209, 107, 106, 'user', '2112', '2026-08-09 03:03:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(210, 107, 106, 'user', '4545', '2026-08-09 03:03:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(211, 107, 106, 'user', '434334', '2026-08-09 03:03:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(212, 107, 106, 'user', '33332', '2026-08-09 03:07:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(213, 107, 106, 'user', '12', '2026-08-09 03:07:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(214, 107, 106, 'user', '14', '2026-08-09 03:07:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(215, 106, 107, 'user', '1', '2026-08-09 03:08:22', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(216, 106, 107, 'user', '3', '2026-08-09 03:08:22', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(217, 106, 107, 'user', '4', '2026-08-09 03:08:22', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(218, 106, 107, 'user', '2', '2026-08-09 03:13:12', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(219, 106, 107, 'user', 'друг', '2026-08-09 03:13:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(220, 106, 107, 'user', 'приветик', '2026-08-09 03:13:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(221, 106, 107, 'user', 'друг', '2026-08-09 03:13:50', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(222, 106, 107, 'user', '1', '2026-08-09 03:14:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(223, 106, 107, 'user', '2', '2026-08-09 03:14:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(224, 106, 107, 'user', '3', '2026-08-09 03:14:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(225, 106, 107, 'user', '4', '2026-08-09 03:14:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(226, 106, 107, 'user', '5', '2026-08-09 03:14:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(227, 106, 107, 'user', '5', '2026-08-09 03:14:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(228, 106, 107, 'user', 'норм', '2026-08-09 08:13:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(229, 106, 107, 'user', 'хех', '2026-08-09 08:13:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(230, 106, 107, 'user', 'пон', '2026-08-09 08:13:44', 1, NULL, 0, 0, 0, NULL, '[\"\\/data\\/user\\/0\\/com.flasskdev.vibe\\/cache\\/vibe_images\\/img_78c38898-8fce-4b5b-a59e-c5fb3e6c384c.jpg\"]', NULL, NULL),
(231, 106, 107, 'user', 'пон', '2026-08-09 08:13:44', 1, NULL, 0, 1, 0, NULL, '[\"\\/data\\/user\\/0\\/com.flasskdev.vibe\\/cache\\/vibe_images\\/img_78c38898-8fce-4b5b-a59e-c5fb3e6c384c.jpg\"]', NULL, NULL),
(232, 106, 107, 'user', 'ем', '2026-08-09 08:14:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(233, 106, 107, 'user', 'вв', '2026-08-09 08:14:42', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(234, 106, 107, 'user', 'ер', '2026-08-09 08:21:58', 1, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/152b42727d34c65d7e7f5abcad47d5f73fcee0bbaae931daa1e2b7688d51e91a.webp\"]', NULL, NULL),
(235, 106, 107, 'user', '', '2026-08-09 08:23:08', 1, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/152b42727d34c65d7e7f5abcad47d5f73fcee0bbaae931daa1e2b7688d51e91a.webp\"]', NULL, NULL),
(236, 106, 107, 'user', 'пон', '2026-08-09 08:38:57', 1, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/db592a86a0bab161d8977fc58ed8cfb422d9b560fe12ec1686298d8e759e899b.webp\",\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/152b42727d34c65d7e7f5abcad47d5f73fcee0bbaae931daa1e2b7688d51e91a.webp\",\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/659a8410e2ead2aaa90032395ca0376a73ba00cc13cd05a2713fc0c2ba3a38ca.webp\"]', NULL, NULL),
(12121, 1, 108, 'bot', 'пон', '2026-08-09 08:38:57', 1, NULL, 0, 1, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/db592a86a0bab161d8977fc58ed8cfb422d9b560fe12ec1686298d8e759e899b.webp\",\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/152b42727d34c65d7e7f5abcad47d5f73fcee0bbaae931daa1e2b7688d51e91a.webp\",\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/659a8410e2ead2aaa90032395ca0376a73ba00cc13cd05a2713fc0c2ba3a38ca.webp\"]', NULL, NULL),
(12122, 108, 107, 'user', 'пр сын шлюхи', '2026-08-09 13:40:28', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12123, 107, 104, 'user', 'qq', '2026-08-09 13:41:47', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12124, 107, 104, 'user', 'pr', '2026-08-09 13:42:06', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12125, 107, 104, 'user', 'sas', '2026-08-09 13:42:22', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12126, 107, 104, 'user', 'weww', '2026-08-09 13:42:34', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12127, 107, 104, 'user', 'sasa', '2026-08-09 13:42:40', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12128, 106, 107, 'user', 'пон', '2026-08-11 10:17:18', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12129, 106, 107, 'user', 'ем', '2026-08-11 10:17:25', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12130, 106, 1, 'user', 'duration:1444', '2026-08-11 16:11:16', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/4479ca718c9cfa0707de249305fdc0308e0d6be72ef10bf76a5836f74bf1e36f.webp\"]', NULL, NULL),
(12131, 106, 1, 'user', 'duration:2027', '2026-08-11 16:11:24', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/137e6d238584b1d8cedfffd186b4a32227bff517259583cf73737d5c2853f204.webp\"]', NULL, NULL),
(12132, 106, 1, 'user', 'duration:1147', '2026-08-11 16:11:26', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/6f68fecf9419843dd72f99be22eef285c5b9e1086a7b0c6a600f4796cb59dedf.webp\"]', NULL, NULL),
(12133, 106, 1, 'user', 'duration:2326', '2026-08-11 16:11:31', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/20c629da222722fa67c1bc4977589602df10319091eac20b97f7157843642026.webp\"]', NULL, NULL),
(12134, 106, 1, 'user', '', '2026-08-11 16:24:15', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/0908ea92df1322a54840865a811c2d4eb9ab32412fb37e730c6785a82c3e003c.webp\"]', NULL, NULL),
(12135, 106, 1, 'user', '', '2026-08-11 16:24:15', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/c48d7a077654955899c79df194ef48549d9f9dd0b20bccc87e22eb40296ca69f.webp\"]', NULL, NULL),
(12136, 106, 1, 'user', 'duration:1331', '2026-08-11 16:24:53', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/60485e96667675bb6c3403c5ad1548236ab52a49f47b25438fc68f263054bde2.webp\"]', NULL, NULL),
(12137, 106, 1, 'user', 'duration:1618', '2026-08-11 16:24:57', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/cd6a787453cbeea1058207809374466468f576526a1cfe4dce9d6a19f737ec67.webp\"]', NULL, NULL),
(12138, 106, 1, 'user', 'duration:4029', '2026-08-11 16:25:03', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/7651139b09c39bf8cf68826a1a0731a4d9d76d42eb52dccdac46db4f0b811cb1.webp\"]', NULL, NULL),
(12139, 106, 1, 'user', 'duration:606', '2026-08-11 16:25:05', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/3ca67a33c1b3d29c39aee1d0d89529e98f9a9478a25ab4b5af3699cc95e6dba7.webp\"]', NULL, NULL),
(12140, 106, 1, 'user', 'duration:1006', '2026-08-11 16:27:09', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/72ef22c20cd76eb2164c189f06d1cf2062dc8b3d3766d46cf6f62a60a49369e8.webp\"]', NULL, NULL),
(12141, 105, 1, 'user', 'duration:818', '2026-08-11 20:37:53', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/60e32f8b0110fe378b6895e703e1f528ffde12f5eadf1d8f001a1b6544ceb2b4.webp\"]', NULL, NULL),
(12142, 105, 1, 'user', 'duration:1538', '2026-08-11 20:37:56', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/8ad39920b30f9c6ebe47ff4358685cf3b74ea36e5d649d4b9dde15cdf52df9aa.webp\"]', NULL, NULL),
(12143, 105, 1, 'user', 'duration:1531', '2026-08-11 20:38:00', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/94f754d8dad5ade81a520f59826ad9d734817590d93fe8c6e6204c6dc9c1403a.webp\"]', NULL, NULL),
(12144, 106, 1, 'user', 'duration:1314', '2026-08-11 21:48:01', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/020f8373f6aa96c8b6aebf0d67d9e26e412ab561c16501b7b51a059a13bb5335.webp\"]', NULL, NULL),
(12145, 106, 1, 'user', 'duration:742', '2026-08-11 21:48:03', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/f86b1118c733a1f1afaf5c6299d1edc24dc822be658da18553c8680b5c1aecff.webp\"]', NULL, NULL),
(12146, 106, 1, 'user', 'duration:1125', '2026-08-12 08:01:59', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/6d14074d3c81833b8a0148542c27863ed4356a70b5a216612b4287ab39cab20c.webp\"]', NULL, NULL),
(12147, 106, 1, 'user', 'duration:4843', '2026-08-12 08:17:52', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/8398ad6aa31fc3a211989d1c15a88bb6d775f145606cd1bfda6ebfa310fdb353.webp\"]', NULL, NULL),
(12148, 106, 1, 'user', 'duration:2220', '2026-08-12 08:17:58', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/4aada056f57e00928195531a5d919d3fd6cbf1a6bc0ba3abf501fd82b6ea18d3.webp\"]', NULL, NULL),
(12149, 106, 1, 'user', 'duration:2220', '2026-08-12 08:54:54', 0, NULL, 0, 0, 0, 106, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/4aada056f57e00928195531a5d919d3fd6cbf1a6bc0ba3abf501fd82b6ea18d3.webp\"]', NULL, NULL),
(12150, 106, 1, 'user', 'пон', '2026-08-12 08:55:18', 0, 12148, 0, 0, 0, NULL, NULL, NULL, NULL),
(12151, 106, 1, 'user', '', '2026-08-12 10:25:10', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/2abaa79f8e0aed862e63ca51366f08d4f85b0f534f209c35567950553e87eb80.webp\"]', NULL, NULL),
(12152, 106, 1, 'user', 'duration:7995', '2026-08-12 16:29:57', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/73e3ac9874d0770bfff7bc3f9aa41b3742da7a1a62336a871349cada4b0aaff2.webp\"]', NULL, NULL),
(12153, 106, 1, 'user', '', '2026-08-15 09:26:28', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/d7f9969630fd6145450245bc17062c1efbfed19305b05bd9c15f546b830dd974.ogg\"]', NULL, NULL),
(12154, 106, 1, 'user', '', '2026-08-16 06:47:42', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/baa42098d7c2c543c9030221b9a65543df1a5ff99df0bf7a8339d069ae7ce1fa.rar\"]', NULL, NULL),
(12155, 106, 1, 'user', '', '2026-08-16 06:57:19', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/738b3c83af5bfd18670e1876beb63ce0a804dda2a5cc204bf94c79d6b4fd1bbb.ogg\"]', NULL, NULL),
(12156, 106, 1, 'user', '', '2026-08-16 14:17:04', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/39d6703cfe14727ec3816e2fafb8a54bf17e584fbd6a6a79eef8cf0649d61eca.mp3\"]', NULL, NULL),
(12157, 106, 1, 'user', '.', '2026-08-16 22:02:20', 0, 12156, 0, 0, 0, NULL, NULL, NULL, NULL),
(12158, 106, 1, 'user', '.', '2026-08-16 22:03:00', 0, 12152, 0, 0, 0, NULL, NULL, NULL, NULL),
(12159, 106, 1, 'user', '.', '2026-08-16 23:27:37', 0, 12154, 0, 0, 0, NULL, NULL, NULL, NULL),
(12160, 2, 106, 'bot', 'Hello, world!', '2026-08-17 10:27:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12161, 2, 106, 'bot', 'Hello, world!', '2026-08-17 10:27:32', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12162, 2, 106, 'bot', 'Array', '2026-08-17 10:29:47', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12163, 2, 106, 'bot', '{\'ok\': True, \'result\': {\'id\': 2, \'is_bot\': True, \'first_name\': \'Spam Info\', \'username\': \'SpamInfo\', \'is_verified\': True, \'about\': \'Информация о статусе SpamBlock.\'}}', '2026-08-17 10:30:12', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12164, 106, 2, 'user', 'привет', '2026-08-17 11:10:55', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12165, 106, 2, 'user', 'привет', '2026-08-17 11:10:55', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12166, 106, 2, 'user', 'привет', '2026-08-17 11:10:58', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12167, 106, 2, 'user', 'ку', '2026-08-17 11:12:25', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12168, 106, 2, 'user', 'ку', '2026-08-17 11:12:25', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12169, 106, 2, 'user', 'пон', '2026-08-17 11:12:33', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12170, 106, 2, 'user', '123', '2026-08-17 11:12:41', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12171, 106, 2, 'user', 'ем чо', '2026-08-17 11:12:45', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12172, 2, 106, 'bot', 'Echo: привет', '2026-08-17 11:14:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12173, 2, 106, 'bot', 'Echo: привет', '2026-08-17 11:14:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12174, 2, 106, 'bot', 'Echo: привет', '2026-08-17 11:14:50', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12175, 2, 106, 'bot', 'Echo: ку', '2026-08-17 11:14:50', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12176, 2, 106, 'bot', 'Echo: ку', '2026-08-17 11:14:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12177, 2, 106, 'bot', 'Echo: пон', '2026-08-17 11:14:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12178, 2, 106, 'bot', 'Echo: 123', '2026-08-17 11:14:52', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12179, 2, 106, 'bot', 'Echo: ем чо', '2026-08-17 11:14:52', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12180, 106, 2, 'user', 'ем что бля', '2026-08-17 11:15:01', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12181, 2, 106, 'bot', 'Echo: ем что бля', '2026-08-17 11:15:02', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12182, 106, 2, 'user', 'соси хуй', '2026-08-17 11:15:06', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12183, 2, 106, 'bot', 'Echo: соси хуй', '2026-08-17 11:15:07', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12184, 1, 103, 'bot', 'Echo: привет', '2026-08-17 11:16:07', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12185, 1, 106, 'bot', 'Echo: привет', '2026-08-17 11:16:08', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12186, 1, 106, 'bot', 'Echo: пон', '2026-08-17 11:16:08', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12187, 1, 106, 'bot', 'Echo: чо', '2026-08-17 11:16:09', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12188, 1, 106, 'bot', 'Echo: привет', '2026-08-17 11:16:09', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12189, 1, 106, 'bot', 'Echo: пон', '2026-08-17 11:16:10', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12190, 1, 106, 'bot', 'Echo: чо', '2026-08-17 11:16:10', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12191, 1, 106, 'bot', 'Echo: привет', '2026-08-17 11:16:11', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12192, 1, 106, 'bot', 'Echo: пон', '2026-08-17 11:16:11', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12193, 1, 106, 'bot', 'Echo: чо', '2026-08-17 11:16:12', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12194, 106, 1, 'user', 'пися', '2026-08-17 11:17:50', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12195, 1, 106, 'bot', 'Echo: пися', '2026-08-17 11:17:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12196, 106, 1, 'user', 'писюн', '2026-08-17 11:20:00', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12197, 106, 1, 'user', 'порно', '2026-08-17 11:20:58', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12198, 106, 1, 'user', 'писька', '2026-08-17 11:21:28', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12199, 1, 106, 'bot', 'Echo: писька', '2026-08-17 11:21:28', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12200, 106, 1, 'user', 'порнуха', '2026-08-17 11:22:02', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12201, 1, 106, 'bot', 'Echo: {\'id\': 12200, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'порнуха\', \'timestamp\': \'2026-08-17 13:22:02\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:22:04', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12202, 1, 103, 'bot', 'Echo: {\'id\': 53, \'sender_id\': 103, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-28 23:33:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:24', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12203, 1, 106, 'bot', 'Echo: {\'id\': 57, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12204, 1, 106, 'bot', 'Echo: {\'id\': 58, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:25', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12205, 1, 106, 'bot', 'Echo: {\'id\': 59, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 58, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:25', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12206, 1, 106, 'bot', 'Echo: {\'id\': 60, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:26', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12207, 1, 106, 'bot', 'Echo: {\'id\': 61, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:26', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12208, 1, 106, 'bot', 'Echo: {\'id\': 62, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:27', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12209, 1, 106, 'bot', 'Echo: {\'id\': 63, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:27', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12210, 1, 106, 'bot', 'Echo: {\'id\': 64, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:28', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12211, 1, 106, 'bot', 'Echo: {\'id\': 65, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 58, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:28', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12212, 1, 106, 'bot', 'Echo: {\'id\': 66, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:29', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12213, 1, 106, 'bot', 'Echo: {\'id\': 67, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:29', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12214, 1, 106, 'bot', 'Echo: {\'id\': 68, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:30', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12215, 1, 106, 'bot', 'Echo: {\'id\': 70, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:30', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12216, 1, 106, 'bot', 'Echo: {\'id\': 71, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:31', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12217, 1, 106, 'bot', 'Echo: {\'id\': 72, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 58, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:31', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12218, 1, 106, 'bot', 'Echo: {\'id\': 73, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:32', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12219, 1, 106, 'bot', 'Echo: {\'id\': 74, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:32', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12220, 1, 106, 'bot', 'Echo: {\'id\': 75, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12221, 1, 106, 'bot', 'Echo: {\'id\': 76, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12222, 1, 106, 'bot', 'Echo: {\'id\': 77, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:34', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12223, 106, 1, 'user', 'пон', '2026-08-17 11:25:34', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/63791989bd899f056d02bd8eb99d2368eab3fc5357a494ad9216a19ad593c079.webp\"]', NULL, NULL),
(12224, 1, 106, 'bot', 'Echo: {\'id\': 78, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 58, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:34', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12225, 1, 106, 'bot', 'Echo: {\'id\': 79, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12226, 1, 106, 'bot', 'Echo: {\'id\': 80, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12227, 1, 106, 'bot', 'Echo: {\'id\': 81, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12228, 1, 106, 'bot', 'Echo: {\'id\': 83, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12229, 1, 106, 'bot', 'Echo: {\'id\': 84, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12230, 1, 106, 'bot', 'Echo: {\'id\': 85, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 58, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12231, 1, 106, 'bot', 'Echo: {\'id\': 86, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:38', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12232, 1, 106, 'bot', 'Echo: {\'id\': 87, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:38', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12233, 1, 106, 'bot', 'Echo: {\'id\': 88, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12234, 1, 106, 'bot', 'Echo: {\'id\': 89, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL);
INSERT INTO `messages` (`id`, `sender_id`, `receiver_id`, `sender_type`, `content`, `timestamp`, `is_read`, `reply_to_id`, `is_edited`, `deleted_by_sender`, `deleted_by_receiver`, `forwarded_from_id`, `attachments`, `reactions`, `reply_markup`) VALUES
(12235, 1, 106, 'bot', 'Echo: {\'id\': 90, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12236, 1, 106, 'bot', 'Echo: {\'id\': 91, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 58, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12237, 1, 106, 'bot', 'Echo: {\'id\': 92, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12238, 1, 106, 'bot', 'Echo: {\'id\': 93, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12239, 1, 106, 'bot', 'Echo: {\'id\': 94, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:42', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12240, 1, 106, 'bot', 'Echo: {\'id\': 96, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:42', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12241, 1, 106, 'bot', 'Echo: {\'id\': 97, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:43', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12242, 1, 106, 'bot', 'Echo: {\'id\': 98, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'is_read\': 0, \'reply_to_id\': 58, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:43', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12243, 1, 106, 'bot', 'Echo: {\'id\': 99, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:44', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12244, 1, 106, 'bot', 'Echo: {\'id\': 100, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'is_read\': 0, \'reply_to_id\': 59, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': None}', '2026-08-17 11:25:44', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12245, 106, 1, 'user', '', '2026-08-17 11:25:54', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/63791989bd899f056d02bd8eb99d2368eab3fc5357a494ad9216a19ad593c079.webp\"]', NULL, NULL),
(12246, 1, 106, 'bot', 'Echo: {\'id\': 12245, \'sender_id\': 106, \'receiver_id\': 1, \'sender_type\': \'user\', \'content\': \'\', \'timestamp\': \'2026-08-17 13:25:54\', \'is_read\': 0, \'reply_to_id\': None, \'is_edited\': 0, \'deleted_by_sender\': 0, \'deleted_by_receiver\': 0, \'forwarded_from_id\': None, \'attachments\': \'[\"https:\\\\/\\\\/flasskdev.alwaysdata.net\\\\/attachments\\\\/63791989bd899f056d02bd8eb99d2368eab3fc5357a494ad9216a19ad593c079.webp\"]\'}', '2026-08-17 11:25:54', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12247, 106, 1, 'user', '', '2026-08-17 11:31:15', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/63791989bd899f056d02bd8eb99d2368eab3fc5357a494ad9216a19ad593c079.webp\"]', NULL, NULL),
(12248, 1, 106, 'bot', 'Echo: {\'id\': 12247, \'sender_id\': 106, \'content\': \'\', \'timestamp\': \'2026-08-17 13:31:15\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': \'[\"https:\\\\/\\\\/flasskdev.alwaysdata.net\\\\/attachments\\\\/63791989bd899f056d02bd8eb99d2368eab3fc5357a494ad9216a19ad593c079.webp\"]\'}', '2026-08-17 11:31:16', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12249, 1, 103, 'bot', 'Echo: {\'id\': 53, \'sender_id\': 103, \'sender_info\': {\'name\': \'snzjdjr\', \'username\': \'znzkdjd\', \'avatar_url\': None, \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-28 23:33:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:32', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12250, 1, 106, 'bot', 'Echo: {\'id\': 57, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12251, 1, 106, 'bot', 'Echo: {\'id\': 58, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12252, 1, 106, 'bot', 'Echo: {\'id\': 59, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:34', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12253, 1, 106, 'bot', 'Echo: {\'id\': 60, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:34', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12254, 1, 106, 'bot', 'Echo: {\'id\': 61, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12255, 1, 106, 'bot', 'Echo: {\'id\': 62, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:35', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12256, 1, 106, 'bot', 'Echo: {\'id\': 63, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12257, 1, 106, 'bot', 'Echo: {\'id\': 64, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12258, 1, 106, 'bot', 'Echo: {\'id\': 65, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12259, 1, 106, 'bot', 'Echo: {\'id\': 66, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:37', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12260, 1, 106, 'bot', 'Echo: {\'id\': 67, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:38', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12261, 1, 106, 'bot', 'Echo: {\'id\': 68, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:38', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12262, 1, 106, 'bot', 'Echo: {\'id\': 70, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12263, 1, 106, 'bot', 'Echo: {\'id\': 71, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12264, 1, 106, 'bot', 'Echo: {\'id\': 72, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12265, 1, 106, 'bot', 'Echo: {\'id\': 73, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12266, 1, 106, 'bot', 'Echo: {\'id\': 74, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12267, 1, 106, 'bot', 'Echo: {\'id\': 75, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12268, 1, 106, 'bot', 'Echo: {\'id\': 76, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:42', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12269, 1, 106, 'bot', 'Echo: {\'id\': 77, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:42', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12270, 1, 106, 'bot', 'Echo: {\'id\': 78, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:43', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12271, 1, 106, 'bot', 'Echo: {\'id\': 79, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:43', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12272, 1, 106, 'bot', 'Echo: {\'id\': 80, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:44', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12273, 1, 106, 'bot', 'Echo: {\'id\': 81, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:44', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12274, 1, 106, 'bot', 'Echo: {\'id\': 83, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:45', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12275, 1, 106, 'bot', 'Echo: {\'id\': 84, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:45', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12276, 1, 106, 'bot', 'Echo: {\'id\': 85, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:46', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12277, 1, 106, 'bot', 'Echo: {\'id\': 86, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:46', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12278, 1, 106, 'bot', 'Echo: {\'id\': 87, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:47', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12279, 1, 106, 'bot', 'Echo: {\'id\': 88, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:47', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12280, 1, 106, 'bot', 'Echo: {\'id\': 89, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:48', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12281, 1, 106, 'bot', 'Echo: {\'id\': 90, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:48', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12282, 1, 106, 'bot', 'Echo: {\'id\': 91, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12283, 1, 106, 'bot', 'Echo: {\'id\': 92, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12284, 1, 106, 'bot', 'Echo: {\'id\': 93, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:50', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12285, 1, 106, 'bot', 'Echo: {\'id\': 94, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:50', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12286, 1, 106, 'bot', 'Echo: {\'id\': 96, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12287, 1, 106, 'bot', 'Echo: {\'id\': 97, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12288, 1, 106, 'bot', 'Echo: {\'id\': 98, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:52', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12289, 1, 106, 'bot', 'Echo: {\'id\': 99, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:52', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12290, 1, 106, 'bot', 'Echo: {\'id\': 100, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:53', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12291, 1, 106, 'bot', 'Echo: {\'id\': 101, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:53', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12292, 1, 106, 'bot', 'Echo: {\'id\': 102, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:54', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12293, 1, 106, 'bot', 'Echo: {\'id\': 103, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:55', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12294, 1, 106, 'bot', 'Echo: {\'id\': 104, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'чо\', \'timestamp\': \'2026-07-29 02:57:57\', \'reply_to_id\': 58, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:55', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12295, 1, 106, 'bot', 'Echo: {\'id\': 105, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'привет\', \'timestamp\': \'2026-07-29 02:27:21\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:56', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12296, 1, 106, 'bot', 'Echo: {\'id\': 106, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'пон\', \'timestamp\': \'2026-07-29 02:29:10\', \'reply_to_id\': 59, \'is_bot\': 0, \'is_media\': 0, \'attachments\': None}', '2026-08-17 11:37:56', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12297, 1, 106, 'bot', 'Echo: {\'id\': 12130, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'duration:1444\', \'timestamp\': \'2026-08-11 18:11:16\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': [\'https://flasskdev.alwaysdata.net/attachments/4479ca718c9cfa0707de249305fdc0308e0d6be72ef10bf76a5836f74bf1e36f.webp\']}', '2026-08-17 11:37:57', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12298, 1, 106, 'bot', 'Echo: {\'id\': 12131, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'duration:2027\', \'timestamp\': \'2026-08-11 18:11:24\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': [\'https://flasskdev.alwaysdata.net/attachments/137e6d238584b1d8cedfffd186b4a32227bff517259583cf73737d5c2853f204.webp\']}', '2026-08-17 11:37:57', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12299, 1, 106, 'bot', 'Echo: {\'id\': 12132, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'duration:1147\', \'timestamp\': \'2026-08-11 18:11:26\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': [\'https://flasskdev.alwaysdata.net/attachments/6f68fecf9419843dd72f99be22eef285c5b9e1086a7b0c6a600f4796cb59dedf.webp\']}', '2026-08-17 11:37:58', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12300, 1, 106, 'bot', 'Echo: {\'id\': 12133, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'duration:2326\', \'timestamp\': \'2026-08-11 18:11:31\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': [\'https://flasskdev.alwaysdata.net/attachments/20c629da222722fa67c1bc4977589602df10319091eac20b97f7157843642026.webp\']}', '2026-08-17 11:37:58', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12301, 1, 106, 'bot', 'Echo: {\'id\': 12134, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'\', \'timestamp\': \'2026-08-11 18:24:15\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': [\'https://flasskdev.alwaysdata.net/attachments/0908ea92df1322a54840865a811c2d4eb9ab32412fb37e730c6785a82c3e003c.webp\']}', '2026-08-17 11:37:59', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12302, 1, 106, 'bot', 'Echo: {\'id\': 12135, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'\', \'timestamp\': \'2026-08-11 18:24:15\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': [\'https://flasskdev.alwaysdata.net/attachments/c48d7a077654955899c79df194ef48549d9f9dd0b20bccc87e22eb40296ca69f.webp\']}', '2026-08-17 11:37:59', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12303, 1, 106, 'bot', 'Echo: {\'id\': 12136, \'sender_id\': 106, \'sender_info\': {\'name\': \'Locked\', \'username\': \'locked\', \'avatar_url\': \'https://flasskdev.alwaysdata.net/avatars/0bf3205217300f96c72ae3ff298bb602b8ba329a7f943c8d77da4e9cd79a8735.webp\', \'is_developer\': True, \'is_verified\': False, \'is_bot\': False}, \'content\': \'duration:1331\', \'timestamp\': \'2026-08-11 18:24:53\', \'reply_to_id\': None, \'is_bot\': 0, \'is_media\': 1, \'attachments\': [\'https://flasskdev.alwaysdata.net/attachments/60485e96667675bb6c3403c5ad1548236ab52a49f47b25438fc68f263054bde2.webp\']}', '2026-08-17 11:38:00', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12304, 106, 1, 'user', 'привет, как дела?', '2026-08-17 11:51:52', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12305, 1, 106, 'bot', 'Привет! 😊 У меня всё отлично, спасибо за вопрос! А у тебя как дела? Чем могу помочь или чем поделиться? 💬✨\n\n(Если нужна помощь с чем-то конкретным — дай знать!)', '2026-08-17 11:51:54', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12306, 106, 1, 'user', 'у меня тоже все хорошо', '2026-08-17 11:52:20', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12307, 1, 106, 'bot', 'Отлично! 😊 Если у тебя всё хорошо — это уже отличный старт для хорошего дня! Если хочешь, могу поделиться идеями, как сделать его ещё лучше:\n\n- **План на день**: Что бы ты хотел/а сделать или чего достичь?\n- **Вдохновение**: Хочешь интересные статьи, книги, фильмы или музыку?\n- **Общение**: Можно поговорить на любую тему — от путешествий до абстрактных мыслей!\n- **Позитив**: Хочешь цитату, мем или просто поддержку?\n\nИли просто радуйся — это уже прекрасно! 🌟\n*Что бы ты хотел/а сделать сейчас?* 😊', '2026-08-17 11:52:25', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12308, 106, 1, 'user', 'привет, что это?', '2026-08-17 11:56:22', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12309, 1, 106, 'bot', 'Привет! VibeMessenger — это мессенджер от FlasskDev с акцентом на скорость, удобство и уникальные фичи. Подробнее в канале и чате.', '2026-08-17 11:56:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12310, 106, 1, 'user', 'а где канал можно найти?', '2026-08-17 11:56:35', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12311, 1, 106, 'bot', 'https://t.me/vibemsg', '2026-08-17 11:56:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12312, 106, 1, 'user', 'спасибо.', '2026-08-17 11:56:47', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12313, 1, 106, 'bot', 'Спасибо за отзыв! Если нужна помощь — обращайся. VibeMessenger всегда в тонусе.', '2026-08-17 11:56:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12314, 106, 1, 'user', 'что ты за нейросеть?', '2026-08-17 11:56:57', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12315, 1, 106, 'bot', 'Я — встроенный помощник VibeMessenger, обучаюсь на данных FlasskDev и оптимизирован под работу в экосистеме мессенджера. Отвечаю быстро, без лишнего, используя только актуальные данные и инструкции.', '2026-08-17 11:56:59', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12316, 106, 1, 'user', 'почему после регистрации мне в этого бота пришло такое сообщение?  \"Добро пожаловать в семейство Vibe!\nЗадавай мне любые вопросы, я подскажу что да как!\nМяу.\"', '2026-08-17 11:57:55', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12317, 1, 106, 'bot', 'Это стандартное приветственное сообщение от бота VibeMessenger. Оно генерируется автоматически после регистрации для знакомства с функционалом. Если оно не соответствует ожиданиям, проверь настройки бота или обратись в поддержку через чат или канал.', '2026-08-17 11:57:57', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12318, 106, 1, 'user', 'какие настройки брат?', '2026-08-17 11:58:10', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12319, 1, 106, 'bot', 'Настройки в VibeMessenger:\n- **Приватность**: скрытие статуса, онлайн-режима, просмотра профиля.\n- **Уведомления**: звук, вибрация, отключение для групп/контактов.\n- **Профиль**: аватар, имя, статус, темная тема.\n- **Безопасность**: двухфакторная аутентификация, блокировка аккаунта.\n- **Чаты**: скрытие истории, автоудаление сообщений.\n- **Дополнительно**: синхронизация с облаком, языки, кэш.\n\nПроверь в настройках профиля.', '2026-08-17 11:58:13', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12320, 106, 1, 'user', 'привет', '2026-08-17 12:01:55', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12321, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь тебе с Vibe или чем-то ещё?', '2026-08-17 12:01:57', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12322, 106, 1, 'user', 'какой функционал у мессенджера?', '2026-08-17 12:02:14', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12323, 1, 106, 'bot', 'Вот основной функционал **VibeMessenger**:\n\n✅ **Общение**:\n- Текст, фото, видео, аудио, файлы.\n- Пересылка сообщений и ответы на них.\n- Редактирование и удаление сообщений (для всех или только для себя).\n\n🔒 **Приватность**:\n- Скрытие аватара, статуса \"был в сети\".\n- Ограничение переписки (Всем/Никому/Избранным).\n- Запрет на пересылку сообщений.\n\n🛡️ **Безопасность**:\n- Управление активными устройствами (выход с них).\n- Мутирование пользователей и спам-блоки.\n\n📱 **Дополнительно**:\n- Закрепление сообщений (pin).\n- Push-уведомления.\n- Поддержка ботов.\n- Верификация аккаунтов (галочки).\n\nПодробности в [Телеграм-чате](https://t.me/vibemsgchat) или [канале](https://t.me/vibemsg).', '2026-08-17 12:02:18', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12324, 106, 2, 'user', '@flassk', '2026-08-17 12:09:39', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12325, 106, 2, 'user', 'пон', '2026-08-17 12:19:28', 0, NULL, 0, 0, 0, 106, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/db592a86a0bab161d8977fc58ed8cfb422d9b560fe12ec1686298d8e759e899b.webp\",\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/152b42727d34c65d7e7f5abcad47d5f73fcee0bbaae931daa1e2b7688d51e91a.webp\",\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/659a8410e2ead2aaa90032395ca0376a73ba00cc13cd05a2713fc0c2ba3a38ca.webp\"]', NULL, NULL),
(12326, 106, 107, 'user', 'duration:3443', '2026-08-17 12:19:50', 0, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/edcdb455cd4b96c288576d3abac02fbd8ef0b77927af843dd766429ac065c785.m4a\"]', NULL, NULL),
(12327, 106, 107, 'user', 'duration:50900', '2026-08-17 12:19:52', 0, NULL, 1, 0, 0, 106, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/edcdb455cd4b96c288576d3abac02fbd8ef0b77927af843dd766429ac065c785.m4a\"]', NULL, NULL),
(12328, 106, 1, 'user', 'привет', '2026-08-17 14:53:26', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12329, 106, 1, 'user', '@flassk', '2026-08-17 14:55:25', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12330, 106, 1, 'user', '@locked', '2026-08-17 14:55:35', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12331, 106, 107, 'user', '@asdasaasdasaasdasaasdasaasdasasd', '2026-08-17 14:55:52', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12332, 106, 1, 'user', '@vibe', '2026-08-17 14:56:47', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12333, 106, 1, 'user', '``привет``', '2026-08-17 15:34:09', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12334, 106, 1, 'user', 'приветик----приве----', '2026-08-17 15:34:17', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12335, 106, 1, 'user', 'пон***пон**пон***', '2026-08-17 15:34:24', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12336, 106, 1, 'user', '**пон**', '2026-08-17 15:34:30', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12337, 106, 1, 'user', '__пон__', '2026-08-17 15:34:34', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12338, 106, 1, 'user', '~~пон~~', '2026-08-17 15:34:37', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12339, 106, 1, 'user', '--пон--', '2026-08-17 15:34:40', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12340, 106, 1, 'user', '`пон`', '2026-08-17 15:34:43', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12341, 106, 1, 'user', '`print(\"hello world\")`', '2026-08-17 15:34:54', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12342, 106, 1, 'user', '{{#FF5733:пизда}}', '2026-08-17 15:35:09', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12343, 106, 1, 'user', '||ахаха||', '2026-08-17 15:35:14', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12344, 106, 1, 'user', '>пон>', '2026-08-17 15:35:22', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12345, 106, 1, 'user', 'пон>>', '2026-08-17 15:35:24', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12346, 106, 1, 'user', 'пон>>>>', '2026-08-17 15:35:27', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12347, 106, 1, 'user', 'пон>>п', '2026-08-17 15:35:30', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12348, 106, 1, 'user', 'пон>>а>>', '2026-08-17 15:35:33', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12349, 106, 1, 'user', '**ааа** пиздец', '2026-08-17 22:58:52', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12350, 106, 1, 'user', '`писька https://t.me/aaa`', '2026-08-17 22:59:12', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12351, 106, 1, 'user', '[порно](https://mrush.mobi)', '2026-08-17 22:59:33', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12352, 106, 1, 'user', 'https://t.me/virus_play_bot/app?startapp=openRouletteMain_inviteCodeNw84OclPRz37SsY3_adSegmentCode=freespininlineshare4', '2026-08-17 23:01:08', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12353, 106, 1, 'user', 'ем чо\nПлачу 10000 грамм за вход в бота и прокрутку рулетки (она сразу откроется)\nТОЛЬКО ЛЮДИ С НФТ\nТОЛЬКО НОВЫЕ! СКРИН ОБЯЗАТЕЛЬНО!\n\n(ссылка в лс, сообщение удаляется)\n\n0/1', '2026-08-17 23:38:02', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12354, 106, 1, 'user', 'fullstack developer | t.me/validmafia', '2026-08-17 23:38:05', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12355, 106, 1, 'user', 'https://t.me/flas', '2026-08-17 23:38:12', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12356, 106, 1, 'user', 'google.com', '2026-08-17 23:38:19', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12357, 106, 1, 'user', 'вовлаа[пизда](google.com)', '2026-08-17 23:38:46', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12358, 106, 1, 'user', 'удвдвбада  аладада[пиздец нахуй](djdjdn.com)', '2026-08-17 23:39:15', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12359, 106, 1, 'user', 'пон пон [ем](google.com)ем ем ем', '2026-08-18 00:03:18', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12360, 106, 1, 'user', '||Плачу 10000 грамм за вход в бота и прокрутку рулетки (она сразу откроется)\nТОЛЬКО ЛЮДИ С НФТ\nТОЛЬКО НОВЫЕ! СКРИН ОБЯЗАТЕЛЬНО!\n\n(ссылка в лс, сообщение удаляется)\n\n0/1||', '2026-08-18 00:19:19', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12361, 106, 1, 'user', '||влвдвлалаладалал||', '2026-08-18 00:19:33', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12362, 106, 1, 'user', '||влвдвбадададабабададвдв\nвлвдвдададада||', '2026-08-18 00:20:03', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12363, 106, 1, 'user', '||Плачу 10000 грамм за вход в бота и прокрутку рулетки (она сразу откроется)\nТОЛЬКО ЛЮДИ С НФТ\nТОЛЬКО НОВЫЕ! СКРИН ОБЯЗАТЕЛЬНО!\n\n(ссылка в лс, сообщение удаляется)\n\n0/1Плачу 10000 грамм за вход в бота и прокрутку рулетки (она сразу откроется)\nТОЛЬКО ЛЮДИ С НФТ\nТОЛЬКО НОВЫЕ! СКРИН ОБЯЗАТЕЛЬНО!\n\n(ссылка в лс, сообщение удаляется)\n\n0/1Плачу 10000 грамм за вход в бота и прокрутку рулетки (она сразу откроется)\nТОЛЬКО ЛЮДИ С НФТ\nТОЛЬКО НОВЫЕ! СКРИН ОБЯЗАТЕЛЬНО!\n\n(ссылка в лс, сообщение удаляется)\n\n0/1||', '2026-08-18 00:22:41', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12364, 106, 1, 'user', '>>пизда', '2026-08-18 00:29:04', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12365, 106, 1, 'user', '>>ебануться можно нахуй', '2026-08-18 00:29:12', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12366, 106, 1, 'user', '[авьввлал](google.com)', '2026-08-18 00:29:35', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12367, 106, 1, 'user', 'привет', '2026-08-18 00:33:03', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12368, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь тебе с VibeMessenger?', '2026-08-18 00:33:04', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12369, 106, 1, 'user', 'есть ли у мессенджера инфоканал?', '2026-08-18 00:33:19', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12370, 1, 106, 'bot', 'Да, у Vibe есть **официальный канал в Телеграме** для обновлений и новостей:\n\n🔗 [@vibemsg](https://t.me/vibemsg)', '2026-08-18 00:33:21', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12371, 106, 1, 'user', 'есть ли в мессенджере реакции?', '2026-08-18 00:33:43', 0, NULL, 0, 0, 0, NULL, NULL, '[{\"emoji\":\"❤️\",\"count\":1,\"userIds\":[106]}]', NULL),
(12372, 1, 106, 'bot', 'Нет, в **Vibe** пока нет стандартных **реакций** (эмодзи-откликов), как в Telegram или WhatsApp. Но можно использовать **ответы (reply)** или **пересылку (forward)** для обратной связи.\n\nЕсли это важно для тебя, следи за обновлениями в [официальном чате](https://t.me/vibemsgchat) или канале [@vibemsg](https://t.me/vibemsg). 😊', '2026-08-18 00:33:45', 1, NULL, 0, 0, 0, NULL, NULL, '[{\"emoji\":\"❤️\",\"count\":1,\"userIds\":[106]}]', NULL),
(12373, 105, 106, 'user', 'q', '2026-08-18 00:46:22', 1, NULL, 0, 0, 0, NULL, NULL, '[{\"emoji\":\"👎\",\"count\":1,\"userIds\":[105]},{\"emoji\":\"👍\",\"count\":1,\"userIds\":[106]}]', NULL),
(12374, 106, 105, 'user', 'пон', '2026-08-18 00:46:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12375, 106, 105, 'user', 'отсакаебь мне хуй', '2026-08-18 00:46:46', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12376, 106, 105, 'user', 'порно', '2026-08-18 00:46:49', 1, NULL, 0, 0, 0, NULL, NULL, '[{\"emoji\":\"👍\",\"count\":1,\"userIds\":[105]}]', NULL),
(12377, 106, 105, 'user', '||пмся||', '2026-08-18 00:47:24', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12378, 106, 105, 'user', '[пиздануйк](google.com)', '2026-08-18 00:47:38', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12379, 106, 105, 'user', 'Я ТЕ МАТУХУ ЕБУ', '2026-08-18 00:47:51', 1, NULL, 0, 0, 0, NULL, NULL, '[{\"emoji\":\"👍\",\"count\":1,\"userIds\":[105],\"users\":[]},{\"emoji\":\"🤡\",\"count\":1,\"userIds\":[106],\"users\":[{\"userId\":106,\"timestamp\":1787014689855}]}]', NULL),
(12380, 107, 101, 'user', 'aaa', '2026-08-18 00:57:46', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12381, 107, 101, 'user', 'aaa', '2026-08-18 01:01:49', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12382, 106, 105, 'user', '@locked', '2026-08-18 01:04:39', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12383, 106, 105, 'user', '@dmsms', '2026-08-18 01:05:08', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12384, 106, 105, 'user', '', '2026-08-18 01:08:51', 1, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/39d6703cfe14727ec3816e2fafb8a54bf17e584fbd6a6a79eef8cf0649d61eca.mp3\"]', NULL, NULL),
(12385, 106, 105, 'user', '', '2026-08-18 01:09:08', 1, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/39d6703cfe14727ec3816e2fafb8a54bf17e584fbd6a6a79eef8cf0649d61eca.mp3\"]', NULL, NULL),
(12386, 106, 105, 'user', '', '2026-08-18 01:09:34', 1, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/baa42098d7c2c543c9030221b9a65543df1a5ff99df0bf7a8339d069ae7ce1fa.rar\"]', NULL, NULL);
INSERT INTO `messages` (`id`, `sender_id`, `receiver_id`, `sender_type`, `content`, `timestamp`, `is_read`, `reply_to_id`, `is_edited`, `deleted_by_sender`, `deleted_by_receiver`, `forwarded_from_id`, `attachments`, `reactions`, `reply_markup`) VALUES
(12387, 106, 105, 'user', 'duration:3434', '2026-08-18 01:15:34', 1, NULL, 0, 0, 0, NULL, '[\"https:\\/\\/flasskdev.alwaysdata.net\\/attachments\\/9c30175c7e747efda0f3e3c5850b7a693bde898229cc6894b865525eb8eb023f.m4a\"]', NULL, NULL),
(12388, 107, 101, 'user', 'aaa', '2026-08-18 01:41:27', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12389, 106, 105, 'user', 's', '2026-08-18 01:43:58', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12390, 106, 105, 'user', 'd', '2026-08-18 01:44:00', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12391, 106, 105, 'user', 'ddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdnds', '2026-08-18 01:44:23', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12392, 106, 105, 'user', 'ddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndndndnddddjdkdjdnsjdjdjdjdndn', '2026-08-18 01:44:30', 1, NULL, 0, 0, 0, NULL, NULL, '[{\"emoji\":\"🔥\",\"count\":1,\"userIds\":[105],\"users\":[{\"userId\":105,\"timestamp\":1787017585396}]}]', NULL),
(12393, 106, 105, 'user', '**пизда** ебаная [okak](google.com) пиздануйк можна аэахаахзаах __да__', '2026-08-18 01:52:57', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12394, 106, 105, 'user', '__а__ окакккк сикс севен**а**а', '2026-08-18 01:53:21', 1, 12392, 0, 0, 0, NULL, NULL, NULL, NULL),
(12395, 106, 105, 'user', 'пон', '2026-08-18 01:54:57', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12396, 106, 105, 'user', '**пизда** ебаная [okak](google.com) пиздануйк можна аэахаахзаах __да__', '2026-08-18 08:21:08', 1, NULL, 0, 0, 0, 106, NULL, NULL, NULL),
(12397, 106, 105, 'user', '.', '2026-08-18 08:21:19', 1, 12393, 0, 0, 0, NULL, NULL, NULL, NULL),
(12398, 107, 101, 'user', 'aaa', '2026-08-18 08:41:21', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12399, 107, 101, 'user', 'aaa', '2026-08-18 08:42:39', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12400, 106, 105, 'user', '.', '2026-08-18 08:42:56', 1, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(12401, 106, 105, 'user', 'порно', '2026-08-18 08:43:09', 1, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12402, 106, 105, 'user', 'ем чо', '2026-08-18 08:43:14', 0, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(12403, 106, 105, 'user', 'пон', '2026-08-18 08:43:27', 0, NULL, 0, 0, 1, NULL, NULL, NULL, NULL),
(12404, 106, 1, 'user', 'привет', '2026-08-18 10:59:30', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12405, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Спрашивай про VibeMessenger или просто общайся!', '2026-08-18 10:59:32', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#1E88E5\",\"text_color\":\"#FFFFFF\"},{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#43A047\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"},{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12406, 106, 1, 'user', 'привет', '2026-08-18 11:00:32', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12407, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Вопросы о Vibe или просто общаться?', '2026-08-18 11:00:33', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#1E88E5\",\"text_color\":\"#FFFFFF\"},{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#43A047\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"},{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12408, 106, 1, 'user', 'привет', '2026-08-18 11:01:04', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12409, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Вопросы по Vibe или просто общаться?', '2026-08-18 11:01:06', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#1E88E5\",\"text_color\":\"#FFFFFF\"},{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#43A047\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"},{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12410, 106, 1, 'user', 'приветик', '2026-08-18 11:02:53', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12411, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? В Vibe всё круто, да? 🚀', '2026-08-18 11:02:55', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#1E88E5\",\"text_color\":\"#FFFFFF\"},{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#43A047\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"},{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12412, 106, 1, 'user', 'пр', '2026-08-18 11:11:24', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12413, 106, 1, 'user', 'пр', '2026-08-18 11:11:34', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12414, 1, 106, 'bot', 'В Vibe можно отправлять **пропуски** (voice messages) — короткие голосовые сообщения. Их можно записывать и отправлять, как и обычные сообщения.\n\nЕсли имелось в виду что-то другое — уточните! 😊', '2026-08-18 11:11:36', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"},{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"},{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12415, 106, 1, 'user', 'привет', '2026-08-18 11:22:05', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12416, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Спроси про Vibe или просто поговорим! 🚀', '2026-08-18 11:22:07', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12417, 1, 103, 'bot', 'Привет! 😊 Как я могу помочь? Вопросы по Vibe или просто общаться?', '2026-08-18 12:17:44', 0, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12418, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Спрашивай про Vibe или просто общайся!', '2026-08-18 12:17:45', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12419, 1, 106, 'bot', 'Понял! 😊 В чём помощь по **VibeMessenger**? Например:\n- Как отправить кружочки (video messages)?\n- Как настроить приватность?\n- Как создать инлайн-кнопку с Glassmorphism?', '2026-08-18 12:17:47', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12420, 1, 106, 'bot', 'Привет! 😊 Я здесь, чтобы помочь с **VibeMessenger** — рассказать про функции, форматирование, боты или что-то ещё. Спроси, что нужно! 🚀\n\nНапример:\n- *\"Как отправить кружочек?\"*\n- *\"Как сделать жирный текст?\"*\n- *\"Как заблокировать пользователя?\"*', '2026-08-18 12:17:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12421, 1, 106, 'bot', 'Привет! 😊 Чем могу помочь?', '2026-08-18 12:17:50', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12422, 1, 103, 'bot', 'Привет! 😊 Как я могу помочь? Спрашивай про Vibe или просто общайся!', '2026-08-18 12:17:57', 0, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12423, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Вопросы о Vibe или просто общаться?', '2026-08-18 12:17:59', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12424, 1, 106, 'bot', 'В VibeMessenger **\"пон\"** — это сокращение от **\"понимание\"** или **\"понял\"**, но в мессенджере его нет как команды или функции.\n\nЕсли ты имел в виду что-то конкретное (например, поиск, настройки или функцию), уточни, пожалуйста! 😊\n\n*Пример:*\n- *\"Как найти чат?\"*\n- *\"Можно ли скрыть статус \'был в сети\'?\"*', '2026-08-18 12:18:01', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12425, 1, 106, 'bot', 'Привет! 😊 Я здесь, чтобы помочь с **VibeMessenger** — рассказать о функциях, ответить на вопросы о чатах, сообщениях или настройках. Спроси, что нужно! 🚀\n\nНапример:\n- *\"Как отправить кружочек?\"*\n- *\"Как закрепить сообщение?\"*\n- *\"Как сделать инлайн-кнопки?\"*', '2026-08-18 12:18:03', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12426, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Спрашивай про VibeMessenger или просто общайся!', '2026-08-18 12:18:05', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12427, 1, 106, 'bot', 'Понял! 😊 Как могу помочь с **VibeMessenger**? Например:\n- Показать, как отправить **спойлер**?\n- Настроить **приватность**?\n- Создать **инлайн-кнопку** с Glassmorphism?\n- Или что-то другое? 👇\n\n*(Напоминаю: если вопрос не про Vibe — отвечу по теме!)*', '2026-08-18 12:18:07', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12428, 1, 106, 'bot', 'Привет! 😊 Я здесь, чтобы помочь с **VibeMessenger** — рассказать о функциях, ответить на вопросы про чаты, сообщения, боты и всё такое. Спроси, что интересует! 🚀\n\nНапример:\n- *\"Как отправить кружочек?\"*\n- *\"Можно ли скрыть статус \'был в сети\'?\"*\n- *\"Как создать инлайн-кнопку?\"*', '2026-08-18 12:18:09', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12429, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Спрашивай про VibeMessenger или просто общайся!', '2026-08-18 12:18:11', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12430, 1, 106, 'bot', 'Понял! 😊 Как могу помочь с **VibeMessenger**? Например:\n- Показать, как отправить кружочек (video message)?\n- Рассказать про форматирование текста?\n- Помогут ли инлайн-кнопки с кастомными цветами?', '2026-08-18 12:18:13', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12431, 1, 106, 'bot', 'Привет! 😊 Я здесь, чтобы помочь с **VibeMessenger** — рассказать про функции, помочь настроить или просто подсказать, как что-то сделать. Спроси, что интересует! 🚀\n\nНапример:\n- *\"Как отправить кружочек?\"*\n- *\"Как закрепить сообщение?\"*\n- *\"Как сделать реакцию на сообщение?\"*', '2026-08-18 12:18:15', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12432, 1, 103, 'bot', 'Привет! 😊 Как я могу помочь? Спрашивай про Vibe или просто общайся!', '2026-08-18 12:18:23', 0, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12433, 1, 103, 'bot', 'Привет! 😊 Как я могу помочь? В Vibe всё отлично?', '2026-08-18 12:19:38', 0, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12434, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Спрашивай про Vibe или просто общайся!', '2026-08-18 12:19:40', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12435, 1, 106, 'bot', 'Понял! 😊 Как могу помочь с **VibeMessenger**? Например:\n- Показать, как отправить **кружочки** (видео-сообщения)?\n- Рассказать о **реакциях** или **Markdown**?\n- Помогу с **настройками приватности** или **блоками**?', '2026-08-18 12:19:41', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12436, 1, 106, 'bot', 'Привет! 😊 Я здесь, чтобы помочь с **VibeMessenger** — рассказать о функциях, ответить на вопросы о чатах, сообщениях, ботов и т.д.\n\nЧто именно интересует? 👇\nНапример:\n- Как отправить кружочек (видео-сообщение)?\n- Как закрепить сообщение в чате?\n- Как настроить приватность?\n- Или просто хочешь попробовать мессенджер? 😉\n\nСпрашивай!', '2026-08-18 12:19:44', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12437, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь тебе с Vibe?', '2026-08-18 12:19:45', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12438, 1, 106, 'bot', 'Понял! 😊 В чём вопрос по **VibeMessenger**? Спрашивай — помогу! 🚀\n\n*(Например: как отправить кружочки? Как настроить приватность? Как создать бота?)*', '2026-08-18 12:19:47', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12439, 1, 106, 'bot', 'Привет! 😊 Я здесь, чтобы помочь с **VibeMessenger** — рассказать о функциях, ответить на вопросы про чаты, сообщения, боты и всё такое. Спроси, что интересует! 🚀\n\nНапример:\n- *\"Как отправить кружочек?\"*\n- *\"Как сделать инлайн-кнопки?\"*\n- *\"Как скрыть статус \'был в сети\'?\"*', '2026-08-18 12:19:49', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12440, 1, 106, 'bot', 'Привет! 😊 Как я могу помочь? Вопросы о Vibe?', '2026-08-18 12:19:51', 1, NULL, 0, 0, 0, NULL, NULL, NULL, '{\"inline_keyboard\":[[{\"text\":\"ℹ️ О мессенджере\",\"callback_data\":\"btn_info\",\"bg_color\":\"#FFFFFF\",\"text_color\":\"#000000\"}],[{\"text\":\"📢 Канал Vibe\",\"url\":\"https:\\/\\/t.me\\/vibemsg\",\"bg_color\":\"#808080\",\"text_color\":\"#FFFFFF\"}],[{\"text\":\"⚡ Пинг\",\"callback_data\":\"btn_ping\"}],[{\"text\":\"❓ Помощь\",\"callback_data\":\"btn_help\",\"bg_color\":\"#8E24AA\",\"text_color\":\"#FFFFFF\"}]]}'),
(12441, 106, 1, 'user', 'привет', '2026-08-18 12:20:41', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12442, 106, 1, 'user', 'как дела?', '2026-08-18 12:20:51', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12443, 106, 1, 'user', 'пон', '2026-08-18 12:21:01', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12444, 106, 1, 'user', 'приветик', '2026-08-18 12:24:35', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12445, 106, 1, 'user', 'привет', '2026-08-18 12:27:13', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL),
(12446, 106, 1, 'user', 'привет', '2026-08-18 12:27:31', 0, NULL, 0, 0, 0, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `muted_users`
--

CREATE TABLE `muted_users` (
  `user_id` int(11) NOT NULL,
  `muted_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `muted_users`
--

INSERT INTO `muted_users` (`user_id`, `muted_id`) VALUES
(107, 1),
(107, 106);

-- --------------------------------------------------------

--
-- Table structure for table `pending_users`
--

CREATE TABLE `pending_users` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `code` varchar(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `attempts` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pinned_messages`
--

CREATE TABLE `pinned_messages` (
  `id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `pinned_by_id` int(11) NOT NULL,
  `pinned_for_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pinned_messages`
--

INSERT INTO `pinned_messages` (`id`, `message_id`, `pinned_by_id`, `pinned_for_id`, `created_at`) VALUES
(38, 153, 106, 106, '2026-08-01 20:44:02'),
(39, 195, 107, 107, '2026-08-02 21:38:28'),
(40, 12148, 106, 106, '2026-08-12 08:18:10'),
(41, 12151, 106, 106, '2026-08-12 10:25:24'),
(42, 12393, 106, 106, '2026-08-18 08:20:46');

-- --------------------------------------------------------

--
-- Table structure for table `privacy_settings`
--

CREATE TABLE `privacy_settings` (
  `user_id` int(11) NOT NULL,
  `activity` varchar(20) DEFAULT 'EVERYONE',
  `activity_users` text DEFAULT NULL,
  `avatar` varchar(20) DEFAULT 'EVERYONE',
  `avatar_users` text DEFAULT NULL,
  `forwarded` varchar(20) DEFAULT 'EVERYONE',
  `forwarded_users` text DEFAULT NULL,
  `messages` varchar(20) DEFAULT 'EVERYONE',
  `messages_users` text DEFAULT NULL,
  `status` varchar(20) DEFAULT 'EVERYONE',
  `status_users` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `privacy_settings`
--

INSERT INTO `privacy_settings` (`user_id`, `activity`, `activity_users`, `avatar`, `avatar_users`, `forwarded`, `forwarded_users`, `messages`, `messages_users`, `status`, `status_users`) VALUES
(106, 'NOBODY', '[107]', 'EVERYONE', '[107]', 'NOBODY', '[]', 'EVERYONE', '[107]', 'EVERYONE', '[107]'),
(107, 'EVERYONE', '[]', 'EVERYONE', '[]', 'NOBODY', '[]', 'EVERYONE', '[]', 'EVERYONE', '[]');

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `id` int(11) NOT NULL,
  `theme` enum('SPAM','SCAM','DRUGS','WEAPONS','PORNOGRAPHY','CSAM','VIOLENCE','HARASSMENT','HATESPEECH','IMPERSONATION','MISINFORMATION') NOT NULL,
  `from_user` int(11) NOT NULL,
  `to_user` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `comment` varchar(512) DEFAULT NULL,
  `time` timestamp NULL DEFAULT current_timestamp(),
  `punishment` set('ban','freeze','spamblock') DEFAULT NULL,
  `punishment_time` timestamp NULL DEFAULT NULL,
  `status` enum('pending','accepted','declined') DEFAULT 'pending',
  `resolved_by` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reports`
--

INSERT INTO `reports` (`id`, `theme`, `from_user`, `to_user`, `message_id`, `comment`, `time`, `punishment`, `punishment_time`, `status`, `resolved_by`) VALUES
(1, '', 107, 108, 12122, '', '2026-08-09 13:40:47', NULL, NULL, 'accepted', 108);

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `device_id` varchar(255) NOT NULL,
  `device_name` varchar(255) DEFAULT NULL,
  `os_version` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `last_active` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`id`, `user_id`, `device_id`, `device_name`, `os_version`, `location`, `last_active`) VALUES
(1, 107, '17615384-0f53-4e93-b6d6-6df40dfb9979', 'sdk_gphone64_x86_64', 'Android 17', 'Харьков, Украина', '2026-08-09 15:20:16'),
(62, 106, '11a8a980-bfd1-4772-9b10-a45e894f3b0b', 'Xiaomi 15T Pro', 'Android 17', 'Харьков, Украина', '2026-08-09 10:14:10'),
(1050, 106, '19c743ae-7be3-4654-a4c1-3136a6a7d95f', 'Xiaomi 15T Pro', 'Android 17', 'Харьков, Украина', '2026-08-10 17:18:54'),
(1057, 106, 'a7c0aec2-a5f5-4a5d-9418-adc944d49db7', 'Xiaomi 15T Pro', 'Android 17', 'Харьков, Украина', '2026-08-15 19:33:43'),
(1061, 107, 'a8d1a530-0392-4003-9f6a-fac48b937549', 'sdk_gphone64_x86_64', 'Android 17', 'Харьков, Украина', '2026-08-10 21:18:46'),
(1220, 105, '781b713e-e738-415d-b8fe-2a9cc6fd583d', 'sdk_gphone64_x86_64', 'Android 17', 'Харьков, Украина', '2026-08-18 08:52:30'),
(1372, 106, '7318332c-8a56-4a12-83a9-8af907a982aa', 'Xiaomi 15T Pro', 'Android 17', 'Харьков, Украина', '2026-08-17 10:08:22'),
(1485, 106, 'f2f0b098-1985-4e41-aded-0ec7c5a1fcec', 'Xiaomi 15T Pro', 'Android 17', 'Харьков, Украина', '2026-08-18 12:27:17');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL,
  `register_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `is_online` tinyint(1) DEFAULT 0,
  `last_seen` datetime DEFAULT current_timestamp(),
  `is_developer` tinyint(1) DEFAULT 0,
  `is_verified` tinyint(1) DEFAULT 0,
  `avatar_url` varchar(255) DEFAULT NULL,
  `about` text DEFAULT NULL,
  `is_banned` tinyint(1) NOT NULL DEFAULT 0,
  `is_freezed` tinyint(1) NOT NULL DEFAULT 0,
  `freeze_time` timestamp NOT NULL,
  `is_spamblock` tinyint(1) NOT NULL DEFAULT 0,
  `spamblock_time` timestamp NOT NULL,
  `last_device_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `name`, `username`, `register_date`, `is_online`, `last_seen`, `is_developer`, `is_verified`, `avatar_url`, `about`, `is_banned`, `is_freezed`, `freeze_time`, `is_spamblock`, `spamblock_time`, `last_device_id`) VALUES
(98, 'hesushesus97@gmail.com', '', 'hesus', '2026-07-28 18:57:33', 1, '2024-07-10 20:05:26', 1, 1, NULL, NULL, 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', NULL),
(99, 'snzjsjd@mail.ru', 'snsjdj', 'sjskznx', '2026-07-28 20:28:01', 0, '2026-07-28 22:28:01', 0, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', NULL),
(100, 'snzjsjdj@mail.ru', 'snzjzs', 'snskdn', '2026-07-28 20:29:05', 0, '2026-07-28 22:48:15', 0, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', NULL),
(101, 'snzjdndjdj@mail.ru', 'znsjejd', 'snskdndn', '2026-07-28 20:48:36', 0, '2026-07-28 22:48:36', 0, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', NULL),
(102, 'sjsjdnnd@mail.ru', 'shzjsne', 'smxkjdd', '2026-07-28 21:00:08', 0, '2026-07-28 23:00:08', 0, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', NULL),
(103, 'xbzjdjdnbz@mail.ru', 'snzjdjr', 'znzkdjd', '2026-07-28 21:06:16', 0, '2026-07-29 00:08:44', 1, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', NULL),
(104, 'sbxjendndj@gmail.com', 'andrei', 'dnxjendnd', '2026-07-28 22:09:13', 0, '2026-07-29 00:20:05', 0, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', NULL),
(105, 'ammask@mail.ru', 'sbsjsndnd', 'snxjndxn', '2026-07-28 22:19:15', 0, '2026-08-18 12:29:21', 0, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '781b713e-e738-415d-b8fe-2a9cc6fd583d'),
(106, 'flasskdev@gmail.com', 'Locked', 'locked', '2026-07-28 22:28:21', 1, '2026-08-18 14:25:19', 1, 0, 'https://flasskdev.alwaysdata.net/avatars/4297e122fc20a8ba92f2f445dd77cdf843fb8fb761a5706d7f550ef97a6f567d.webp', 'Вопросы в поддержку.', 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 'f2f0b098-1985-4e41-aded-0ec7c5a1fcec'),
(107, 'kskskskks@gmail.com', 'asdasaasdasaasdasaasdasaasdasasd', 'asdasaasdasaasdasaasdasaasdasasd', '2026-07-29 01:47:51', 0, '2026-08-10 23:20:04', 1, 1, '', '9223', 1, 0, '0000-00-00 00:00:00', 0, '2026-08-26 14:42:24', 'a8d1a530-0392-4003-9f6a-fac48b937549'),
(108, 'ejsksnsj@gmail.com', 'dayn', 'dalbaebsban', '2026-08-09 10:14:49', 0, '2026-08-09 17:28:18', 1, 0, NULL, NULL, 0, 0, '0000-00-00 00:00:00', 1, '0000-00-00 00:00:00', 'f75aa2ab-0d26-47ae-8111-d4c277a40d83');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `banned_devices`
--
ALTER TABLE `banned_devices`
  ADD PRIMARY KEY (`device_id`);

--
-- Indexes for table `blocked_users`
--
ALTER TABLE `blocked_users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`blocked_id`),
  ADD KEY `blocked_id` (`blocked_id`);

--
-- Indexes for table `bots`
--
ALTER TABLE `bots`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `bot_callbacks`
--
ALTER TABLE `bot_callbacks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_bot_offset` (`bot_id`,`id`);

--
-- Indexes for table `bot_callback_answers`
--
ALTER TABLE `bot_callback_answers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_delivered` (`is_delivered`,`id`);

--
-- Indexes for table `bot_updates`
--
ALTER TABLE `bot_updates`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_bot_id` (`bot_id`),
  ADD KEY `idx_bot_msg` (`bot_id`,`message_id`);

--
-- Indexes for table `chat_actions`
--
ALTER TABLE `chat_actions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `device_tokens`
--
ALTER TABLE `device_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_sender` (`sender_id`),
  ADD KEY `idx_receiver` (`receiver_id`),
  ADD KEY `idx_chat` (`sender_id`,`receiver_id`,`timestamp`),
  ADD KEY `fk_message_reply` (`reply_to_id`);

--
-- Indexes for table `muted_users`
--
ALTER TABLE `muted_users`
  ADD PRIMARY KEY (`user_id`,`muted_id`);

--
-- Indexes for table `pending_users`
--
ALTER TABLE `pending_users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pinned_messages`
--
ALTER TABLE `pinned_messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `message_id` (`message_id`);

--
-- Indexes for table `privacy_settings`
--
ALTER TABLE `privacy_settings`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_reports_from_user` (`from_user`),
  ADD KEY `fk_reports_to_user` (`to_user`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`device_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `blocked_users`
--
ALTER TABLE `blocked_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `bots`
--
ALTER TABLE `bots`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `bot_callbacks`
--
ALTER TABLE `bot_callbacks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=48;

--
-- AUTO_INCREMENT for table `bot_callback_answers`
--
ALTER TABLE `bot_callback_answers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `bot_updates`
--
ALTER TABLE `bot_updates`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `chat_actions`
--
ALTER TABLE `chat_actions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `device_tokens`
--
ALTER TABLE `device_tokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1023;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12447;

--
-- AUTO_INCREMENT for table `pending_users`
--
ALTER TABLE `pending_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=133;

--
-- AUTO_INCREMENT for table `pinned_messages`
--
ALTER TABLE `pinned_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `sessions`
--
ALTER TABLE `sessions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2021;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=109;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `blocked_users`
--
ALTER TABLE `blocked_users`
  ADD CONSTRAINT `blocked_users_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `blocked_users_ibfk_2` FOREIGN KEY (`blocked_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `fk_message_reply` FOREIGN KEY (`reply_to_id`) REFERENCES `messages` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `pinned_messages`
--
ALTER TABLE `pinned_messages`
  ADD CONSTRAINT `pinned_messages_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `privacy_settings`
--
ALTER TABLE `privacy_settings`
  ADD CONSTRAINT `privacy_settings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `fk_reports_from_user` FOREIGN KEY (`from_user`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_reports_to_user` FOREIGN KEY (`to_user`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
