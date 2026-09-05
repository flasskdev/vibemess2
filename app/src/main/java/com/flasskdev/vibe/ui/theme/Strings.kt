package com.flasskdev.vibe.ui.theme

import androidx.compose.runtime.compositionLocalOf

/**
 * NOTE: intentionally an interface with two singleton implementations instead of a
 * `data class`.
 *
 * A JVM/DEX method signature is limited to 255 argument slots (the implicit `this`
 * counts as one). A `data class` with N properties generates a constructor AND a
 * `copy()` with N parameters, so once this table passed ~254 entries the generated
 * `<init>` / `copy` no longer fit and the DEX verifier failed at install time
 * ("invalid arg count" on invoke-direct/range). Incremental Apply Changes hid it;
 * a clean rebuild did not.
 *
 * Properties on an object are plain field initializers + getters, so there is no
 * per-method argument limit and the table can grow freely. Adding a string means:
 * declare it in the interface, then override it in BOTH objects (the compiler
 * enforces that, exactly like named constructor arguments did).
 */
interface VibeStrings {

    // Auth & Nickname & Verification
    val createAccount: String
    val welcomeBack: String
    val emailLabel: String
    val usernameLabel: String
    val emailInvalidFormat: String
    val continueBtn: String
    val verificationTitle: String
    val verificationSubtitle: (String) -> String
    val verifyBtn: String
    val verifyLoading: String
    val codeInvalid: String
    val nicknameTitle: String
    val nicknameLabel: String
    val saveBtn: String
    val saveLoading: String
    val errorSaving: String
    val freezedAcc: String
    val bannedAcc: String


    // Main Container & Tabs
    val tabChats: String
    val tabSettings: String
    val tabProfile: String


    // Profile
    val profileTitle: String
    val statusOnline: String
    val statusBot: String
    val statusUnknown: String
    val actionForward: String
    val draftLabel: String
    val badgeVerified: String
    val badgeDeveloper: String
    val badgeBot: String
    val aboutLabel: String
    val registerDateLabel: String
    val btnTheme: String
    val themeDark: String
    val themeLight: String
    val btnLanguage: String
    val btnLogout: String
    val logoutConfirmTitle: String
    val logoutConfirmText: String
    val logoutCancel: String
    val logoutConfirm: String
    val userLabel: String


    // Chats
    val chatsTitle: String
    val chatsEmptyTitle: String
    val chatsEmptySubtitle: String
    val searchPlaceholder: String
    val globalSearchResults: String
    val typing: String
    val connecting: String
    val waitingForNetwork: String
    val monthsShort: List<String>


    // Last Seen
    val dateToday: String
    val dateYesterday: String
    val lastSeenRecently: String
    val lastSeenLongAgo: String
    val lastSeenInWeek: String
    val lastSeenInMonth: String
    val lastSeenToday: (String) -> String
    val lastSeenYesterday: (String) -> String
    val lastSeenDate: (String, String) -> String


    // Chat Screen
    val backBtn: String
    val messagePlaceholder: String
    val replyTo: String
    val emptyChat: String
    val chatHistoryCleared: String
    val chatHistoryEmpty: String
    val deletedAcc: String


    // Onboarding
    val onboardingPages: List<Pair<String, String>>


    // Chat Actions
    val selectedMessagesCount: (Int) -> String
    val deleteMessagesTitle: String
    val deleteMessagesText: (Int) -> String
    val deleteForEveryone: (String) -> String
    val deleteForEveryoneAlsoMine: (String) -> String
    val deleteBtn: String
    val cancelBtn: String
    val forwardMessageTitle: String
    val noRecentChats: String
    val editMessageTitle: String
    val sendBtn: String
    val forwardedFrom: (String) -> String
    val replyDefault: String


    // Pinned messages and others
    val pinnedMessage: String
    val pinnedMessages: String
    val pinMessage: String
    val pinMessageConfirm: String
    val unpinMessage: String
    val unpinMessageConfirm: String
    val unpinAll: String
    val unpinAllConfirm: String
    val forBoth: (String) -> String
    val pin: String
    val unpin: String
    val you: String
    val edit: String


    // Settings & Profile editing
    val usernameTaken: String
    val usernameAvailable: String
    val usernameDescription: String
    val usernameMinLength: String
    val nicknameDescription: String
    val bioDescription: String
    val doneBtn: String


    // Chat restrictions & status
    val userRestrictedMessaging: String
    val userHiddenAccount: String
    val editedLabel: String


    // Mute
    val muteNotifications: String
    val unmuteNotifications: String


    // System messages
    val pinnedMessageSystemText: (String, String) -> String


    // Privacy settings
    val privacyScreenTitle: String
    val privacyTwoFactor: String
    val privacyPasscodeLogin: String
    val privacyBlocked: String
    val privacyActivityTitle: String
    val privacyActivityDesc: String
    val privacyAvatarTitle: String
    val privacyAvatarDesc: String
    val privacyForwardedTitle: String
    val privacyForwardedDesc: String
    val privacyMessagesTitle: String
    val privacyMessagesDesc: String
    val privacyStatusTitle: String
    val privacyStatusDesc: String

    // Report dialog
    val reportTitle: String
    val reportSubtitle: String
    val reportStepLabel: (Int, Int) -> String
    val reportReasonSpam: String
    val reportReasonSpamDesc: String
    val reportReasonFraud: String
    val reportReasonFraudDesc: String
    val reportReasonDrugs: String
    val reportReasonDrugsDesc: String
    val reportReasonWeapons: String
    val reportReasonWeaponsDesc: String
    val reportReasonPorn: String
    val reportReasonPornDesc: String
    val reportReasonCsam: String
    val reportReasonCsamDesc: String
    val reportReasonViolence: String
    val reportReasonViolenceDesc: String
    val reportReasonHarassment: String
    val reportReasonHarassmentDesc: String
    val reportReasonHate: String
    val reportReasonHateDesc: String
    val reportReasonFakeAccount: String
    val reportReasonFakeAccountDesc: String
    val reportReasonMisinfo: String
    val reportReasonMisinfoDesc: String
    val reportCriticalNotice: String
    val reportDetailsTitle: String
    val reportDetailsHint: String
    val reportCommentPlaceholder: String
    val reportCommentCounter: (Int, Int) -> String
    val reportChangeReason: String
    val reportSubmitBtn: String
    val reportCancelBtn: String
    val reportSentTitle: String
    val reportSentDesc: String
    val reportDoneBtn: String
    val a11yReportClose: String

    // Privacy exceptions picker
    val privacyExceptionsTitle: String
    val privacyExceptionsHint: String
    val privacyExceptionsSelected: (Int) -> String
    val privacyExceptionsSelectAll: String
    val privacyExceptionsClearAll: String
    val privacyExceptionsEmptyTitle: String
    val privacyExceptionsEmptyDesc: String
    val a11yExceptionToggle: (String) -> String
    val a11yExceptionRemove: (String) -> String


    // Settings sections
    val settingsPrivacy: String
    val settingsAccount: String
    val settingsDevices: String
    val settingsPasscode: String


    // Profile screen extras
    val usernameCopied: String
    val addAvatar: String
    val choosePhoto: String


    // Media types
    val typeVideo: String
    val typeVideoMessage: String
    val typeAudio: String
    val typeFile: String
    val typeVoice: String


    // Media player
    val playerPlaylist: String
    val playerSearchTracks: String
    val playerNowPlaying: String
    val playerTrackFallback: String
    val playerTracksCount: (Int) -> String
    val playerQueueEmpty: String
    val playerQueueEmptyHint: String
    val playerSearchEmptyTitle: String
    val playerSearchEmptySubtitle: (String) -> String
    val playerBuffering: String
    val playerTimeZero: String
    val playerTimeUnknown: String
    val playerSpeedFormat: (String) -> String
    val playerRepeatOff: String
    val playerRepeatAll: String
    val playerRepeatOne: String
    val a11yPlayerPlay: String
    val a11yPlayerPause: String
    val a11yPlayerNext: String
    val a11yPlayerPrevious: String
    val a11yPlayerRewind10: String
    val a11yPlayerForward10: String
    val a11yPlayerShuffle: String
    val a11yPlayerClose: String
    val a11yPlayerExpand: String
    val a11yPlayerCollapse: String
    val a11yPlayerSearch: String
    val a11yPlayerSearchClose: String
    val a11yPlayerClearSearch: String
    val a11yPlayerArtwork: String
    val a11yPlayerSpeed: (String) -> String
    val a11yPlayerTrackRow: (String) -> String


    // Profile media sections
    val sectionPhotosVideos: String
    val sectionFiles: String
    val sectionMusic: String
    val sectionVoice: String
    val profileSearchFiles: String


    // Downloads
    val actionDownload: String
    val actionDownloadSelected: String


    // Auth screen
    val switchSignIn: String
    val switchSignUp: String
    val authTabSignUp: String
    val authTabSignIn: String
    val authChecking: String
    val authEmailAvailable: String
    val authEmailTaken: String
    val authUsernameTakenShort: String
    val authUsernameAvailable: (String) -> String
    val authRegisterFailed: String
    val authLoginFailed: String
    val authUsernameCounter: (Int, Int) -> String
    val languageName: String


    // Blocked users screen
    val blockedTitle: String
    val blockedSearchPlaceholder: String
    val blockedClearSearch: String
    val blockedEmptyTitle: String
    val blockedEmptyDesc: String
    val blockedSearchEmptyTitle: String
    val blockedSearchEmptyDesc: (String) -> String
    val blockedUnblockBtn: String
    val blockedUnblockConfirmTitle: String
    val blockedUnblockConfirmText: (String) -> String
    val blockedUnblockedToast: String
    val blockedUserFallback: (Int) -> String
    val accountDeleted: String
    val accountFrozen: String
    val accountBannedMessage: String
    val accountFrozenMessage: String


    // Text formatting
    val formatCopy: String
    val formatCut: String
    val formatFormat: String
    val formatBold: String
    val formatItalic: String
    val formatBoldItalic: String
    val formatStrikethrough: String
    val formatUnderline: String
    val formatMonospace: String
    val formatLink: String
    val formatTextColor: String
    val formatSpoiler: String
    val formatQuote: String
    val formatLinkUrlHint: String
    val formatColorHint: String
    val formatPreview: String
    val formatCopied: String
    val formatReadMore: String
    val formatCollapse: String


    // Attachment menu
    val attachPhotoVideo: String
    val attachFile: String
    val attachTitle: String


    // Photo viewer
    val photoViewer: String
    val photoOf: (Int, Int) -> String


    // Message context menu
    val actionCopy: String
    val actionReport: String


    // Chat header (toolbar, search, selection, pinned banner)
    val chatSearchPlaceholder: String
    val chatSearchNoResults: String
    val chatSearchCounter: (Int, Int) -> String
    val chatSearchClose: String
    val chatSearchClear: String
    val chatSearchByDate: String
    val chatSearchNext: String
    val chatSearchPrev: String
    val chatActionSearch: String
    val chatMenu: String
    val chatAvatar: String
    val chatOpenProfile: String
    val chatClearSelection: String
    val chatStatusBlockedByMe: String
    val chatBlockUser: String
    val chatUnblockUser: String
    val chatUserBlockedToast: String
    val chatPinnedCounter: (Int, Int) -> String
    val chatJumpToPinned: String
    val chatUnpinAllHint: String


    // Chat input bar (composer, attachments, voice recording)
    val inputAttachMedia: String
    val inputAttachGallery: String
    val inputAttachFile: String
    val inputEmojiPanel: String
    val inputSelectedMedia: (Int) -> String
    val inputClearAttachments: String
    val inputBlockedByMe: String
    val inputAttachmentPreview: String
    val inputCancelReply: String
    val voiceHoldToRecord: String
    val voiceSendRecording: String
    val voiceCancelRecording: String
    val voiceLocked: String
    val voiceSlideToCancel: String
    val voiceSlideToLock: String
    val voiceRecordStartFailed: String
    val voicePermissionRequired: String
    val voiceTooShort: String

    // ПУНКТ 2 — кружки (видеосообщения)
    val circleModeSwitchedOn: String
    val circleModeSwitchedOff: String
    val circleRecordVideoMessage: String
    val circleHoldOrTapHint: String
    val circleRecordingHint: String
    val circleReleaseToCancel: String
    val circleLockedHint: String
    val circleTapToStop: String
    val circleCameraPreparing: String
    val circleMaxDurationHint: String
    val circlePermissionRequired: String
    val circleTooShort: String
    val circleCancel: String
    val circleSend: String
    val circleSwitchCamera: String
    val circleSendFailed: String


    // Chat list (iOS redesign)
    val chatsSectionPinned: String
    val filterAll: String
    val filterUnread: String
    val filterUnreadCount: (Int) -> String
    val chatsNoUnreadTitle: String
    val chatsNoUnreadSubtitle: String
    val chatsSectionAll: String
    val chatsCountFooter: (Int) -> String
    val chatsEmptyHint: String
    val chatsSearchCancel: String
    val chatsSearchClearField: String
    val chatsSearchNoResultsTitle: String
    val chatsSearchNoResultsSubtitle: (String) -> String
    val userFallback: (Int) -> String
    val someoneLabel: String
    val a11yMutedChat: String
    val a11yPinnedChat: String
    val actionMuteShort: String
    val actionUnmuteShort: String


    // Chat list message previews
    val typePhoto: String
    val previewVoiceMessage: (String) -> String
    val previewVideoMessage: (String) -> String
    val previewAudioTrack: (String, String) -> String
    val previewAudioLoading: String
    val previewMorePhotos: (Int) -> String
    val previewMoreVideos: (Int) -> String
    val previewMoreWithCaption: (Int, String) -> String
    val typeSticker: String
    val typeGif: String
    val previewMediaCount: (Int) -> String
    val previewMoreAudio: (Int) -> String
    val previewMoreFiles: (Int) -> String
    val previewMoreAttachments: (Int) -> String
    val actionSelectMessage: String

