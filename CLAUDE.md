# FeverLog — CLAUDE.md

Android app for parents to log a child's fever, medication and symptoms. **Fully offline: no server, no account, no network access.** **English by default, multi-language by design** (see "Localization" below).

Reference docs (read before starting any milestone):
- `docs/features.md` — full feature specification (source of truth for scope)
- `docs/ui-design-brief.md` — screens and UX intent

Items marked **(ASSUMED)** were proposed, not confirmed by the owner. If something marked ASSUMED blocks you, stop and ask.

## Non-negotiable rules

1. **Never invent medical content.** Fever thresholds, "red flag" criteria, minimum dosing intervals, age cut-offs and **country emergency numbers** live in ONE place each: `app/src/main/assets/safety_rules.json` (loaded by a `SafetyRules` class) and `app/src/main/assets/emergency_numbers.json` (country → number). Every entry has a `source` field and a `verified` flag. Guidelines differ by country, so `safety_rules.json` must allow per-region rule sets. Until the owner provides sources, ship placeholders with `"verified": false`, show no red-flag alerts that rely on unverified rules, and leave a clear TODO. Do not hardcode numbers anywhere else.
2. **No dose calculator.** The app records what the parent gave; it never suggests a dose, never diagnoses, never advises treatment. No AI features.
3. **No INTERNET permission** in the manifest. No analytics, ads, crash reporters, or any third-party SDK that phones home. Sharing/email/maps/calls go through Android intents only.
4. **All data stays on the device** (app-private storage). Photos live in `filesDir`, never in the public gallery. Strip EXIF (incl. GPS) when saving. Exclude the database and photos from Android Auto Backup / device transfer unless the owner explicitly decides otherwise (configure `dataExtractionRules` and `fullBackupContent`).
5. **Medication logging is the highest-risk path.** Wrong-child and double-dose mistakes must be hard to make: show the child's name before saving a dose, warn on early dose, warn on a duplicate active ingredient. Every destructive action is undoable.
6. **Surgical edits over rewrites.** Do not rewrite stable, working code to apply a style preference. Change the minimum needed.
7. **Do not add features outside `docs/features.md`.** If you think something is missing, propose it; don't build it.

## Tech stack (ASSUMED — confirm before milestone 1)

- Kotlin, Jetpack Compose, Material 3, single `app` module
- minSdk 26, targetSdk = latest stable, compileSdk = latest stable
- Architecture: MVVM + unidirectional data flow, repository layer, Kotlin Coroutines + Flow
- Room (SQLite) for persistence; DataStore for settings
- Navigation Compose
- Hilt for DI (or manual DI if you judge it overkill — say so in the plan)
- WorkManager for backup; AlarmManager for dose/measure reminders
- Coil for image display (local files only); `kotlinx.serialization` for backup/export JSON
- App name: **FeverLog**. Package name: `com.feverlog.app` (derived from the app name; owner may change it before milestone 1)
- Strings only in resources (see "Localization"). No hardcoded UI text.

## Data model principles

- Event-based. Every record has: `id` (UUID string), `childId`, `createdAt`, `updatedAt`, `deletedAt` (nullable, soft delete so undo works and future merge is possible).
- Time: `occurredAt` as epoch millis (UTC) plus `zoneId` string. Never store local-time strings.
- Temperature stored as **integer tenths of °C** (e.g. 384 = 38.4 °C). No floats for stored values.
- Anything with a fixed set of values (symptoms, measurement methods, units, active ingredients, record types) is stored as a **stable code** (enum/ID), never as translated display text, so records stay correct when the user changes language. User-typed text is stored as typed.
- Record types: `TemperatureReading` (value, method), `MedicationDose` (medicationId, amount, unit), `Medication` (active ingredient, brand names, min interval from rules/user), `SymptomLog`, `Note`, `PhotoAttachment` (parentRecordId, file name, size), `Child`, `Contact`, `ReminderSchedule`, `Episode` (derived grouping, may be computed), settings in DataStore.
- Import/export/backup must be idempotent: importing the same file twice must not create duplicates (merge by `id`, newest `updatedAt` wins).
- Provide Room migrations from the first schema version; never use destructive migration.

