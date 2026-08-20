# VibeMessenger Refactor and Security Review

## Scope and outcome

This review targeted the Android/Jetpack Compose chat experience, WebSocket delivery behavior, local security controls, and the Python support bot. The changes are implemented directly in the project source tree. They focus on the reported inline-button failures, chat-navigation jank, reply/pin jump instability, pinned-message state drift, visual polish, and bot troubleshooting quality.

The review identified several high-priority issues: callbacks could be discarded during a socket reconnect; chat payloads were written to logcat; pin ordering depended on numeric IDs rather than chronology; message jumps could use an asynchronously stale list index while the list was animating; and sensitive local state plus bot credentials were stored in plaintext.

| Area | Implemented change | Result |
| --- | --- | --- |
| Inline bot buttons | Confined swipe detection to the message body, removed expensive per-button liquid distortion, enlarged touch targets, and added a lightweight native-style surface. | Button taps no longer compete with the parent chat-row drag recognizer, while scrolling has less GPU work. |
| Callback delivery | Added bounded queuing for raw WebSocket commands, including bot callbacks, and replays queued commands after reconnect. | A tap is retained across transient socket disconnections instead of silently dropping. |
| Reply and pin navigation | Builds a lazy-list index from the exact displayed grouped-message snapshot, uses deterministic scroll positioning, clips chat bounds, and removes placement animations from context-window replacement. | Eliminates the primary causes of stale index jumps, background layering, and item-overlap glitches. |
| Pinned state | Normalizes pinned messages by timestamp, resets state when switching chats, handles empty server pin lists, and uses timestamp-based visibility selection. | The header consistently reflects the newest relevant pin and does not inherit state from another chat. |
| Navigation performance | Replaced full-screen tab slide transitions with short fades, reduced selector distortion, removed list-wide chat-row placement animation, and stopped per-row audio metadata extraction. | Less compositing, less work during return navigation, and fewer asynchronous jobs created by recycled rows. |
| Android local security | Migrates account/passcode state to Android-Keystore-backed encrypted preferences and disables app data backup. | Login data, passcode, and privacy state are no longer stored in an ordinary backupable preferences file. |
| Python support bot | Moved secrets to environment variables; added input bounds, secret-detection, deterministic issue triage, short rolling context, safe error handling, and purpose-led callback menus. | Users receive clearer automated diagnostics without the bot echoing internal exceptions or requesting sensitive information. |

## Updated files

| File | Main responsibility of the change |
| --- | --- |
| `app/src/main/java/com/flasskdev/vibe/data/VibeWebSocket.kt` | Queues raw commands on reconnection and prevents private incoming/outgoing payloads from being logged. |
| `app/src/main/java/com/flasskdev/vibe/ui/screens/ChatScreen.kt` | Stabilizes item indexing, jump navigation, z-order, pin-banner interaction, and swipe boundaries. |
| `app/src/main/java/com/flasskdev/vibe/ui/viewmodels/ChatScreenViewModel.kt` | Normalizes pin order and resets stale per-chat pin/highlight state. |
| `app/src/main/java/com/flasskdev/vibe/ui/components/Components.kt` | Redesigns inline keyboard buttons with larger targets and lower rendering cost. |
| `app/src/main/java/com/flasskdev/vibe/ui/screens/MainContainerScreen.kt` | Uses lower-cost tab transitions and gentler navigation-indicator motion. |
| `app/src/main/java/com/flasskdev/vibe/ui/screens/ChatListScreen.kt` | Avoids one-second main-thread connectivity checks, repetitive row animation, and media metadata fetching in every recycled item. |
| `app/src/main/java/com/flasskdev/vibe/data/UserPreferences.kt` | Adds encrypted storage and one-time migration/cleanup of the legacy preferences file. |
| `app/src/main/AndroidManifest.xml` | Disables device/cloud backup for sensitive messenger data. |
| `app/build.gradle.kts` | Adds the AndroidX security-crypto dependency required by encrypted preferences. |
| `VIBEBOTS-python-lib/config.py` | Removes hard-coded credentials and validates required environment variables at startup. |
| `VIBEBOTS-python-lib/test.py` | Replaces generic AI-only replies with secure contextual support triage and callback flows. |
| `VIBEBOTS-python-lib/.env.example` | Provides a non-secret deployment configuration template. |
| `.gitignore` | Ignores environment files, bot config, service accounts, and signing material. |