    // Chat message list (empty state, system messages, scroll-to-bottom)
    val emptyChatSubtitle: String
    val emptyChatHint: String
    val chatSystemMessageLabel: String
    val linkOpenFailed: String
    val a11yMessageList: String
    val a11yScrollToBottom: String
    val a11yUnreadCount: (Int) -> String
    val unreadCountOverflow: String

    // Chat dialogs, reactions sheet, bot & network toasts
    val okBtn: String
    val actionClose: String
    val reportSentToast: String
    val restrictionTitle: String
    val restrictionUnderstood: String
    val restrictionWhy: String
    val datePickerTitle: String
    val dateJumpNotFound: String
    val botMessageTitle: String
    val botLabel: String
    val attachmentLabel: String
    val fileSizeLoading: String
    val reactionsTitle: String
    val reactionsAllTab: (Int) -> String
    val reactionsEmpty: String
    val voiceTrackTitleMine: String
    val botCallbackTimeout: String
    val connectionLostToast: String
    val fileOpenFailed: String
    val maxPinnedChatsToast: (Int) -> String

    // Chat toast host
    val toastTitleInfo: String
    val toastTitleSuccess: String
    val toastTitleWarning: String
    val toastTitleError: String
    val toastActionRetry: String
    val toastActionUndo: String
    val toastCopied: String
    val a11yToast: (String) -> String
    val a11yToastDismiss: String

    // Devices & sessions
    val devicesTitle: String
    val devicesSubtitle: String
    val devicesSessionsCount: (Int) -> String
    val devicesRefreshCd: String
    val devicesSectionCurrent: String
    val devicesSectionOther: String
    val devicesCurrentBadge: String
    val devicesOnlineNow: String
    val devicesLastActiveNow: String
    val devicesLastActiveMinutes: (Int) -> String
    val devicesLastActiveHours: (Int) -> String
    val devicesLastActiveYesterday: String
    val devicesLastActiveDate: (String) -> String
    val devicesDateTimePattern: String
    val devicesUnknownDevice: String
    val devicesUnknownLocation: String
    val devicesNoOtherSessions: String
    val devicesNoOtherSessionsHint: String
    val devicesEmptyTitle: String
    val devicesEmptySubtitle: String
    val devicesLoading: String
    val devicesLoadFailedTitle: String
    val devicesLoadFailedSubtitle: String
    val devicesTerminateCd: String
    val devicesTerminateAll: String
    val devicesTerminateTitle: String
    val devicesTerminateText: (String) -> String
    val devicesTerminateAllTitle: String
    val devicesTerminateAllText: (Int) -> String
    val devicesTerminateConfirm: String
    val devicesSecurityHint: String

    // Edit profile field
    val editFieldSave: String
    val editFieldSaveCd: String
    val editFieldPlaceholder: (String) -> String
    val editFieldClearCd: String
    val editFieldCounter: (Int, Int) -> String
    val editFieldLimitReached: String
    val editFieldUnsavedTitle: String
    val editFieldUnsavedText: String
    val editFieldUnsavedDiscard: String

    // Main container navigation (a11y)
    val a11yTab: (String) -> String

    // Profile screen (redesign)
    val profileSectionInfo: String
    val profileSectionAppearance: String
    val profileSectionSession: String
    val profileCopyUsername: String
    val profileCopyUsernameHint: String
    val a11yAvatar: String
    val a11yEditAvatar: String
    val a11yAvatarPreview: String
    val a11yChoosePhoto: String
    val avatarCropHint: String
    val avatarPickPrompt: String

    // Profile screen (extra)
    val profileNotFoundTitle: String
    val profileNotFoundDesc: (String) -> String
    val profileLoading: String
    val profileWriteBtn: String
    val profileBlockedTitle: String
    val profileBlockedDesc: String
    val a11yProfileMenu: String
    val a11yAvatarViewerClose: String

    // Compact number units
    val unitCompactFormat: (String, String) -> String
    val unitThousandShort: String
    val unitMillionShort: String
    val unitBillionShort: String

    // Settings root list
    val settingsChats: String
    val settingsChatsSubtitle: String
    val settingsPrivacySubtitle: String
    val settingsNotifications: String
    val settingsNotificationsSubtitle: String
    val settingsPowerSaving: String
    val settingsPowerSavingSubtitle: String
    val settingsDevicesSubtitle: String
    val settingsLanguageSubtitle: String
    val settingsAccountSubtitle: String
    val settingsSupport: String
    val settingsSupportSubtitle: String
    val settingsVibePro: String
    val settingsVibeProSubtitle: String
    val settingsVibeProCta: String
    // Vibe Pro screen
    val vibeProHeroDescription: String
    val vibeProSectionFeatures: String
    val vibeProSectionPlans: String
    val vibeProFeatureLimitsTitle: String
    val vibeProFeatureLimitsSubtitle: String
    val vibeProFeatureVoiceToTextTitle: String
    val vibeProFeatureVoiceToTextSubtitle: String
    val vibeProFeatureReactionsTitle: String
    val vibeProFeatureReactionsSubtitle: String
    val vibeProFeatureBadgeTitle: String
    val vibeProFeatureBadgeSubtitle: String
    val vibeProFeatureSpeedTitle: String
    val vibeProFeatureSpeedSubtitle: String
    val vibeProFeatureNoAdsTitle: String
    val vibeProFeatureNoAdsSubtitle: String
    val vibeProPlanYearly: String
    val vibeProPlanYearlyPrice: String
    val vibeProPlanYearlyDiscount: String
    val vibeProPlanMonthly: String
    val vibeProPlanMonthlyPrice: String
    val vibeProSubscribeCta: (String) -> String
    val vibeProAutoRenewalDisclaimer: String
    val vibeProComingSoonToast: String
    // Two-factor authentication (2FA)
    val twoFactorTitle: String
    val twoFactorSubtitle: String
    val twoFactorDescription: String
    val twoFactorStatusEnabled: String
    val twoFactorStatusDisabled: String
    val twoFactorEnabledBadge: String
    val twoFactorEnabledDesc: String
    val twoFactorBullet1Title: String
    val twoFactorBullet1Desc: String
    val twoFactorBullet2Title: String
    val twoFactorBullet2Desc: String
    val twoFactorBullet3Title: String
    val twoFactorBullet3Desc: String
    val twoFactorSetPasswordBtn: String
    val twoFactorChangePasswordBtn: String
    val twoFactorChangeHintBtn: String
    val twoFactorDisableBtn: String
    val twoFactorEnterNewPasswordTitle: String
    val twoFactorEnterNewPasswordSubtitle: String
    val twoFactorRepeatPasswordTitle: String
    val twoFactorRepeatPasswordSubtitle: String
    val twoFactorEnterCurrentPasswordTitle: String
    val twoFactorEnterCurrentPasswordSubtitle: String
    val twoFactorHintTitle: String
    val twoFactorHintSubtitle: String
    val twoFactorHintPlaceholder: String
    val twoFactorHintTooLong: String
    val twoFactorHintContainsPassword: String
    val twoFactorHintPublicWarning: String
    val twoFactorPasswordTooShort: String
    val twoFactorPasswordMismatch: String
    val twoFactorPasswordWrong: String
    val twoFactorDisableConfirmTitle: String
    val twoFactorDisableConfirmDesc: String
    val twoFactorDisableAction: String
    val twoFactorNextBtn: String
    val twoFactorSkipBtn: String
    val twoFactorSaveBtn: String
    val twoFactorStrengthWeak: String
    val twoFactorStrengthMedium: String
    val twoFactorStrengthStrong: String
    val twoFactorStrengthVeryStrong: String
    val twoFactorCurrentHintPill: (String) -> String
    val twoFactorSuccessSetToast: String
    val twoFactorSuccessChangedToast: String
    val twoFactorSuccessDisabledToast: String
    val twoFactorPasswordFieldLabel: String
    val twoFactorConfirmFieldLabel: String
    val twoFactorCurrentFieldLabel: String
    val twoFactorHintFieldLabel: String
    val settingsVibes: String
    val settingsVibesSubtitle: String
    val settingsGroupGeneral: String
    val settingsGroupExtras: String
    val settingsGroupHelp: String
    val settingsSoonBadge: String
    val appVersion: (String) -> String

    // Onboarding controls
    val onboardingGetStarted: String
    val onboardingSkip: String
    val a11yOnboardingPage: (Int, Int) -> String

    // Passcode
    val passcodeEnterTitle: String
    val passcodeEnterSubtitle: String
    val passcodeEnterCurrentTitle: String
    val passcodeCreateTitle: String
    val passcodeRepeatTitle: String
    val passcodeInfoTitle: String
    val passcodeInfoText: String
    val passcodeEnableBtn: String
    val passcodeChangeBtn: String
    val passcodeDisableBtn: String
    val passcodeDisableShort: String
    val passcodeRemoveTitle: String
    val passcodeRemoveText: String
    val passcodeWrongCode: String
    val passcodeMismatch: String
    val a11yPasscodeLock: String
    val a11yPasscodeBackspace: String
    val a11yPasscodeDigit: (String) -> String

    // Nickname screen
    val nicknameHint: String

    // Shared UI components (button, text field, OTP, toast, inline keyboard)
    val a11yLoading: String
    val a11yOtpInput: String
    val a11yOtpDigit: (Int, Int) -> String
    val a11yOtpDigitEmpty: (Int, Int) -> String
    val a11yFieldError: (String) -> String
    val a11yClearField: String
    val a11yInlineButtonLink: String
    val a11yInlineButtonLoading: String

    // Link confirmation dialog & inline formatting (a11y)
    val linkDialogTitle: String
    val linkDialogSubtitle: String
    val linkDialogSecure: String
    val linkDialogInsecure: String
    val linkDialogOpen: String
    val linkDialogCancel: String
    val a11yLinkChip: (String) -> String
    val a11ySpoilerHidden: String
    val a11ySpoilerRevealed: String
    val a11yQuote: String
    val formatInlineQuoteWrap: (String) -> String

    val locale: String
}