## Permissions policy

- `POST_NOTIFICATIONS`: request in context (not at first launch).
- Exact alarms: use `SCHEDULE_EXACT_ALARM`, check `canScheduleExactAlarms()`, send the user to the system settings screen if denied, and degrade gracefully to inexact alarms with a visible notice. Do not assume `USE_EXACT_ALARM` is allowed by Play policy (verify before using).
- Camera: use the system camera via `ActivityResultContracts.TakePicture` + `FileProvider`; do **not** declare the `CAMERA` permission. Gallery via the Photo Picker (`PickVisualMedia`).
- Phone: default is `ACTION_DIAL` (no permission). Direct call (`ACTION_CALL` + `CALL_PHONE`) is an optional setting, off by default.
- Contacts: use the system contact picker; do **not** request `READ_CONTACTS`.
- Email: `ACTION_SENDTO` (`mailto:`) with prefilled subject/body; attachments through `FileProvider`. Warn the user before sending that email is usually unencrypted.

## UX rules

- Used one-handed at night by a tired parent: primary touch targets ≥ 56dp, ≤ 3 taps to log a temperature or a dose, sensible defaults (time = now).
- Dark theme and a dim "night mode". Must remain usable at 200% font scale.
- Fever status must never be conveyed by color alone (add icon/text).
- Layouts must survive 30–40% longer text (German) and mirror in right-to-left. Use start/end, never left/right.
- Calm tone in copy. No alarmist language. Disclaimer visible in onboarding and in warnings: the app does not replace a doctor.

## Localization (i18n)

- **Source language: English** in `res/values/strings.xml` (default). One translation per `res/values-<qualifier>/`. Launch languages **(ASSUMED — owner to confirm)**: English, Spanish (`es`), French (`fr`), German (`de`), Portuguese (`pt`, with `pt-BR` and `pt-PT` where they differ), Italian (`it`), Greek (`el`). Planned next: Arabic (`ar`, RTL), Simplified Chinese (`zh-rCN`), Hindi (`hi`), Russian (`ru`), Turkish (`tr`), Japanese (`ja`).
- **Design for all of them from milestone 1**, even for languages that ship later: `android:supportsRtl="true"`, start/end attributes, no hardcoded text, no text inside images, no string concatenation. Use positional placeholders (`%1$s`) and `<plurals>` for every count (do not assume two plural forms).
- **Per-app language:** use the AndroidX per-app language APIs (`AppCompatDelegate.setApplicationLocales` + `res/xml/locales_config.xml` referenced by `android:localeConfig`) so Android 13+ system settings and the in-app picker agree. Decide `AppCompatActivity` vs `ComponentActivity` in the milestone 1 plan (per-app locale handling is simpler with `AppCompatActivity`). Default: follow the device language; if unsupported, fall back to English.
- **A language is enabled only when its safety strings are reviewed.** Safety-critical strings use the key prefix `safety_` (disclaimer, red-flag alerts, early-dose and duplicate-ingredient warnings, ingredient names). They must be human-translated and reviewed. Machine translation may be used to draft ordinary UI text only. Keep a list of enabled languages in one place (e.g. `SupportedLanguages`) with a `safetyReviewed` flag; the picker and `locales_config.xml` must only list reviewed languages.
- **Formatting is locale-aware, never hand-built:** dates/times via `java.time` localized formatters and the device 12/24-hour setting; numbers via `NumberFormat`; durations ("2h 10m") via ICU `MeasureFormat` or a plural-aware resource, not concatenation. Weight and temperature units default by selected country and are changeable.
- **Decimal input:** the temperature field must accept both `.` and `,` regardless of locale, and tolerate non-Latin digits where the locale uses them. Parse with a locale-tolerant function and store the result as integer tenths of °C. Add unit tests for `38.4`, `38,4`, and Arabic-Indic digits.
- **Country selection** (onboarding, prefilled from the device region, changeable in Settings) drives the emergency number and default units. Do not infer country from the SIM or network.
- **Avoid inflection traps:** do not put a child's name into a possessive or inflected sentence. Prefer "Temperature check: %1$s" over "%1$s's temperature". Add translator comments to every string with placeholders.
- **Exported text** (share-status message, email template, PDF summary) is generated in a selectable *document language*, which defaults to the app language.
- **Checks in CI/local:** treat lint `MissingTranslation` and `ExtraTranslation` as errors for enabled languages; run UI tests with a pseudolocale (`en-XA`) and an RTL pseudolocale (`ar-XB`), and with a German-length stress locale; screenshot-test the key screens in LTR and RTL.