## Support bot behavior

The bot now recognizes common support categories before invoking the language model: account security, connectivity, notifications, media uploads, login/access, and messaging interactions. Each category returns immediate, safe first steps and asks only for non-sensitive diagnostic details. Other requests retain a short per-user conversation context and use the configured Mistral model with a constrained support prompt.

The inline menu now uses compact actions for diagnostics, connectivity, account/privacy, and message/media issues. Callback queries are acknowledged before the fuller guide is sent, allowing the Android client to clear its pending callback state promptly.

> The bot never needs a password, confirmation code, API key, bot token, or payment information. The new handler rejects messages containing likely secrets and asks the user to resend a redacted description.

## Required deployment action

The old tracked bot configuration contained live credential material. Removing it from the working tree and ignoring future copies is necessary but not sufficient: those credentials should be treated as exposed.

| Priority | Required action |
| --- | --- |
| Critical | Revoke and reissue the Vibe bot token and the Mistral API key, then set the replacements as `VIBE_BOT_TOKEN` and `MISTRAL_API_KEY` in the bot host’s environment. |
| Critical | Revoke/recreate any Firebase service-account key that has been committed or shared, and remove secret files from Git history with an approved history-rewrite process. |
| High | Protect deployment variables in the hosting platform’s secret store; do not create a populated `.env` file in Git. |
| High | Ensure the production backend authorizes every WebSocket action server-side. Client-side ID checks and UI state are not authorization controls. |

For the Python bot, use `VIBEBOTS-python-lib/.env.example` as a naming template. The runtime reads process environment variables; it intentionally fails early if the mandatory values are missing.

## Verification performed

The final Python modules passed syntax compilation on the connected desktop:

```text
py -3 -m py_compile config.py test.py
PYTHON_COMPILE_OK
```

Android source validation was initiated through Gradle. The local desktop project has a configured Android SDK but no Java runtime available on `JAVA_HOME` or `PATH`. A separate Java 21 environment reached Gradle task resolution but lacks an Android SDK. Consequently, a complete Kotlin/Android build could not be run in the available environments; this is an environment limitation, not a claimed successful Android compile.

| Check | Status | Notes |
| --- | --- | --- |
| Python syntax compilation | Passed | Final `config.py` and `test.py` compile successfully. |
| Gradle configuration loading | Reached task resolution | The Android module and its Gradle configuration were loaded. |
| Full Android Kotlin compilation | Blocked by environment | Desktop lacks Java; isolated Java environment lacks Android SDK. |
| Runtime/device UI validation | Pending | Requires a local JDK + Android SDK/emulator or a physical device. |

## Recommended acceptance test

After restoring a local Java 21 configuration, run `gradlew.bat :app:assembleDebug` from the project root. Then test these scenarios on a physical device or emulator:

1. Tap callback buttons repeatedly while the socket is reconnecting; each action should be delivered once and the UI should remain responsive.
2. Open a media-heavy chat, return to the chat list, and re-enter it repeatedly; transitions should not stall and audio previews should not trigger background metadata work per recycled row.
3. Open a reply or pinned-message target both inside and outside the local message window; the list should jump to a visible, highlighted target without overlapping rows.
4. Pin messages from different dates, switch chats, return, and scroll through the conversation; the pin counter and preview should follow newest-first chronology.
5. Upgrade an existing install; verify login/profile state is preserved, then inspect that the legacy `vibe_user_prefs` file has been cleared after migration.
6. Start the support bot only with new environment credentials and verify that it never echoes secret values or raw provider errors.

## Remaining review boundary

This implementation improves the reviewed Android client and Python bot paths. It does not substitute for a production infrastructure review of the PHP/WebSocket server, database authorization rules, rate limits, file-upload validation, TLS/certificate configuration, Firebase IAM, dependency vulnerability scanning, or penetration testing. Those controls should be audited separately before a public production release.