object RuStrings : VibeStrings {
    override val createAccount: String = "Создание аккаунта"
    override val welcomeBack: String = "С возвращением"
    override val emailLabel: String = "ПОЧТА"
    override val usernameLabel: String = "ЮЗЕРНЕЙМ"
    override val emailInvalidFormat: String = "НЕВЕРНЫЙ ФОРМАТ"
    override val continueBtn: String = "ПРОДОЛЖИТЬ"
    override val verificationTitle: String = "Введите код"
    override val verificationSubtitle: (String) -> String = { email -> "Мы отправили 6-значный код на вашу почту\n$email" }
    override val verifyBtn: String = "ПОДТВЕРДИТЬ"
    override val verifyLoading: String = "ПРОВЕРКА..."
    override val codeInvalid: String = "Неверный код"
    override val nicknameTitle: String = "Как вас зовут?"
    override val nicknameLabel: String = "ВАШ НИКНЕЙМ"
    override val saveBtn: String = "ПРОДОЛЖИТЬ"
    override val saveLoading: String = "СОХРАНЕНИЕ..."
    override val errorSaving: String = "Ошибка при сохранении"
    override val tabChats: String = "Чаты"
    override val tabSettings: String = "Настройки"
    override val tabProfile: String = "Профиль"
    override val profileTitle: String = "Профиль"
    override val deletedAcc: String = "Удаленный аккаунт"
    override val statusOnline: String = "В сети"
    override val statusBot: String = "Бот"
    override val statusUnknown: String = "Неизвестно"
    override val actionForward: String = "Переслать"
    override val badgeVerified: String = "Пользователь верифицирован командой Vibe."
    override val badgeDeveloper: String = "Член команды разработчиков Vibe."
    override val badgeBot: String = "Просто бот."
    override val freezedAcc: String = "Аккаунт заморожен за нарушение правил."
    override val bannedAcc: String = "Аккаунт заблокирован за нарушение правил."
    override val aboutLabel: String = "Описание"
    override val registerDateLabel: String = "Дата регистрации"
    override val btnTheme: String = "Тема"
    override val themeDark: String = "Темная"
    override val themeLight: String = "Светлая"
    override val btnLanguage: String = "Язык"
    override val btnLogout: String = "Выйти из аккаунта"
    override val logoutConfirmTitle: String = "Выход из аккаунта"
    override val logoutConfirmText: String = "Вы уверены, что хотите выйти из аккаунта?"
    override val logoutCancel: String = "Отмена"
    override val logoutConfirm: String = "Выйти"
    override val userLabel: String = "Пользователь"
    override val chatsTitle: String = "Чаты"
    override val chatsEmptyTitle: String = "Нет чатов"
    override val chatsEmptySubtitle: String = "Начните переписку!"
    override val searchPlaceholder: String = "Поиск..."
    override val globalSearchResults: String = "Глобальный поиск"
    override val typing: String = "Печатает"
    override val connecting: String = "Соединение"
    override val waitingForNetwork: String = "Ожидание сети"
    override val monthsShort: List<String> = listOf("янв", "фев", "мар", "апр", "мая", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
    override val dateToday: String = "Сегодня"
    override val dateYesterday: String = "Вчера"
    override val lastSeenRecently: String = "Был(а) недавно"
    override val lastSeenLongAgo: String = "Был(а) очень давно"
    override val lastSeenInWeek: String = "Был(а) на этой неделе"
    override val lastSeenInMonth: String = "Был(а) в этом месяце"
    override val lastSeenToday: (String) -> String = { time -> "Был(а) сегодня в $time" }
    override val lastSeenYesterday: (String) -> String = { time -> "Был(а) вчера в $time" }
    override val lastSeenDate: (String, String) -> String = { date, time -> "Был(а) $date в $time" }
    override val backBtn: String = "Назад"
    override val draftLabel: String = "Черновик: "
    override val messagePlaceholder: String = "Сообщение..."
    override val replyTo: String = "Ответить"
    override val emptyChat: String = "Здесь пока пусто..."
    override val chatHistoryCleared: String = "История очищена"
    override val chatHistoryEmpty: String = "История пуста"
    override val onboardingPages: List<Pair<String, String>> = listOf(
        "Безопасность данных" to "Ваше общение защищено шифрованием военного уровня.",
        "Скорость света" to "Протоколы нового поколения для мгновенной доставки сообщений.",
        "Универсальная синхронизация" to "Вся ваша история данных доступна на каждом устройстве.",
        "Глобальные сообщества" to "Масштабируйте сообщества до миллионов активных участников.",
        "Уникальный Вайб" to "Настройте каждый аспект вашего мессенджера под себя."
    )
    override val selectedMessagesCount: (Int) -> String = { count -> "Выбрано: $count" }
    override val deleteMessagesTitle: String = "Удалить сообщения?"
    override val deleteMessagesText: (Int) -> String = { count -> "Вы собираетесь удалить $count сообщений." }
    override val deleteForEveryone: (String) -> String = { name -> "Также удалить для $name" }
    override val deleteForEveryoneAlsoMine: (String) -> String = { name -> "Также удалить свои сообщения для $name" }
    override val deleteBtn: String = "Удалить"
    override val cancelBtn: String = "Отмена"
    override val forwardMessageTitle: String = "Переслать сообщение"
    override val noRecentChats: String = "Нет недавних чатов"
    override val editMessageTitle: String = "Редактирование сообщения"
    override val sendBtn: String = "Отправить"
    override val forwardedFrom: (String) -> String = { name -> "Переслано от $name" }
    override val replyDefault: String = "Ответ"
    override val pinnedMessage: String = "Закрепленное сообщение"
    override val pinnedMessages: String = "Закрепленные сообщения"
    override val pinMessage: String = "Закрепить сообщение"
    override val pinMessageConfirm: String = "Вы действительно хотите закрепить это сообщение?"
    override val unpinMessage: String = "Открепить сообщение"
    override val unpinMessageConfirm: String = "Вы действительно хотите открепить это сообщение?"
    override val unpinAll: String = "Открепить все"
    override val unpinAllConfirm: String = "Открепить все сообщения в этом чате?"
    override val forBoth: (String) -> String = { name -> "Также для $name" }
    override val pin: String = "Закрепить"
    override val unpin: String = "Открепить"
    override val you: String = "Вы"
    override val edit: String = "Изменить"
    override val usernameTaken: String = "Этот юзернейм уже занят"
    override val usernameAvailable: String = "Юзернейм свободен"
    override val usernameDescription: String = "Вы можете выбрать уникальное имя пользователя в Vibe. Если вы это сделаете, другие люди смогут найти вас по этому имени и связаться с вами, не зная вашего номера телефона."
    override val usernameMinLength: String = "Минимальная длина 4 символа"
    override val nicknameDescription: String = "Ваше имя, которое будет отображаться всем пользователям. Постарайтесь выбрать узнаваемое имя, чтобы друзья могли легко вас найти."
    override val bioDescription: String = "Напишите немного о себе. Эта информация будет видна другим пользователям в вашем профиле."
    override val doneBtn: String = "Готово"
    override val userRestrictedMessaging: String = "Пользователь ограничил круг общения"
    override val userHiddenAccount: String = "Пользователь скрыл аккаунт"
    override val editedLabel: String = " (изменено)"
    override val muteNotifications: String = "Выключить уведомления"
    override val unmuteNotifications: String = "Включить уведомления"
    override val pinnedMessageSystemText: (String, String) -> String = { sender, content -> "$sender закрепил(а) сообщение: \"$content\"" }
    override val privacyScreenTitle: String = "Конфиденциальность"
    override val privacyTwoFactor: String = "Двойная аутентификация"
    override val privacyPasscodeLogin: String = "Вход по коду"
    override val privacyBlocked: String = "Заблокированные"
    override val privacyActivityTitle: String = "Статус активности"
    override val privacyActivityDesc: String = "Кто может видеть, когда вы в последний раз были в сети. Если вы скроете свой статус активности, вы не сможете видеть статус других пользователей (будет отображаться примерное время)."
    override val privacyAvatarTitle: String = "Аватарка"
    override val privacyAvatarDesc: String = "Кто может видеть вашу аватарку. Для остальных будет отображаться первая буква вашего имени на синем фоне."
    override val privacyForwardedTitle: String = "Пересланные сообщения"
    override val privacyForwardedDesc: String = "Кто может переходить на ваш профиль из пересланных сообщений."
    override val privacyMessagesTitle: String = "Сообщения"
    override val privacyMessagesDesc: String = "Кто может отправлять вам сообщения. Пользователи, которым это запрещено, увидят надпись «Пользователь ограничил круг общения»."
    override val privacyStatusTitle: String = "Статус"
    override val privacyStatusDesc: String = "Кто может видеть блок «О себе» в вашем профиле."

    // Report dialog
    override val reportTitle: String = "Пожаловаться"
    override val reportSubtitle: String = "Выберите причину. Жалоба анонимна, автор её не увидит."
    override val reportStepLabel: (Int, Int) -> String = { current, total -> "Шаг $current из $total" }
    override val reportReasonSpam: String = "Спам"
    override val reportReasonSpamDesc: String = "Реклама, рассылки, накрутка"
    override val reportReasonFraud: String = "Мошенничество"
    override val reportReasonFraudDesc: String = "Обман, фишинг, схемы с деньгами"
    override val reportReasonDrugs: String = "Наркотики"
    override val reportReasonDrugsDesc: String = "Продажа или реклама запрещённых веществ"
    override val reportReasonWeapons: String = "Оружие"
    override val reportReasonWeaponsDesc: String = "Торговля оружием и взрывчаткой"
    override val reportReasonPorn: String = "Порнография"
    override val reportReasonPornDesc: String = "Материалы для взрослых без предупреждения"
    override val reportReasonCsam: String = "Материалы с детьми (CSAM)"
    override val reportReasonCsamDesc: String = "Сексуализированный контент с несовершеннолетними"
    override val reportReasonViolence: String = "Насилие"
    override val reportReasonViolenceDesc: String = "Угрозы, жестокость, призывы к вреду"
    override val reportReasonHarassment: String = "Травля"
    override val reportReasonHarassmentDesc: String = "Оскорбления, преследование, шантаж"
    override val reportReasonHate: String = "Разжигание ненависти"
    override val reportReasonHateDesc: String = "Нападки по признаку расы, религии, пола"
    override val reportReasonFakeAccount: String = "Фейковый аккаунт"
    override val reportReasonFakeAccountDesc: String = "Выдаёт себя за другого человека"
    override val reportReasonMisinfo: String = "Ложная информация"
    override val reportReasonMisinfoDesc: String = "Опасные слухи и дезинформация"
    override val reportCriticalNotice: String = "Такие жалобы мы рассматриваем в приоритетном порядке и передаём в профильные органы."
    override val reportDetailsTitle: String = "Расскажите подробнее"
    override val reportDetailsHint: String = "Опишите ситуацию: что произошло и где. Это поможет нам принять меры быстрее."
    override val reportCommentPlaceholder: String = "Комментарий (необязательно)"
    override val reportCommentCounter: (Int, Int) -> String = { used, limit -> "$used / $limit" }
    override val reportChangeReason: String = "Другая причина"
    override val reportSubmitBtn: String = "Отправить жалобу"
    override val reportCancelBtn: String = "Отмена"
    override val reportSentTitle: String = "Жалоба отправлена"
    override val reportSentDesc: String = "Спасибо. Модераторы изучат обращение и примут решение."
    override val reportDoneBtn: String = "Готово"
    override val a11yReportClose: String = "Закрыть"

    // Privacy exceptions picker
    override val privacyExceptionsTitle: String = "Исключения"
    override val privacyExceptionsHint: String = "Выберите, на кого правило не распространяется"
    override val privacyExceptionsSelected: (Int) -> String = { count -> "Выбрано: $count" }
    override val privacyExceptionsSelectAll: String = "Выбрать всех"
    override val privacyExceptionsClearAll: String = "Снять выбор"
    override val privacyExceptionsEmptyTitle: String = "Некого выбрать"
    override val privacyExceptionsEmptyDesc: String = "Здесь появятся люди, с которыми у вас есть чаты."
    override val a11yExceptionToggle: (String) -> String = { name -> "Выбрать $name" }
    override val a11yExceptionRemove: (String) -> String = { name -> "Убрать $name из выбранных" }
    override val settingsPrivacy: String = "Приватность"
    override val settingsAccount: String = "Аккаунт"
    override val settingsDevices: String = "Устройства"
    override val settingsPasscode: String = "Код-пароль"
    override val usernameCopied: String = "Юзернейм скопирован"
    override val addAvatar: String = "Добавить аватарку"
    override val choosePhoto: String = "Выбрать фото"
    override val switchSignIn: String = "Уже есть аккаунт? Войти"
    override val switchSignUp: String = "Нет аккаунта? Создать"
    override val authTabSignUp: String = "Регистрация"
    override val authTabSignIn: String = "Вход"
    override val authChecking: String = "Проверяем..."
    override val authEmailAvailable: String = "Почта свободна"
    override val authEmailTaken: String = "ЭТА ПОЧТА ЗАНЯТА"
    override val authUsernameTakenShort: String = "ЭТОТ ЮЗЕРНЕЙМ ЗАНЯТ"
    override val authUsernameAvailable: (String) -> String = { username -> "@$username свободен" }
    override val authRegisterFailed: String = "Не удалось создать аккаунт"
    override val authLoginFailed: String = "Не удалось войти"
    override val authUsernameCounter: (Int, Int) -> String = { current, max -> "$current/$max" }
    override val languageName: String = "Русский"
    override val blockedTitle: String = "Заблокированные"
    override val blockedSearchPlaceholder: String = "Поиск пользователей..."
    override val blockedClearSearch: String = "Очистить поиск"
    override val blockedEmptyTitle: String = "Нет заблокированных"
    override val blockedEmptyDesc: String = "Здесь будут отображаться пользователи, которых вы заблокировали."
    override val blockedSearchEmptyTitle: String = "Ничего не найдено"
    override val blockedSearchEmptyDesc: (String) -> String = { query -> "По запросу «$query» пользователей не найдено" }
    override val blockedUnblockBtn: String = "Разблокировать"
    override val blockedUnblockConfirmTitle: String = "Разблокировать?"
    override val blockedUnblockConfirmText: (String) -> String = { name -> "$name снова сможет писать вам и видеть ваш профиль." }
    override val blockedUnblockedToast: String = "Пользователь разблокирован"
    override val blockedUserFallback: (Int) -> String = { id -> "Пользователь #$id" }
    override val accountDeleted: String = "Удаленный аккаунт"
    override val accountFrozen: String = "Замороженный аккаунт"
    override val accountBannedMessage: String = "Ваш аккаунт был заблокирован за нарушение правил."
    override val accountFrozenMessage: String = "Ваш аккаунт был заморожен модератором."
    override val typeVideo: String = "Видео"
    override val typeVideoMessage: String = "Видеосообщение"
    override val typeAudio: String = "Аудиофайл"
    override val typeFile: String = "Файл"
    override val typeVoice: String = "Голосовое сообщение"
    override val playerPlaylist: String = "Плейлист чата"
    override val playerSearchTracks: String = "Поиск треков"
    override val playerNowPlaying: String = "Сейчас играет"
    override val playerTrackFallback: String = "Аудиозапись"
    override val playerTracksCount: (Int) -> String = { count ->
        val word = when {
            count % 10 == 1 && count % 100 != 11 -> "трек"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "трека"
            else -> "треков"
        }
        "$count $word"
    }
    override val playerQueueEmpty: String = "Очередь пуста"
    override val playerQueueEmptyHint: String = "Аудио из этого чата появится здесь"
    override val playerSearchEmptyTitle: String = "Ничего не найдено"
    override val playerSearchEmptySubtitle: (String) -> String = { query -> "По запросу «$query» треков нет" }
    override val playerBuffering: String = "Буферизация…"
    override val playerTimeZero: String = "0:00"
    override val playerTimeUnknown: String = "--:--"
    override val playerSpeedFormat: (String) -> String = { value -> "$value×" }
    override val playerRepeatOff: String = "Повтор выключен"
    override val playerRepeatAll: String = "Повторять плейлист"
    override val playerRepeatOne: String = "Повторять трек"
    override val a11yPlayerPlay: String = "Воспроизвести"
    override val a11yPlayerPause: String = "Пауза"
    override val a11yPlayerNext: String = "Следующий трек"
    override val a11yPlayerPrevious: String = "Предыдущий трек"
    override val a11yPlayerRewind10: String = "Назад на 10 секунд"
    override val a11yPlayerForward10: String = "Вперёд на 10 секунд"
    override val a11yPlayerShuffle: String = "Перемешать"
    override val a11yPlayerClose: String = "Закрыть плеер"
    override val a11yPlayerExpand: String = "Развернуть плеер"
    override val a11yPlayerCollapse: String = "Свернуть плеер"
    override val a11yPlayerSearch: String = "Поиск по плейлисту"
    override val a11yPlayerSearchClose: String = "Закрыть поиск"
    override val a11yPlayerClearSearch: String = "Очистить поиск"
    override val a11yPlayerArtwork: String = "Обложка трека"
    override val a11yPlayerSpeed: (String) -> String = { value -> "Скорость воспроизведения: $value" }
    override val a11yPlayerTrackRow: (String) -> String = { title -> "Включить трек $title" }
    override val sectionPhotosVideos: String = "Фото и видео"
    override val sectionFiles: String = "Файлы"
    override val sectionMusic: String = "Музыка"
    override val sectionVoice: String = "Голосовые"
    override val profileSearchFiles: String = "Поиск файлов"
    override val actionDownload: String = "Скачать"
    override val actionDownloadSelected: String = "Скачать выбранные"
    override val formatCopy: String = "Скопировать"
    override val formatCut: String = "Вырезать"
    override val formatFormat: String = "Форматировать"
    override val formatBold: String = "Жирный"
    override val formatItalic: String = "Курсив"
    override val formatBoldItalic: String = "Жирный курсив"
    override val formatStrikethrough: String = "Зачеркнутый"
    override val formatUnderline: String = "Подчеркнутый"
    override val formatMonospace: String = "Моноширинный"
    override val formatLink: String = "Ссылка"
    override val formatTextColor: String = "Цвет текста"
    override val formatSpoiler: String = "Спойлер"
    override val formatQuote: String = "Цитата"
    override val formatLinkUrlHint: String = "Введите URL"
    override val formatColorHint: String = "Введите HEX (напр. #FF5733)"
    override val formatPreview: String = "Предпросмотр"
    override val formatCopied: String = "Скопировано"
    override val formatReadMore: String = "Читать далее"
    override val formatCollapse: String = "Свернуть"
    override val attachPhotoVideo: String = "Фото и видео"
    override val attachFile: String = "Файл"
    override val attachTitle: String = "Вложения"
    override val photoViewer: String = "Просмотр фото"
    override val photoOf: (Int, Int) -> String = { current, total -> "$current из $total" }
    override val actionCopy: String = "Скопировать текст"
    override val actionReport: String = "Пожаловаться"


    // Chat header
    override val chatSearchPlaceholder: String = "Поиск по сообщениям…"
    override val chatSearchNoResults: String = "Ничего не найдено"
    override val chatSearchCounter: (Int, Int) -> String = { current, total -> "$current из $total" }
    override val chatSearchClose: String = "Закрыть поиск"
    override val chatSearchClear: String = "Очистить запрос"
    override val chatSearchByDate: String = "Поиск по дате"
    override val chatSearchNext: String = "Следующий результат"
    override val chatSearchPrev: String = "Предыдущий результат"
    override val chatActionSearch: String = "Поиск"
    override val chatMenu: String = "Меню чата"
    override val chatAvatar: String = "Аватар"
    override val chatOpenProfile: String = "Открыть профиль"
    override val chatClearSelection: String = "Снять выделение"
    override val chatStatusBlockedByMe: String = "Заблокирован"
    override val chatBlockUser: String = "Заблокировать"
    override val chatUnblockUser: String = "Разблокировать"
    override val chatUserBlockedToast: String = "Пользователь заблокирован"
    override val chatPinnedCounter: (Int, Int) -> String = { current, total -> "$current/$total" }
    override val chatJumpToPinned: String = "Перейти к закреплённому сообщению"
    override val chatUnpinAllHint: String = "Открепить все"


    // Chat input bar
    override val inputAttachMedia: String = "Прикрепить"
    override val inputAttachGallery: String = "Фото или видео"
    override val inputAttachFile: String = "Файл"
    override val inputEmojiPanel: String = "Эмодзи, стикеры и GIF"
    override val inputSelectedMedia: (Int) -> String = { count -> "Выбрано медиа: $count" }
    override val inputClearAttachments: String = "Очистить вложения"
    override val inputBlockedByMe: String = "Вы заблокировали этого пользователя"
    override val inputAttachmentPreview: String = "Вложение"
    override val inputCancelReply: String = "Отменить"
    override val voiceHoldToRecord: String = "Удерживайте для записи"
    override val voiceSendRecording: String = "Отправить голосовое сообщение"
    override val voiceCancelRecording: String = "Отменить запись"
    override val voiceLocked: String = "Запись закреплена"
    override val voiceSlideToCancel: String = "Смахните влево, чтобы отменить"
    override val voiceSlideToLock: String = "Вверх — закрепить"
    override val voiceRecordStartFailed: String = "Не удалось начать запись голосового сообщения"
    override val voicePermissionRequired: String = "Для записи нужен доступ к микрофону"
    override val voiceTooShort: String = "Запись получилась слишком короткой"

    // ПУНКТ 2 — кружки (видеосообщения)
    override val circleModeSwitchedOn: String = "Режим кружков: удерживайте для записи"
    override val circleModeSwitchedOff: String = "Режим голосовых сообщений"
    override val circleRecordVideoMessage: String = "Записать видеосообщение"
    override val circleHoldOrTapHint: String = "Нажмите для записи, удерживайте для быстрой съёмки"
    override val circleRecordingHint: String = "Влево — отмена · вверх — закрепить"
    override val circleReleaseToCancel: String = "Отпустите для отмены"
    override val circleLockedHint: String = "Нажмите, чтобы отправить"
    override val circleTapToStop: String = "Нажмите, чтобы остановить и отправить"
    override val circleCameraPreparing: String = "Готовим камеру…"
    override val circleMaxDurationHint: String = "Максимум 60 секунд"
    override val circlePermissionRequired: String = "Для кружков нужен доступ к камере и микрофону"
    override val circleTooShort: String = "Кружок получился слишком коротким"
    override val circleCancel: String = "Отменить запись"
    override val circleSend: String = "Отправить кружок"
    override val circleSwitchCamera: String = "Сменить камеру"
    override val circleSendFailed: String = "Не удалось отправить кружок"


    // Chat list (iOS redesign)
    override val chatsSectionPinned: String = "Закреплённые"
    override val filterAll: String = "Все"
    override val filterUnread: String = "Непрочитанные"
    override val filterUnreadCount: (Int) -> String = { count -> "Непрочитанные ($count)" }
    override val chatsNoUnreadTitle: String = "Всё прочитано"
    override val chatsNoUnreadSubtitle: String = "Непрочитанных сообщений нет."
    override val chatsSectionAll: String = "Все чаты"
    override val chatsCountFooter: (Int) -> String = { count -> "Чатов: $count" }
    override val chatsEmptyHint: String = "Найдите пользователя через поиск выше"
    override val chatsSearchCancel: String = "Отмена"
    override val chatsSearchClearField: String = "Очистить поле поиска"
    override val chatsSearchNoResultsTitle: String = "Ничего не найдено"
    override val chatsSearchNoResultsSubtitle: (String) -> String = { query -> "Нет чатов и пользователей по запросу «$query»" }
    override val userFallback: (Int) -> String = { id -> "Пользователь #$id" }
    override val someoneLabel: String = "Кто-то"
    override val a11yMutedChat: String = "Уведомления выключены"
    override val a11yPinnedChat: String = "Чат закреплён"
    override val actionMuteShort: String = "Без звука"
    override val actionUnmuteShort: String = "Со звуком"


    // Chat list message previews
    override val typePhoto: String = "Фотография"
    override val previewVoiceMessage: (String) -> String = { duration -> "Голосовое сообщение $duration" }
    override val previewVideoMessage: (String) -> String = { duration -> "Видеосообщение $duration" }
    override val previewAudioTrack: (String, String) -> String = { artist, title -> "$artist — $title" }
    override val previewAudioLoading: String = "Музыка..."
    override val previewMorePhotos: (Int) -> String = { count -> "+$count фотографий" }
    override val previewMoreVideos: (Int) -> String = { count -> "+$count видео" }
    override val previewMoreWithCaption: (Int, String) -> String = { count, caption -> "+$count $caption" }
    override val typeSticker: String = "Стикер"
    override val typeGif: String = "GIF"
    override val previewMediaCount: (Int) -> String = { count ->
        val form = when {
            count % 10 == 1 && count % 100 != 11 -> "медиафайл"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "медиафайла"
            else -> "медиафайлов"
        }
        "$count $form"
    }
    override val previewMoreAudio: (Int) -> String = { count ->
        val form = when {
            count % 10 == 1 && count % 100 != 11 -> "аудиофайл"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "аудиофайла"
            else -> "аудиофайлов"
        }
        "$count $form"
    }
    override val previewMoreFiles: (Int) -> String = { count ->
        val form = when {
            count % 10 == 1 && count % 100 != 11 -> "файл"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "файла"
            else -> "файлов"
        }
        "$count $form"
    }
    override val previewMoreAttachments: (Int) -> String = { count ->
        val form = when {
            count % 10 == 1 && count % 100 != 11 -> "вложение"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "вложения"
            else -> "вложений"
        }
        "$count $form"
    }
    override val actionSelectMessage: String = "Выбрать"
    // Chat message list (empty state, system messages, scroll-to-bottom)
    override val emptyChatSubtitle: String = "Напишите первое сообщение, и история чата появится здесь."
    override val emptyChatHint: String = "Начните разговор первым"
    override val chatSystemMessageLabel: String = "Системное сообщение"
    override val linkOpenFailed: String = "Не удалось открыть ссылку"
    override val a11yMessageList: String = "Список сообщений"
    override val a11yScrollToBottom: String = "Перейти к последним сообщениям"
    override val a11yUnreadCount: (Int) -> String = { count -> "Новых сообщений: $count" }
    override val unreadCountOverflow: String = "99+"
    // Chat dialogs, reactions sheet, bot & network toasts
    override val okBtn: String = "OK"
    override val actionClose: String = "Закрыть"
    override val reportSentToast: String = "Жалоба успешно отправлена"
    override val restrictionTitle: String = "Ограничение"
    override val restrictionUnderstood: String = "Понятно"
    override val restrictionWhy: String = "Почему?"
    override val datePickerTitle: String = "Выберите дату"
    override val dateJumpNotFound: String = "Сообщений за эту дату не найдено"
    override val botMessageTitle: String = "Сообщение от бота"
    override val botLabel: String = "Бот"
    override val attachmentLabel: String = "Вложение"
    override val fileSizeLoading: String = "Загрузка..."
    override val reactionsTitle: String = "Реакции"
    override val reactionsAllTab: (Int) -> String = { count -> "Все $count" }
    override val reactionsEmpty: String = "Пока нет реакций"
    override val voiceTrackTitleMine: String = "Вы (Голосовое сообщение)"
    override val botCallbackTimeout: String = "Бот не ответил за 10 секунд. Кнопки этого сообщения снова доступны."
    override val connectionLostToast: String = "Нет соединения с сервером. Попробуйте ещё раз."
    override val fileOpenFailed: String = "Не удалось открыть выбранный файл"
    override val maxPinnedChatsToast: (Int) -> String = { limit -> "Можно закрепить не более $limit чатов" }
    // Chat toast host
    override val toastTitleInfo: String = "Информация"
    override val toastTitleSuccess: String = "Готово"
    override val toastTitleWarning: String = "Внимание"
    override val toastTitleError: String = "Ошибка"
    override val toastActionRetry: String = "Повторить"
    override val toastActionUndo: String = "Отменить"
    override val toastCopied: String = "Скопировано"
    override val a11yToast: (String) -> String = { text -> "Уведомление: $text" }
    override val a11yToastDismiss: String = "Скрыть уведомление"
    // Devices & sessions
    override val devicesTitle: String = "Устройства"
    override val devicesSubtitle: String = "Здесь показаны все входы в ваш аккаунт"
    override val devicesSessionsCount: (Int) -> String = { count ->
        val tail = count % 10
        val hundred = count % 100
        when {
            hundred in 11..14 -> "$count активных сеансов"
            tail == 1 -> "$count активный сеанс"
            tail in 2..4 -> "$count активных сеанса"
            else -> "$count активных сеансов"
        }
    }
    override val devicesRefreshCd: String = "Обновить список"
    override val devicesSectionCurrent: String = "ЭТО УСТРОЙСТВО"
    override val devicesSectionOther: String = "ДРУГИЕ СЕАНСЫ"
    override val devicesCurrentBadge: String = "Текущее"
    override val devicesOnlineNow: String = "В сети"
    override val devicesLastActiveNow: String = "Только что"
    override val devicesLastActiveMinutes: (Int) -> String = { minutes ->
        val tail = minutes % 10
        val hundred = minutes % 100
        val word = when {
            hundred in 11..14 -> "минут"
            tail == 1 -> "минуту"
            tail in 2..4 -> "минуты"
            else -> "минут"
        }
        "$minutes $word назад"
    }
    override val devicesLastActiveHours: (Int) -> String = { hours ->
        val tail = hours % 10
        val hundred = hours % 100
        val word = when {
            hundred in 11..14 -> "часов"
            tail == 1 -> "час"
            tail in 2..4 -> "часа"
            else -> "часов"
        }
        "$hours $word назад"
    }
    override val devicesLastActiveYesterday: String = "Вчера"
    override val devicesLastActiveDate: (String) -> String = { date -> "Был(а): $date" }
    override val devicesDateTimePattern: String = "dd.MM.yyyy, HH:mm"
    override val devicesUnknownDevice: String = "Неизвестное устройство"
    override val devicesUnknownLocation: String = "Местоположение неизвестно"
    override val devicesNoOtherSessions: String = "Других сеансов нет"
    override val devicesNoOtherSessionsHint: String = "В аккаунт выполнен вход только с этого устройства"
    override val devicesEmptyTitle: String = "Активных сеансов нет"
    override val devicesEmptySubtitle: String = "Не удалось найти ни одного входа в аккаунт. Попробуйте обновить список."
    override val devicesLoading: String = "Загружаем сеансы..."
    override val devicesLoadFailedTitle: String = "Не удалось загрузить"
    override val devicesLoadFailedSubtitle: String = "Сервер не ответил. Проверьте соединение и попробуйте снова."
    override val devicesTerminateCd: String = "Завершить сеанс"
    override val devicesTerminateAll: String = "Завершить все"
    override val devicesTerminateTitle: String = "Завершить сеанс?"
    override val devicesTerminateText: (String) -> String = { name ->
        "Устройство «$name» выйдет из аккаунта. Для повторного входа понадобится код подтверждения."
    }
    override val devicesTerminateAllTitle: String = "Завершить все сеансы?"
    override val devicesTerminateAllText: (Int) -> String = { count ->
        "Из аккаунта выйдут все другие устройства ($count). Это устройство останется в сети."
    }
    override val devicesTerminateConfirm: String = "Завершить"
    override val devicesSecurityHint: String = "Не узнаёте устройство? Завершите сеанс и смените пароль."
    // Edit profile field
    override val editFieldSave: String = "Сохранить"
    override val editFieldSaveCd: String = "Сохранить изменения"
    override val editFieldPlaceholder: (String) -> String = { title -> "Введите ${title.lowercase()}" }
    override val editFieldClearCd: String = "Очистить поле"
    override val editFieldCounter: (Int, Int) -> String = { used, max -> "$used / $max" }
    override val editFieldLimitReached: String = "Достигнут лимит символов"
    override val editFieldUnsavedTitle: String = "Сохранить изменения?"
    override val editFieldUnsavedText: String = "У вас есть несохранённые изменения. Сохранить их перед выходом?"
    override val editFieldUnsavedDiscard: String = "Сбросить"
    // Main container navigation (a11y)
    override val a11yTab: (String) -> String = { name -> "Вкладка «$name»" }
    // Profile screen (redesign)
    override val profileSectionInfo: String = "Информация"
    override val profileSectionAppearance: String = "Оформление"
    override val profileSectionSession: String = "Сеанс"
    override val profileCopyUsername: String = "Скопировать юзернейм"
    override val profileCopyUsernameHint: String = "Нажмите, чтобы скопировать"
    override val a11yAvatar: String = "Аватар профиля"
    override val a11yEditAvatar: String = "Изменить аватар"
    override val a11yAvatarPreview: String = "Предпросмотр аватара"
    override val a11yChoosePhoto: String = "Выбрать фото"
    override val avatarCropHint: String = "Перетаскивайте и сводите пальцы, чтобы разместить фото. Загрузится область внутри круга."
    override val avatarPickPrompt: String = "Нажмите, чтобы выбрать фото"

    // Profile screen (extra)
    override val profileNotFoundTitle: String = "Пользователь не найден"
    override val profileNotFoundDesc: (String) -> String = { username -> "$username не зарегистрирован в Vibe или удалил аккаунт." }
    override val profileLoading: String = "Загружаем профиль..."
    override val profileWriteBtn: String = "Написать"
    override val profileBlockedTitle: String = "Вы заблокировали этого пользователя"
    override val profileBlockedDesc: String = "Он не может писать вам и не видит ваш статус."
    override val a11yProfileMenu: String = "Ещё"
    override val a11yAvatarViewerClose: String = "Закрыть просмотр"
    // Compact number units
    override val unitCompactFormat: (String, String) -> String = { number, unit -> "$number $unit" }
    override val unitThousandShort: String = "тыс."
    override val unitMillionShort: String = "млн."
    override val unitBillionShort: String = "млрд."
    // Settings root list
    override val settingsChats: String = "Настройки чатов"
    override val settingsChatsSubtitle: String = "Тема, обои, размер текста"
    override val settingsPrivacySubtitle: String = "Кто видит вас и пишет вам"
    override val settingsNotifications: String = "Уведомления"
    override val settingsNotificationsSubtitle: String = "Звуки, превью, приоритет"
    override val settingsPowerSaving: String = "Экономия энергии"
    override val settingsPowerSavingSubtitle: String = "Анимации и фоновая работа"
    override val settingsDevicesSubtitle: String = "Активные сеансы и выход"
    override val settingsLanguageSubtitle: String = "Язык интерфейса"
    override val settingsAccountSubtitle: String = "Имя, юзернейм, описание"
    override val settingsSupport: String = "Поддержка"
    override val settingsSupportSubtitle: String = "Вопросы, отчёты об ошибках"
    override val settingsVibePro: String = "Vibe Pro"
    override val settingsVibeProSubtitle: String = "Больше лимитов, эксклюзивные функции"
    override val settingsVibeProCta: String = "Подробнее"
    // Vibe Pro screen
    override val vibeProHeroDescription: String = "Максимальные возможности общения, увеличенные лимиты и эксклюзивный статус в Vibe."
    override val vibeProSectionFeatures: String = "ВОЗМОЖНОСТИ ПОДПИСКИ"
    override val vibeProSectionPlans: String = "ТАРИФНЫЙ ПЛАН"
    override val vibeProFeatureLimitsTitle: String = "Увеличенные лимиты"
    override val vibeProFeatureLimitsSubtitle: String = "Отправка файлов до 2 ГБ, до 100 закрепленных чатов и 20 папок"
    override val vibeProFeatureVoiceToTextTitle: String = "Голосовые в текст"
    override val vibeProFeatureVoiceToTextSubtitle: String = "Мгновенная расшифровка аудио- и видеосообщений одним касанием"
    override val vibeProFeatureReactionsTitle: String = "Эксклюзивные реакции"
    override val vibeProFeatureReactionsSubtitle: String = "Анимированные стикеры, уникальные эмодзи и эмодзи-статусы"
    override val vibeProFeatureBadgeTitle: String = "Премиум-значок"
    override val vibeProFeatureBadgeSubtitle: String = "Особый значок Pro рядом с вашим именем в чатах и профиле"
    override val vibeProFeatureSpeedTitle: String = "Сверхбыстрая скорость"
    override val vibeProFeatureSpeedSubtitle: String = "Загрузка и отправка медиафайлов без ограничения пропускной способности"
    override val vibeProFeatureNoAdsTitle: String = "Полная свобода"
    override val vibeProFeatureNoAdsSubtitle: String = "Никакой рекламы, приоритетная техническая поддержка 24/7"
    override val vibeProPlanYearly: String = "1 год"
    override val vibeProPlanYearlyPrice: String = "149 ₽ / мес"
    override val vibeProPlanYearlyDiscount: String = "−25%"
    override val vibeProPlanMonthly: String = "1 месяц"
    override val vibeProPlanMonthlyPrice: String = "199 ₽ / мес"
    override val vibeProSubscribeCta: (String) -> String = { price -> "Подключить Vibe Pro — $price" }
    override val vibeProAutoRenewalDisclaimer: String = "Подписка продлевается автоматически. Отменить можно в любое время."
    override val vibeProComingSoonToast: String = "Оформление подписки станет доступно в ближайшем обновлении"
    // Two-factor authentication (2FA)
    override val twoFactorTitle: String = "Двухэтапная аутентификация"
    override val twoFactorSubtitle: String = "Дополнительный пароль для защиты при входе"
    override val twoFactorDescription: String = "Вы можете задать дополнительный пароль, который потребуется вводить при входе с нового устройства в дополнение к коду из почты."
    override val twoFactorStatusEnabled: String = "Включена"
    override val twoFactorStatusDisabled: String = "Выключена"
    override val twoFactorEnabledBadge: String = "Защита активна"
    override val twoFactorEnabledDesc: String = "При входе на новом устройстве потребуется ввести этот пароль после кода подтверждения."
    override val twoFactorBullet1Title: String = "Надёжная защита"
    override val twoFactorBullet1Desc: String = "Даже при утере доступа к почте злоумышленник не сможет войти в ваш аккаунт"
    override val twoFactorBullet2Title: String = "Облачный пароль"
    override val twoFactorBullet2Desc: String = "Пароль надёжно зашифрован в защищённом хранилище"
    override val twoFactorBullet3Title: String = "Подсказка для памяти"
    override val twoFactorBullet3Desc: String = "Возможность указать подсказку, которая поможет вспомнить пароль"
    override val twoFactorSetPasswordBtn: String = "Задать пароль"
    override val twoFactorChangePasswordBtn: String = "Изменить пароль"
    override val twoFactorChangeHintBtn: String = "Изменить подсказку"
    override val twoFactorDisableBtn: String = "Отключить защиту"
    override val twoFactorEnterNewPasswordTitle: String = "Новый пароль"
    override val twoFactorEnterNewPasswordSubtitle: String = "Придумайте пароль длиной не менее 6 символов"
    override val twoFactorRepeatPasswordTitle: String = "Повторите пароль"
    override val twoFactorRepeatPasswordSubtitle: String = "Введите пароль ещё раз для подтверждения"
    override val twoFactorEnterCurrentPasswordTitle: String = "Текущий пароль"
    override val twoFactorEnterCurrentPasswordSubtitle: String = "Введите текущий пароль двухэтапной аутентификации"
    override val twoFactorHintTitle: String = "Подсказка для пароля"
    override val twoFactorHintSubtitle: String = "Подсказка поможет вспомнить пароль при необходимости"
    override val twoFactorHintPlaceholder: String = "Например: любимая книга или дата"
    override val twoFactorHintTooLong: String = "Подсказка не должна превышать 32 символов"
    override val twoFactorHintContainsPassword: String = "Подсказка не должна содержать сам пароль"
    override val twoFactorHintPublicWarning: String = "Подсказка видна любому, кто попытается войти в ваш аккаунт"
    override val twoFactorPasswordTooShort: String = "Пароль должен содержать минимум 6 символов"
    override val twoFactorPasswordMismatch: String = "Пароли не совпадают"
    override val twoFactorPasswordWrong: String = "Неверный текущий пароль"
    override val twoFactorDisableConfirmTitle: String = "Отключить двухэтапную защиту?"
    override val twoFactorDisableConfirmDesc: String = "Для входа на новых устройствах снова будет достаточно только кода из почты."
    override val twoFactorDisableAction: String = "Отключить"
    override val twoFactorNextBtn: String = "Далее"
    override val twoFactorSkipBtn: String = "Пропустить"
    override val twoFactorSaveBtn: String = "Сохранить"
    override val twoFactorStrengthWeak: String = "Слабый"
    override val twoFactorStrengthMedium: String = "Средний"
    override val twoFactorStrengthStrong: String = "Надёжный"
    override val twoFactorStrengthVeryStrong: String = "Отличный"
    override val twoFactorCurrentHintPill: (String) -> String = { hint -> "Подсказка: $hint" }
    override val twoFactorSuccessSetToast: String = "Двухэтапная аутентификация успешно включена"
    override val twoFactorSuccessChangedToast: String = "Пароль успешно изменён"
    override val twoFactorSuccessDisabledToast: String = "Двухэтапная аутентификация отключена"
    override val twoFactorPasswordFieldLabel: String = "Пароль"
    override val twoFactorConfirmFieldLabel: String = "Подтверждение пароля"
    override val twoFactorCurrentFieldLabel: String = "Текущий пароль"
    override val twoFactorHintFieldLabel: String = "Подсказка (необязательно)"
    override val settingsVibes: String = "Vibes"
    override val settingsVibesSubtitle: String = "Оформление и эффекты чатов"
    override val settingsGroupGeneral: String = "Основное"
    override val settingsGroupExtras: String = "Дополнительно"
    override val settingsGroupHelp: String = "Помощь"
    override val settingsSoonBadge: String = "Скоро"
    override val appVersion: (String) -> String = { version -> "Версия приложения $version" }
    // Onboarding controls
    override val onboardingGetStarted: String = "НАЧАТЬ"
    override val onboardingSkip: String = "Пропустить"
    override val a11yOnboardingPage: (Int, Int) -> String = { current, total -> "Экран $current из $total" }
    // Passcode
    override val passcodeEnterTitle: String = "Введите код-пароль"
    override val passcodeEnterSubtitle: String = "Четыре цифры для входа"
    override val passcodeEnterCurrentTitle: String = "Введите текущий код-пароль"
    override val passcodeCreateTitle: String = "Придумайте код-пароль"
    override val passcodeRepeatTitle: String = "Повторите код-пароль"
    override val passcodeInfoTitle: String = "Вход по коду"
    override val passcodeInfoText: String = "Код-пароль дополнительно защитит ваши данные. При открытии приложения потребуется ввести установленный код-пароль."
    override val passcodeEnableBtn: String = "Включить код-пароль"
    override val passcodeChangeBtn: String = "Изменить код-пароль"
    override val passcodeDisableBtn: String = "Отключить код-пароль"
    override val passcodeDisableShort: String = "Отключить"
    override val passcodeRemoveTitle: String = "Отключить код-пароль?"
    override val passcodeRemoveText: String = "Код-пароль будет удалён, приложение перестанет запрашивать его при запуске."
    override val passcodeWrongCode: String = "Неверный код-пароль"
    override val passcodeMismatch: String = "Код-пароли не совпадают"
    override val a11yPasscodeLock: String = "Защита кодом-паролем"
    override val a11yPasscodeBackspace: String = "Удалить цифру"
    override val a11yPasscodeDigit: (String) -> String = { digit -> "Цифра $digit" }
    // Nickname screen
    override val nicknameHint: String = "От 1 до 32 символов. Имя можно изменить позже в настройках."

    // Shared UI components (button, text field, OTP, toast, inline keyboard)
    override val a11yLoading: String = "Загрузка"
    override val a11yOtpInput: String = "Код подтверждения"
    override val a11yOtpDigit: (Int, Int) -> String = { position, total -> "Цифра $position из $total" }
    override val a11yOtpDigitEmpty: (Int, Int) -> String = { position, total -> "Цифра $position из $total, не введена" }
    override val a11yFieldError: (String) -> String = { error -> "Ошибка: $error" }
    override val a11yClearField: String = "Очистить поле"
    override val a11yInlineButtonLink: String = "Открывает внешнюю ссылку"
    override val a11yInlineButtonLoading: String = "Выполняется запрос"

    // Link confirmation dialog & inline formatting (a11y)
    override val linkDialogTitle: String = "Открыть ссылку?"
    override val linkDialogSubtitle: String = "Вы переходите на внешний сайт"
    override val linkDialogSecure: String = "Защищённое соединение"
    override val linkDialogInsecure: String = "Соединение без шифрования"
    override val linkDialogOpen: String = "Перейти"
    override val linkDialogCancel: String = "Отмена"
    override val a11yLinkChip: (String) -> String = { domain -> "Ссылка на $domain" }
    override val a11ySpoilerHidden: String = "Скрытый текст. Нажмите, чтобы показать"
    override val a11ySpoilerRevealed: String = "Скрытый текст показан"
    override val a11yQuote: String = "Цитата"
    override val formatInlineQuoteWrap: (String) -> String = { text -> "«$text»" }

    override val locale: String = "ru"
}

object EnStrings : VibeStrings {
    override val createAccount: String = "Create Account"
    override val welcomeBack: String = "Welcome Back"
    override val emailLabel: String = "EMAIL"
    override val usernameLabel: String = "USERNAME"
    override val emailInvalidFormat: String = "INVALID FORMAT"
    override val continueBtn: String = "CONTINUE"
    override val verificationTitle: String = "Enter Code"
    override val verificationSubtitle: (String) -> String = { email -> "We sent a 6-digit code to\n$email" }
    override val verifyBtn: String = "VERIFY"
    override val verifyLoading: String = "VERIFYING..."
    override val codeInvalid: String = "Invalid code"
    override val nicknameTitle: String = "What's your name?"
    override val nicknameLabel: String = "YOUR NAME"
    override val saveBtn: String = "CONTINUE"
    override val saveLoading: String = "SAVING..."
    override val errorSaving: String = "Error saving"
    override val tabChats: String = "Chats"
    override val tabSettings: String = "Settings"
    override val tabProfile: String = "Profile"
    override val profileTitle: String = "Profile"
    override val deletedAcc: String = "Deleted account"
    override val statusOnline: String = "Online"
    override val statusBot: String = "Bot"
    override val statusUnknown: String = "Unknown"
    override val actionForward: String = "Forward"
    override val badgeVerified: String = "Verified by Vibe Team."
    override val badgeDeveloper: String = "Vibe Developer Team member."
    override val badgeBot: String = "Just a bot."
    override val freezedAcc: String = "Account frozen for violating the rules."
    override val bannedAcc: String = "Account banned for violating the rules."
    override val aboutLabel: String = "About"
    override val registerDateLabel: String = "Joined"
    override val btnTheme: String = "Theme"
    override val themeDark: String = "Dark"
    override val themeLight: String = "Light"
    override val btnLanguage: String = "Language"
    override val btnLogout: String = "Log Out"
    override val logoutConfirmTitle: String = "Log Out"
    override val logoutConfirmText: String = "Are you sure you want to log out?"
    override val logoutCancel: String = "Cancel"
    override val logoutConfirm: String = "Log Out"
    override val userLabel: String = "User"
    override val chatsTitle: String = "Chats"
    override val chatsEmptyTitle: String = "No chats"
    override val chatsEmptySubtitle: String = "Start a conversation!"
    override val searchPlaceholder: String = "Search..."
    override val globalSearchResults: String = "Global Search Results"
    override val typing: String = "Typing"
    override val connecting: String = "Connecting"
    override val waitingForNetwork: String = "Waiting for network"
    override val monthsShort: List<String> = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    override val dateToday: String = "Today"
    override val dateYesterday: String = "Yesterday"
    override val lastSeenRecently: String = "Last seen recently"
    override val lastSeenLongAgo: String = "Last seen a long time ago"
    override val lastSeenInWeek: String = "Last seen this week"
    override val lastSeenInMonth: String = "Last seen this month"
    override val lastSeenToday: (String) -> String = { time -> "Last seen today at $time" }
    override val lastSeenYesterday: (String) -> String = { time -> "Last seen yesterday at $time" }
    override val lastSeenDate: (String, String) -> String = { date, time -> "Last seen $date at $time" }
    override val backBtn: String = "Back"
    override val draftLabel: String = "Draft: "
    override val messagePlaceholder: String = "Message..."
    override val replyTo: String = "Reply"
    override val emptyChat: String = "It's empty here..."
    override val chatHistoryCleared: String = "History cleared"
    override val chatHistoryEmpty: String = "History is empty"
    override val onboardingPages: List<Pair<String, String>> = listOf(
        "Data Shield" to "Your communication is locked with military-grade encryption.",
        "Light Speed" to "Next-gen protocols for instant message propagation.",
        "Universal Sync" to "Your entire data history available on every device.",
        "Global Hubs" to "Scale communities to millions of active nodes.",
        "Unique Vibe" to "Tailor every aspect of your messaging experience."
    )
    override val selectedMessagesCount: (Int) -> String = { count -> "Selected: $count" }
    override val deleteMessagesTitle: String = "Delete messages?"
    override val deleteMessagesText: (Int) -> String = { count -> "You are about to delete $count messages." }
    override val deleteForEveryone: (String) -> String = { name -> "Also delete for $name" }
    override val deleteForEveryoneAlsoMine: (String) -> String = { name -> "Also delete my messages for $name" }
    override val deleteBtn: String = "Delete"
    override val cancelBtn: String = "Cancel"
    override val forwardMessageTitle: String = "Forward message"
    override val noRecentChats: String = "No recent chats"
    override val editMessageTitle: String = "Edit message"
    override val sendBtn: String = "Send"
    override val forwardedFrom: (String) -> String = { name -> "Forwarded from $name" }
    override val replyDefault: String = "Reply"
    override val pinnedMessage: String = "Pinned message"
    override val pinnedMessages: String = "Pinned messages"
    override val pinMessage: String = "Pin message"
    override val pinMessageConfirm: String = "Are you sure you want to pin this message?"
    override val unpinMessage: String = "Unpin message"
    override val unpinMessageConfirm: String = "Are you sure you want to unpin this message?"
    override val unpinAll: String = "Unpin all"
    override val unpinAllConfirm: String = "Unpin all messages in this chat?"
    override val forBoth: (String) -> String = { name -> "Also for $name" }
    override val pin: String = "Pin"
    override val unpin: String = "Unpin"
    override val you: String = "You"
    override val edit: String = "Edit"
    override val usernameTaken: String = "This username is already taken"
    override val usernameAvailable: String = "Username is available"
    override val usernameDescription: String = "You can choose a unique username for Vibe. Other people will be able to find you by this name and contact you without knowing your phone number."
    override val usernameMinLength: String = "Minimum 4 characters"
    override val nicknameDescription: String = "Your display name visible to all users. Try to choose a recognizable name so friends can easily find you."
    override val bioDescription: String = "Write a bit about yourself. This information will be visible to other users on your profile."
    override val doneBtn: String = "Done"
    override val userRestrictedMessaging: String = "This user has restricted who can message them"
    override val userHiddenAccount: String = "User has hidden their account"
    override val editedLabel: String = " (edited)"
    override val muteNotifications: String = "Mute notifications"
    override val unmuteNotifications: String = "Unmute notifications"
    override val pinnedMessageSystemText: (String, String) -> String = { sender, content -> "$sender pinned a message: \"$content\"" }
    override val privacyScreenTitle: String = "Privacy"
    override val privacyTwoFactor: String = "Two-factor authentication"
    override val privacyPasscodeLogin: String = "Passcode login"
    override val privacyBlocked: String = "Blocked"
    override val privacyActivityTitle: String = "Activity status"
    override val privacyActivityDesc: String = "Who can see when you were last online. If you hide your activity status, you won't be able to see other users' status (approximate time will be shown)."
    override val privacyAvatarTitle: String = "Avatar"
    override val privacyAvatarDesc: String = "Who can see your avatar. Others will see the first letter of your name on a blue background."
    override val privacyForwardedTitle: String = "Forwarded messages"
    override val privacyForwardedDesc: String = "Who can navigate to your profile from forwarded messages."
    override val privacyMessagesTitle: String = "Messages"
    override val privacyMessagesDesc: String = "Who can send you messages. Users who are restricted will see a notice that you've limited who can contact you."
    override val privacyStatusTitle: String = "Status"
    override val privacyStatusDesc: String = "Who can see the 'About' section in your profile."