## Milestones — work on ONE at a time

Before each milestone: post a short plan (files to touch, schema changes, risks) and wait for approval. After each: app builds, tests pass, short summary of what changed. Small commits.

1. **Foundation + profiles + localization base** — project setup, theme, navigation shell, Room schema v1, Child CRUD, onboarding (language/country, disclaimer, first child), string resources, per-app language, locale-aware formatting helpers. *Done when:* app launches, a child can be created/edited/deleted (soft), data survives restart, schema export is committed, the app runs in English, Spanish and German (placeholder translations flagged unreviewed) and in an RTL pseudolocale without clipped or hardcoded text.
2. **Temperature + timeline** — add temperature (value, method, time), timeline with edit/delete/undo, temperature display in °C/°F setting. *Done when:* logging takes ≤ 3 taps with defaults; invalid values are rejected; deleted entries can be restored from the snackbar.
3. **Symptoms + notes** — `SymptomLog` (stable symptom codes from `docs/features.md` §5, optional fluid intake and urination) and `Note`; the "Log symptoms" button on the temperature form; the "+" bottom-bar button opens the symptom screen directly **(ASSUMED — a "New entry" bottom sheet is the open alternative, owner to confirm)**; symptoms and notes appear in the timeline with filter chips and the same edit/delete/undo as temperature. *Done when:* symptom codes are stored language-independently and display in the current language; a symptom/note can be added in ≤ 3 taps, edited, deleted and restored from the snackbar.
4. **Medication + dose timer** — medication library, dose logging, next-allowed-dose timer, early-dose warning, duplicate-ingredient warning, wrong-child guard. *Done when:* unit tests cover interval logic incl. time-zone/DST changes; a dose can be corrected after saving.
5. **Notifications** — re-measure and "next dose allowed" reminders, exact-alarm permission flow, reschedule on reboot. *Done when:* works after process death and reboot; denied-permission path is handled.
6. **Photos** — attach to any record (camera/picker), private storage, compression, EXIF strip, thumbnails, full-screen viewer, delete-with-record. *Done when:* no photo is visible in the system gallery; size limit enforced; orphan files are cleaned up.
7. **Contacts** — doctor contacts, pinned country-based emergency number (from `emergency_numbers.json`), call (dial), email, SMS/WhatsApp intents, primary pediatrician per child. *Done when:* every action works with no extra permissions in default settings.
8. **Backup + share** — "share status" message, auto-backup to a user-chosen folder (Storage Access Framework), restore, last-backup indicator, failure notification, idempotent import. *Done when:* backup → wipe app data → restore round-trips with identical content incl. photos (automated test).

Later phases (episodes/charts, red flags, PDF, widget, sibling dashboard, fever-free counter, app lock) follow `docs/features.md` §16 — do not start them without being asked.

## Testing

- Unit tests (JUnit): temperature conversion/rounding, locale-tolerant number parsing, plural/duration formatting, dose-interval and duplicate-ingredient logic, DST/time-zone cases, import idempotency, backup round trip.
- Room migration tests for every schema bump.
- Compose UI tests for the quick-log flows (temperature, dose) and the undo path.
- No flaky sleeps; use test dispatchers/fake clock. Inject a `Clock` — never call `System.currentTimeMillis()` directly in logic.

## Working agreements

- Ask before: changing the schema, adding a dependency, adding a permission, or deviating from `docs/features.md`.
- When medical/legal content is unclear, stop and ask; don't guess.
- Keep this file current: when a rule or decision changes, update it in the same commit.
- Language: code, comments, commit messages and all documentation in English. English is the default UI language; other languages are translations.