    // Report dialog
    override val reportTitle: String = "Report"
    override val reportSubtitle: String = "Pick a reason. Reports are anonymous, the author won't see it."
    override val reportStepLabel: (Int, Int) -> String = { current, total -> "Step $current of $total" }
    override val reportReasonSpam: String = "Spam"
    override val reportReasonSpamDesc: String = "Ads, mass messaging, engagement farming"
    override val reportReasonFraud: String = "Fraud"
    override val reportReasonFraudDesc: String = "Scams, phishing, money schemes"
    override val reportReasonDrugs: String = "Drugs"
    override val reportReasonDrugsDesc: String = "Selling or promoting illegal substances"
    override val reportReasonWeapons: String = "Weapons"
    override val reportReasonWeaponsDesc: String = "Trading weapons or explosives"
    override val reportReasonPorn: String = "Pornography"
    override val reportReasonPornDesc: String = "Adult content shared without warning"
    override val reportReasonCsam: String = "Child sexual abuse material (CSAM)"
    override val reportReasonCsamDesc: String = "Sexualised content involving minors"
    override val reportReasonViolence: String = "Violence"
    override val reportReasonViolenceDesc: String = "Threats, cruelty, calls to harm"
    override val reportReasonHarassment: String = "Harassment"
    override val reportReasonHarassmentDesc: String = "Insults, stalking, blackmail"
    override val reportReasonHate: String = "Hate speech"
    override val reportReasonHateDesc: String = "Attacks based on race, religion, gender"
    override val reportReasonFakeAccount: String = "Fake account"
    override val reportReasonFakeAccountDesc: String = "Impersonating someone else"
    override val reportReasonMisinfo: String = "False information"
    override val reportReasonMisinfoDesc: String = "Dangerous rumours and disinformation"
    override val reportCriticalNotice: String = "Reports like this get priority review and are escalated to the proper authorities."
    override val reportDetailsTitle: String = "Tell us more"
    override val reportDetailsHint: String = "Describe what happened and where. It helps us act faster."
    override val reportCommentPlaceholder: String = "Comment (optional)"
    override val reportCommentCounter: (Int, Int) -> String = { used, limit -> "$used / $limit" }
    override val reportChangeReason: String = "Change reason"
    override val reportSubmitBtn: String = "Send report"
    override val reportCancelBtn: String = "Cancel"
    override val reportSentTitle: String = "Report sent"
    override val reportSentDesc: String = "Thanks. Our moderators will review it and decide what to do."
    override val reportDoneBtn: String = "Done"
    override val a11yReportClose: String = "Close"

    // Privacy exceptions picker
    override val privacyExceptionsTitle: String = "Exceptions"
    override val privacyExceptionsHint: String = "Pick who the rule does not apply to"
    override val privacyExceptionsSelected: (Int) -> String = { count -> "$count selected" }
    override val privacyExceptionsSelectAll: String = "Select all"
    override val privacyExceptionsClearAll: String = "Clear selection"
    override val privacyExceptionsEmptyTitle: String = "Nobody to pick"
    override val privacyExceptionsEmptyDesc: String = "People you have chats with will show up here."
    override val a11yExceptionToggle: (String) -> String = { name -> "Select $name" }
    override val a11yExceptionRemove: (String) -> String = { name -> "Remove $name from selection" }
    override val settingsPrivacy: String = "Privacy"
    override val settingsAccount: String = "Account"
    override val settingsDevices: String = "Devices"
    override val settingsPasscode: String = "Passcode"
    override val usernameCopied: String = "Username copied"
    override val addAvatar: String = "Add avatar"
    override val choosePhoto: String = "Choose photo"
    override val switchSignIn: String = "Already have an account? Sign In"
    override val switchSignUp: String = "New here? Create Account"
    override val authTabSignUp: String = "Sign Up"
    override val authTabSignIn: String = "Sign In"
    override val authChecking: String = "Checking..."
    override val authEmailAvailable: String = "Email is available"
    override val authEmailTaken: String = "EMAIL ALREADY TAKEN"
    override val authUsernameTakenShort: String = "USERNAME ALREADY TAKEN"
    override val authUsernameAvailable: (String) -> String = { username -> "@$username is available" }
    override val authRegisterFailed: String = "Could not create account"
    override val authLoginFailed: String = "Could not sign in"
    override val authUsernameCounter: (Int, Int) -> String = { current, max -> "$current/$max" }
    override val languageName: String = "English"
    override val blockedTitle: String = "Blocked"
    override val blockedSearchPlaceholder: String = "Search users..."
    override val blockedClearSearch: String = "Clear search"
    override val blockedEmptyTitle: String = "No blocked users"
    override val blockedEmptyDesc: String = "Users you block will appear here."
    override val blockedSearchEmptyTitle: String = "Nothing found"
    override val blockedSearchEmptyDesc: (String) -> String = { query -> "No users match \"$query\"" }
    override val blockedUnblockBtn: String = "Unblock"
    override val blockedUnblockConfirmTitle: String = "Unblock user?"
    override val blockedUnblockConfirmText: (String) -> String = { name -> "$name will be able to message you and see your profile again." }
    override val blockedUnblockedToast: String = "User unblocked"
    override val blockedUserFallback: (Int) -> String = { id -> "User #$id" }
    override val accountDeleted: String = "Deleted account"
    override val accountFrozen: String = "Frozen account"
    override val accountBannedMessage: String = "Your account has been banned for violating the rules."
    override val accountFrozenMessage: String = "Your account has been frozen by a moderator."
    override val typeVideo: String = "Video"
    override val typeVideoMessage: String = "Video message"
    override val typeAudio: String = "Audio file"
    override val typeFile: String = "File"
    override val typeVoice: String = "Voice message"
    override val playerPlaylist: String = "Chat playlist"
    override val playerSearchTracks: String = "Search tracks"
    override val playerNowPlaying: String = "Now playing"
    override val playerTrackFallback: String = "Audio"
    override val playerTracksCount: (Int) -> String = { count ->
        if (count == 1) "1 track" else "$count tracks"
    }
    override val playerQueueEmpty: String = "Queue is empty"
    override val playerQueueEmptyHint: String = "Audio from this chat will show up here"
    override val playerSearchEmptyTitle: String = "Nothing found"
    override val playerSearchEmptySubtitle: (String) -> String = { query -> "No tracks match \"$query\"" }
    override val playerBuffering: String = "Buffering…"
    override val playerTimeZero: String = "0:00"
    override val playerTimeUnknown: String = "--:--"
    override val playerSpeedFormat: (String) -> String = { value -> "$value×" }
    override val playerRepeatOff: String = "Repeat off"
    override val playerRepeatAll: String = "Repeat playlist"
    override val playerRepeatOne: String = "Repeat track"
    override val a11yPlayerPlay: String = "Play"
    override val a11yPlayerPause: String = "Pause"
    override val a11yPlayerNext: String = "Next track"
    override val a11yPlayerPrevious: String = "Previous track"
    override val a11yPlayerRewind10: String = "Rewind 10 seconds"
    override val a11yPlayerForward10: String = "Forward 10 seconds"
    override val a11yPlayerShuffle: String = "Shuffle"
    override val a11yPlayerClose: String = "Close player"
    override val a11yPlayerExpand: String = "Expand player"
    override val a11yPlayerCollapse: String = "Collapse player"
    override val a11yPlayerSearch: String = "Search the playlist"
    override val a11yPlayerSearchClose: String = "Close search"
    override val a11yPlayerClearSearch: String = "Clear search"
    override val a11yPlayerArtwork: String = "Track artwork"
    override val a11yPlayerSpeed: (String) -> String = { value -> "Playback speed: $value" }
    override val a11yPlayerTrackRow: (String) -> String = { title -> "Play track $title" }
    override val sectionPhotosVideos: String = "Photos and videos"
    override val sectionFiles: String = "Files"
    override val sectionMusic: String = "Music"
    override val sectionVoice: String = "Voice messages"
    override val profileSearchFiles: String = "Search files"
    override val actionDownload: String = "Download"
    override val actionDownloadSelected: String = "Download selected"
    override val formatCopy: String = "Copy"
    override val formatCut: String = "Cut"
    override val formatFormat: String = "Format"
    override val formatBold: String = "Bold"
    override val formatItalic: String = "Italic"
    override val formatBoldItalic: String = "Bold Italic"
    override val formatStrikethrough: String = "Strikethrough"
    override val formatUnderline: String = "Underline"
    override val formatMonospace: String = "Monospace"
    override val formatLink: String = "Link"
    override val formatTextColor: String = "Text Color"
    override val formatSpoiler: String = "Spoiler"
    override val formatQuote: String = "Quote"
    override val formatLinkUrlHint: String = "Enter URL"
    override val formatColorHint: String = "Enter HEX (e.g. #FF5733)"
    override val formatPreview: String = "Preview"
    override val formatCopied: String = "Copied"
    override val formatReadMore: String = "Read more"
    override val formatCollapse: String = "Collapse"
    override val attachPhotoVideo: String = "Photos & Videos"
    override val attachFile: String = "File"
    override val attachTitle: String = "Attachments"
    override val photoViewer: String = "Photo viewer"
    override val photoOf: (Int, Int) -> String = { current, total -> "$current of $total" }
    override val actionCopy: String = "Copy text"
    override val actionReport: String = "Report"


    // Chat header
    override val chatSearchPlaceholder: String = "Search messages…"
    override val chatSearchNoResults: String = "No matches"
    override val chatSearchCounter: (Int, Int) -> String = { current, total -> "$current of $total" }
    override val chatSearchClose: String = "Close search"
    override val chatSearchClear: String = "Clear query"
    override val chatSearchByDate: String = "Search by date"
    override val chatSearchNext: String = "Next result"
    override val chatSearchPrev: String = "Previous result"
    override val chatActionSearch: String = "Search"
    override val chatMenu: String = "Chat menu"
    override val chatAvatar: String = "Avatar"
    override val chatOpenProfile: String = "Open profile"
    override val chatClearSelection: String = "Clear selection"
    override val chatStatusBlockedByMe: String = "Blocked"
    override val chatBlockUser: String = "Block user"
    override val chatUnblockUser: String = "Unblock user"
    override val chatUserBlockedToast: String = "User blocked"
    override val chatPinnedCounter: (Int, Int) -> String = { current, total -> "$current/$total" }
    override val chatJumpToPinned: String = "Jump to pinned message"
    override val chatUnpinAllHint: String = "Unpin all"


    // Chat input bar
    override val inputAttachMedia: String = "Attach"
    override val inputAttachGallery: String = "Photo or Video"
    override val inputAttachFile: String = "File"
    override val inputEmojiPanel: String = "Emoji, stickers and GIFs"
    override val inputSelectedMedia: (Int) -> String = { count -> "Media selected: $count" }
    override val inputClearAttachments: String = "Clear attachments"
    override val inputBlockedByMe: String = "You blocked this user"
    override val inputAttachmentPreview: String = "Attachment"
    override val inputCancelReply: String = "Cancel"
    override val voiceHoldToRecord: String = "Hold to record"
    override val voiceSendRecording: String = "Send voice message"
    override val voiceCancelRecording: String = "Cancel recording"
    override val voiceLocked: String = "Recording locked"
    override val voiceSlideToCancel: String = "Slide left to cancel"
    override val voiceSlideToLock: String = "Up to lock"
    override val voiceRecordStartFailed: String = "Could not start voice recording"
    override val voicePermissionRequired: String = "Microphone access is required to record"
    override val voiceTooShort: String = "The recording was too short"

    // Circles (video messages)
    override val circleModeSwitchedOn: String = "Circle mode: hold to record"
    override val circleModeSwitchedOff: String = "Voice message mode"
    override val circleRecordVideoMessage: String = "Record a video message"
    override val circleHoldOrTapHint: String = "Tap to record, hold for a quick take"
    override val circleRecordingHint: String = "Left to cancel · up to lock"
    override val circleReleaseToCancel: String = "Release to cancel"
    override val circleLockedHint: String = "Tap to send"
    override val circleTapToStop: String = "Tap to stop and send"
    override val circleCameraPreparing: String = "Preparing the camera…"
    override val circleMaxDurationHint: String = "60 seconds max"
    override val circlePermissionRequired: String = "Circles need camera and microphone access"
    override val circleTooShort: String = "The circle was too short"
    override val circleCancel: String = "Cancel recording"
    override val circleSend: String = "Send circle"
    override val circleSwitchCamera: String = "Switch camera"
    override val circleSendFailed: String = "Could not send the circle"


    // Chat list (iOS redesign)
    override val chatsSectionPinned: String = "Pinned"
    override val filterAll: String = "All"
    override val filterUnread: String = "Unread"
    override val filterUnreadCount: (Int) -> String = { count -> "Unread ($count)" }
    override val chatsNoUnreadTitle: String = "All Caught Up"
    override val chatsNoUnreadSubtitle: String = "You have no unread messages."
    override val chatsSectionAll: String = "All Chats"
    override val chatsCountFooter: (Int) -> String = { count -> "$count chats" }
    override val chatsEmptyHint: String = "Find someone using the search above"
    override val chatsSearchCancel: String = "Cancel"
    override val chatsSearchClearField: String = "Clear search field"
    override val chatsSearchNoResultsTitle: String = "No Results"
    override val chatsSearchNoResultsSubtitle: (String) -> String = { query -> "No chats or users match \"$query\"" }
    override val userFallback: (Int) -> String = { id -> "User #$id" }
    override val someoneLabel: String = "Someone"
    override val a11yMutedChat: String = "Notifications muted"
    override val a11yPinnedChat: String = "Chat pinned"
    override val actionMuteShort: String = "Mute"
    override val actionUnmuteShort: String = "Unmute"


    // Chat list message previews
    override val typePhoto: String = "Photo"
    override val previewVoiceMessage: (String) -> String = { duration -> "Voice message $duration" }
    override val previewVideoMessage: (String) -> String = { duration -> "Video message $duration" }
    override val previewAudioTrack: (String, String) -> String = { artist, title -> "$artist — $title" }
    override val previewAudioLoading: String = "Music..."
    override val previewMorePhotos: (Int) -> String = { count -> "+$count photos" }
    override val previewMoreVideos: (Int) -> String = { count -> "+$count videos" }
    override val previewMoreWithCaption: (Int, String) -> String = { count, caption -> "+$count $caption" }
    override val typeSticker: String = "Sticker"
    override val typeGif: String = "GIF"
    override val previewMediaCount: (Int) -> String = { count ->
        if (count == 1) "1 media file" else "$count media files"
    }
    override val previewMoreAudio: (Int) -> String = { count ->
        if (count == 1) "1 audio file" else "$count audio files"
    }
    override val previewMoreFiles: (Int) -> String = { count ->
        if (count == 1) "1 file" else "$count files"
    }
    override val previewMoreAttachments: (Int) -> String = { count ->
        if (count == 1) "1 attachment" else "$count attachments"
    }
    override val actionSelectMessage: String = "Select"
    // Chat message list (empty state, system messages, scroll-to-bottom)
    override val emptyChatSubtitle: String = "Send the first message and your chat history will show up here."
    override val emptyChatHint: String = "Be the first to say hi"
    override val chatSystemMessageLabel: String = "System message"
    override val linkOpenFailed: String = "Couldn't open the link"
    override val a11yMessageList: String = "Message list"
    override val a11yScrollToBottom: String = "Jump to the latest messages"
    override val a11yUnreadCount: (Int) -> String = { count -> "New messages: $count" }
    override val unreadCountOverflow: String = "99+"
    // Chat dialogs, reactions sheet, bot & network toasts
    override val okBtn: String = "OK"
    override val actionClose: String = "Close"
    override val reportSentToast: String = "Report sent"
    override val restrictionTitle: String = "Restriction"
    override val restrictionUnderstood: String = "Got it"
    override val restrictionWhy: String = "Why?"
    override val datePickerTitle: String = "Select a date"
    override val dateJumpNotFound: String = "No messages found for this date"
    override val botMessageTitle: String = "Message from a bot"
    override val botLabel: String = "Bot"
    override val attachmentLabel: String = "Attachment"
    override val fileSizeLoading: String = "Loading..."
    override val reactionsTitle: String = "Reactions"
    override val reactionsAllTab: (Int) -> String = { count -> "All $count" }
    override val reactionsEmpty: String = "No reactions yet"
    override val voiceTrackTitleMine: String = "You (Voice message)"
    override val botCallbackTimeout: String = "The bot didn't respond within 10 seconds. This message's buttons are available again."
    override val connectionLostToast: String = "No connection to the server. Please try again."
    override val fileOpenFailed: String = "Couldn't open the selected file"
    override val maxPinnedChatsToast: (Int) -> String = { limit -> "You can pin up to $limit chats" }
    // Chat toast host
    override val toastTitleInfo: String = "Info"
    override val toastTitleSuccess: String = "Done"
    override val toastTitleWarning: String = "Heads up"
    override val toastTitleError: String = "Error"
    override val toastActionRetry: String = "Retry"
    override val toastActionUndo: String = "Undo"
    override val toastCopied: String = "Copied"
    override val a11yToast: (String) -> String = { text -> "Notification: $text" }
    override val a11yToastDismiss: String = "Dismiss notification"
    // Devices & sessions
    override val devicesTitle: String = "Devices"
    override val devicesSubtitle: String = "Everywhere you're signed in to your account"
    override val devicesSessionsCount: (Int) -> String = { count ->
        if (count == 1) "1 active session" else "$count active sessions"
    }
    override val devicesRefreshCd: String = "Refresh the list"
    override val devicesSectionCurrent: String = "THIS DEVICE"
    override val devicesSectionOther: String = "OTHER SESSIONS"
    override val devicesCurrentBadge: String = "Current"
    override val devicesOnlineNow: String = "Online"
    override val devicesLastActiveNow: String = "Just now"
    override val devicesLastActiveMinutes: (Int) -> String = { minutes ->
        if (minutes == 1) "1 minute ago" else "$minutes minutes ago"
    }
    override val devicesLastActiveHours: (Int) -> String = { hours ->
        if (hours == 1) "1 hour ago" else "$hours hours ago"
    }
    override val devicesLastActiveYesterday: String = "Yesterday"
    override val devicesLastActiveDate: (String) -> String = { date -> "Last active: $date" }
    override val devicesDateTimePattern: String = "MMM d, yyyy, HH:mm"
    override val devicesUnknownDevice: String = "Unknown device"
    override val devicesUnknownLocation: String = "Location unknown"
    override val devicesNoOtherSessions: String = "No other sessions"
    override val devicesNoOtherSessionsHint: String = "This is the only device signed in to your account"
    override val devicesEmptyTitle: String = "No active sessions"
    override val devicesEmptySubtitle: String = "We couldn't find any sign-ins. Try refreshing the list."
    override val devicesLoading: String = "Loading sessions..."
    override val devicesLoadFailedTitle: String = "Couldn't load"
    override val devicesLoadFailedSubtitle: String = "The server didn't respond. Check your connection and try again."
    override val devicesTerminateCd: String = "End session"
    override val devicesTerminateAll: String = "End all"
    override val devicesTerminateTitle: String = "End this session?"
    override val devicesTerminateText: (String) -> String = { name ->
        "\"$name\" will be signed out. Signing back in will require a verification code."
    }
    override val devicesTerminateAllTitle: String = "End all sessions?"
    override val devicesTerminateAllText: (Int) -> String = { count ->
        "All other devices ($count) will be signed out. This device stays online."
    }
    override val devicesTerminateConfirm: String = "End session"
    override val devicesSecurityHint: String = "Don't recognize a device? End its session and change your password."
    // Edit profile field
    override val editFieldSave: String = "Save"
    override val editFieldSaveCd: String = "Save changes"
    override val editFieldPlaceholder: (String) -> String = { title -> "Enter ${title.lowercase()}" }
    override val editFieldClearCd: String = "Clear the field"
    override val editFieldCounter: (Int, Int) -> String = { used, max -> "$used / $max" }
    override val editFieldLimitReached: String = "Character limit reached"
    override val editFieldUnsavedTitle: String = "Save changes?"
    override val editFieldUnsavedText: String = "You have unsaved changes. Do you want to save them before leaving?"
    override val editFieldUnsavedDiscard: String = "Discard"
    // Main container navigation (a11y)
    override val a11yTab: (String) -> String = { name -> "$name tab" }
    // Profile screen (redesign)
    override val profileSectionInfo: String = "Info"
    override val profileSectionAppearance: String = "Appearance"
    override val profileSectionSession: String = "Session"
    override val profileCopyUsername: String = "Copy username"
    override val profileCopyUsernameHint: String = "Tap to copy"
    override val a11yAvatar: String = "Profile avatar"
    override val a11yEditAvatar: String = "Change avatar"
    override val a11yAvatarPreview: String = "Avatar preview"
    override val a11yChoosePhoto: String = "Choose photo"
    override val avatarCropHint: String = "Drag and pinch to position the photo. Everything inside the circle gets uploaded."
    override val avatarPickPrompt: String = "Tap to pick a photo"

    // Profile screen (extra)
    override val profileNotFoundTitle: String = "User not found"
    override val profileNotFoundDesc: (String) -> String = { username -> "$username is not registered on Vibe or deleted their account." }
    override val profileLoading: String = "Loading profile..."
    override val profileWriteBtn: String = "Message"
    override val profileBlockedTitle: String = "You blocked this user"
    override val profileBlockedDesc: String = "They cannot message you and cannot see your status."
    override val a11yProfileMenu: String = "More"
    override val a11yAvatarViewerClose: String = "Close viewer"
    // Compact number units
    override val unitCompactFormat: (String, String) -> String = { number, unit -> "$number$unit" }
    override val unitThousandShort: String = "K"
    override val unitMillionShort: String = "M"
    override val unitBillionShort: String = "B"
    // Settings root list
    override val settingsChats: String = "Chat settings"
    override val settingsChatsSubtitle: String = "Theme, wallpaper, text size"
    override val settingsPrivacySubtitle: String = "Who can see and message you"
    override val settingsNotifications: String = "Notifications"
    override val settingsNotificationsSubtitle: String = "Sounds, previews, priority"
    override val settingsPowerSaving: String = "Power saving"
    override val settingsPowerSavingSubtitle: String = "Animations and background work"
    override val settingsDevicesSubtitle: String = "Active sessions and sign-out"
    override val settingsLanguageSubtitle: String = "Interface language"
    override val settingsAccountSubtitle: String = "Name, username, about"
    override val settingsSupport: String = "Support"
    override val settingsSupportSubtitle: String = "Questions and bug reports"
    override val settingsVibePro: String = "Vibe Pro"
    override val settingsVibeProSubtitle: String = "Higher limits, exclusive features"
    override val settingsVibeProCta: String = "Learn more"
    // Vibe Pro screen
    override val vibeProHeroDescription: String = "Unlock maximum messaging potential, increased limits, and exclusive status in Vibe."
    override val vibeProSectionFeatures: String = "SUBSCRIPTION FEATURES"
    override val vibeProSectionPlans: String = "SUBSCRIPTION PLAN"
    override val vibeProFeatureLimitsTitle: String = "Doubled Limits"
    override val vibeProFeatureLimitsSubtitle: String = "Up to 2 GB file uploads, 100 pinned chats, and 20 chat folders"
    override val vibeProFeatureVoiceToTextTitle: String = "Voice-to-Text"
    override val vibeProFeatureVoiceToTextSubtitle: String = "Instant transcription of voice and video messages with one tap"
    override val vibeProFeatureReactionsTitle: String = "Exclusive Reactions"
    override val vibeProFeatureReactionsSubtitle: String = "Animated stickers, unique emoji reactions, and emoji statuses"
    override val vibeProFeatureBadgeTitle: String = "Premium Badge"
    override val vibeProFeatureBadgeSubtitle: String = "Exclusive Pro badge next to your name in chats and profile"
    override val vibeProFeatureSpeedTitle: String = "Blazing Speed"
    override val vibeProFeatureSpeedSubtitle: String = "Unlimited download and upload speeds for media and files"
    override val vibeProFeatureNoAdsTitle: String = "Complete Freedom"
    override val vibeProFeatureNoAdsSubtitle: String = "No advertisements, 24/7 priority customer support"
    override val vibeProPlanYearly: String = "1 Year"
    override val vibeProPlanYearlyPrice: String = "$1.99 / mo"
    override val vibeProPlanYearlyDiscount: String = "−25%"
    override val vibeProPlanMonthly: String = "1 Month"
    override val vibeProPlanMonthlyPrice: String = "$2.99 / mo"
    override val vibeProSubscribeCta: (String) -> String = { price -> "Subscribe to Vibe Pro — $price" }
    override val vibeProAutoRenewalDisclaimer: String = "Subscription automatically renews. Cancel anytime."
    override val vibeProComingSoonToast: String = "Subscription checkout will be available in the upcoming update"
    // Two-factor authentication (2FA)
    override val twoFactorTitle: String = "Two-Step Verification"
    override val twoFactorSubtitle: String = "Additional password to protect your account on login"
    override val twoFactorDescription: String = "You can set an additional password that will be required when logging in to your account from a new device in addition to the email code."
    override val twoFactorStatusEnabled: String = "Enabled"
    override val twoFactorStatusDisabled: String = "Disabled"
    override val twoFactorEnabledBadge: String = "Protection active"
    override val twoFactorEnabledDesc: String = "When logging in on a new device, you will need to enter this password after the verification code."
    override val twoFactorBullet1Title: String = "Strong Security"
    override val twoFactorBullet1Desc: String = "Even if someone gains access to your email, they will not be able to log in to your account"
    override val twoFactorBullet2Title: String = "Cloud Password"
    override val twoFactorBullet2Desc: String = "Password is encrypted and safely stored in secure storage"
    override val twoFactorBullet3Title: String = "Password Hint"
    override val twoFactorBullet3Desc: String = "You can add a hint to help remember your password"
    override val twoFactorSetPasswordBtn: String = "Set Password"
    override val twoFactorChangePasswordBtn: String = "Change Password"
    override val twoFactorChangeHintBtn: String = "Change Hint"
    override val twoFactorDisableBtn: String = "Disable Protection"
    override val twoFactorEnterNewPasswordTitle: String = "New Password"
    override val twoFactorEnterNewPasswordSubtitle: String = "Choose a password of at least 6 characters"
    override val twoFactorRepeatPasswordTitle: String = "Repeat Password"
    override val twoFactorRepeatPasswordSubtitle: String = "Enter the password again to confirm"
    override val twoFactorEnterCurrentPasswordTitle: String = "Current Password"
    override val twoFactorEnterCurrentPasswordSubtitle: String = "Enter your current two-step verification password"
    override val twoFactorHintTitle: String = "Password Hint"
    override val twoFactorHintSubtitle: String = "A hint to help you recall your password if needed"
    override val twoFactorHintPlaceholder: String = "e.g. favorite book or memorable date"
    override val twoFactorHintTooLong: String = "Hint must not exceed 32 characters"
    override val twoFactorHintContainsPassword: String = "Hint must not contain the password"
    override val twoFactorHintPublicWarning: String = "This hint is visible to anyone attempting to log in to your account"
    override val twoFactorPasswordTooShort: String = "Password must be at least 6 characters"
    override val twoFactorPasswordMismatch: String = "Passwords do not match"
    override val twoFactorPasswordWrong: String = "Incorrect current password"
    override val twoFactorDisableConfirmTitle: String = "Disable Two-Step Verification?"
    override val twoFactorDisableConfirmDesc: String = "Only the verification code from your email will be required to log in on new devices."
    override val twoFactorDisableAction: String = "Disable"
    override val twoFactorNextBtn: String = "Next"
    override val twoFactorSkipBtn: String = "Skip"
    override val twoFactorSaveBtn: String = "Save"
    override val twoFactorStrengthWeak: String = "Weak"
    override val twoFactorStrengthMedium: String = "Medium"
    override val twoFactorStrengthStrong: String = "Strong"
    override val twoFactorStrengthVeryStrong: String = "Excellent"
    override val twoFactorCurrentHintPill: (String) -> String = { hint -> "Hint: $hint" }
    override val twoFactorSuccessSetToast: String = "Two-step verification enabled successfully"
    override val twoFactorSuccessChangedToast: String = "Password changed successfully"
    override val twoFactorSuccessDisabledToast: String = "Two-step verification disabled"
    override val twoFactorPasswordFieldLabel: String = "Password"
    override val twoFactorConfirmFieldLabel: String = "Confirm password"
    override val twoFactorCurrentFieldLabel: String = "Current password"
    override val twoFactorHintFieldLabel: String = "Hint (optional)"
    override val settingsVibes: String = "Vibes"
    override val settingsVibesSubtitle: String = "Chat styling and effects"
    override val settingsGroupGeneral: String = "General"
    override val settingsGroupExtras: String = "Extras"
    override val settingsGroupHelp: String = "Help"
    override val settingsSoonBadge: String = "Soon"
    override val appVersion: (String) -> String = { version -> "App version $version" }
    // Onboarding controls
    override val onboardingGetStarted: String = "GET STARTED"
    override val onboardingSkip: String = "Skip"
    override val a11yOnboardingPage: (Int, Int) -> String = { current, total -> "Page $current of $total" }
    // Passcode
    override val passcodeEnterTitle: String = "Enter passcode"
    override val passcodeEnterSubtitle: String = "Four digits to unlock"
    override val passcodeEnterCurrentTitle: String = "Enter current passcode"
    override val passcodeCreateTitle: String = "Create a passcode"
    override val passcodeRepeatTitle: String = "Repeat the passcode"
    override val passcodeInfoTitle: String = "Passcode lock"
    override val passcodeInfoText: String = "A passcode adds another layer of protection. You'll be asked for it every time the app opens."
    override val passcodeEnableBtn: String = "Enable passcode"
    override val passcodeChangeBtn: String = "Change passcode"
    override val passcodeDisableBtn: String = "Disable passcode"
    override val passcodeDisableShort: String = "Disable"
    override val passcodeRemoveTitle: String = "Disable passcode?"
    override val passcodeRemoveText: String = "The passcode will be deleted and the app will stop asking for it on launch."
    override val passcodeWrongCode: String = "Wrong passcode"
    override val passcodeMismatch: String = "Passcodes don't match"
    override val a11yPasscodeLock: String = "Passcode protection"
    override val a11yPasscodeBackspace: String = "Delete digit"
    override val a11yPasscodeDigit: (String) -> String = { digit -> "Digit $digit" }
    // Nickname screen
    override val nicknameHint: String = "1 to 32 characters. You can change your name later in settings."

    // Shared UI components (button, text field, OTP, toast, inline keyboard)
    override val a11yLoading: String = "Loading"
    override val a11yOtpInput: String = "Verification code"
    override val a11yOtpDigit: (Int, Int) -> String = { position, total -> "Digit $position of $total" }
    override val a11yOtpDigitEmpty: (Int, Int) -> String = { position, total -> "Digit $position of $total, empty" }
    override val a11yFieldError: (String) -> String = { error -> "Error: $error" }
    override val a11yClearField: String = "Clear field"
    override val a11yInlineButtonLink: String = "Opens an external link"
    override val a11yInlineButtonLoading: String = "Request in progress"

    // Link confirmation dialog & inline formatting (a11y)
    override val linkDialogTitle: String = "Open link?"
    override val linkDialogSubtitle: String = "You are leaving the app"
    override val linkDialogSecure: String = "Secure connection"
    override val linkDialogInsecure: String = "Connection is not encrypted"
    override val linkDialogOpen: String = "Open"
    override val linkDialogCancel: String = "Cancel"
    override val a11yLinkChip: (String) -> String = { domain -> "Link to $domain" }
    override val a11ySpoilerHidden: String = "Hidden text. Tap to reveal"
    override val a11ySpoilerRevealed: String = "Hidden text revealed"
    override val a11yQuote: String = "Quote"
    override val formatInlineQuoteWrap: (String) -> String = { text -> "\u201C$text\u201D" }

    override val locale: String = "en"
}

val ruStrings: VibeStrings = RuStrings
val enStrings: VibeStrings = EnStrings

/**
 * Доступ к строкам из слоёв без композиции (ViewModel, сервисы, WebSocket-колбэки).
 *
 * Composition-локали там нет, а тосты и ошибки всё равно должны быть локализованы.
 * Значение выставляется один раз там, где провайдится LocalVibeStrings (см. VibeTheme),
 * плюс страхующий SideEffect в ChatScreen.
 */
object VibeStringsHolder {
    @Volatile
    var current: VibeStrings = RuStrings
}

val LocalVibeStrings = compositionLocalOf<VibeStrings> { RuStrings }