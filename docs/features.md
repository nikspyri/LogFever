# FeverLog — Feature Specification

Android app for logging fever in children. It works **entirely on the device, with no server and no account**.

Items marked **(ASSUMED)** were proposed, not confirmed by the owner.

## 1. Design principles

- **Local storage only.** No data leaves the phone unless the user sends it (share or export).
- **Fast at night.** One-handed logging, large buttons, dark theme, minimal steps.
- **Forgiving of mistakes.** Every record (time, dose, photo) can be corrected or undone easily.
- **No diagnosis.** The app records and reminds; it does not advise treatment.
- **English by default, multi-language by design.** All user-facing text is translatable and the app is built for many languages from the first milestone (see §13).

## 2. Child profiles

- Multiple children in the same app, with quick switching.
- **Siblings home screen:** when more than one child has an active fever episode, they appear side by side (or stacked) as cards showing the current/last temperature and measurement time, the last medication and its time, and the time until the next allowed dose. From each card the user logs directly for that specific child.
- A clear color marker and name (or profile photo) on every card and every log entry, so it is never unclear which child a record is for. The child's name is shown before a medication dose is saved.
- Name (or nickname), date of birth, optional weight, optional profile photo.
- Optional notes (e.g. allergies, or instructions from the pediatrician). They appear in the doctor summary.
- The child's age is used by the warning rules (e.g. infant under 3 months).

## 3. Temperature logging

- Value in °C or °F (the default unit follows the user's country; changeable), step 0.1.
- **Date and time of the measurement:** always shown (day and time). A **Now** button locks the current time into the record; **Change** opens a panel to set another day (up to 7 days back, never in the future) and time (hour, minutes in steps of 5). Any change also locks the time. This lets the user log a late entry that falls on a different calendar day. Stored as epoch millis plus the time zone ID, as in CLAUDE.md.
- **Measurement method:** rectal, oral, armpit, ear, forehead/non-contact. Stored with every reading.
- Color-coded status scale (normal, low-grade, fever, high fever), always paired with an icon and a text label.
- Optional note, **Log symptoms** and **Attach photo** reachable directly from the temperature form (e.g. a photo of the thermometer display).
- Warning for implausible values (e.g. 3.7 or 73), to catch typing mistakes.
- Decimal input accepts both "." and "," whatever the device language is.

## 4. Medication logging

- A medication list with the active ingredient and brand names (e.g. paracetamol/acetaminophen: Panadol, Tylenol; ibuprofen: Nurofen, Advil). Brand names differ by country and are entered by the user; the app ships no brand database.
- **Adding a medicine:** name as on the box, **active ingredient** (stable ingredient ID), **form** (syrup or pills), an optional **photo of the box**, the **dose printed on the label** (typed by the user, stored only as a reference, never suggested by the app) and the **minimum time between doses** (typed by the user from the label or the doctor). The label dose and the minimum interval start empty.
- **Dose logging:** the previous dose of the selected medicine is shown at the top; the medicine is chosen from a drop-down list with an "Add medicine" entry; the amount is entered as **ml or pills** (steps of 0.5, so half pills work); date and time use the same Now / Change control as temperature (section 3); optional notes; optional **next-dose reminder** (a switch that sets a notification at the dose time plus the medicine's minimum interval). Other units (mg, suppository) are to be decided.
- **Next-dose timer:** "Last dose given 2h 10m ago". The user sets the minimum interval per medication (defaults are proposed and must be confirmed by the user/doctor).
- **Early-dose warning** if the user tries to log a dose before the interval has passed.
- Warning for a duplicate active ingredient (e.g. two different products containing paracetamol). Ingredients are matched by a stable internal ID, not by display text.
- The app **does not calculate doses**. It records only what the parent gave.

## 5. Symptoms and notes

- Quick choices: cough, runny nose, vomiting, diarrhea, abdominal pain, rash, sore throat, headache, poor appetite, drowsiness/irritability.
- Fluid intake and urination (optional fields).
- Free-text notes.

## 6. Photos on entries

Ability to attach one or more photos to any record (temperature, medication, symptom, note).

**Typical uses**

- A rash or skin change, so the pediatrician can see how it evolves.
- The thermometer display, as proof of the reading.
- The medication package or label (product, strength).
- Anything else the parent wants to remember.

**Behavior**

- Capture with the system camera or choose from the gallery through the Android Photo Picker, so broad permissions are not needed.
- Photos are stored in the **app's private storage**, do not appear in the phone's gallery, and are never uploaded.
- Compression and resizing when saving, to avoid filling the storage.
- Metadata (EXIF, GPS location) is removed when saving.
- Thumbnails in the timeline; full-screen view with zoom.
- Side-by-side comparison of two photos (e.g. rash yesterday and today).
- Delete a single photo; when a record is deleted, its photos are deleted too.
- Optional protection of the photo viewer with device lock/biometrics.
- In the PDF export, photos are included **only if the user chooses**.

**Limits and risks to consider**

- The app does not analyze photos and gives no diagnosis.
- Photos of a child are sensitive personal data. That is why they stay local and are not included in backups unless explicitly chosen.
- Size limit per record and an overall storage indicator.

## 7. Timeline and chart

- Chronological list of all events (temperature, medication, symptom, photo).
- Temperature chart with a period selector **24 hours · 2 days · 3 days · 7 days**, shown on the Home screen inside the status card (a fuller episode chart comes in Phase 2). Medication doses are marked with a triangle and the measurement method is visually distinguished (Phase 2). The chart draws **no fever-threshold line** until a verified rule exists for the selected country, and has a text description for screen readers.
- "Fever episode": grouping of events with a start, an end and a duration.
- **"Fever-free for X hours" counter:** computed from the last reading above the fever threshold the user has set; shown on the child's card and in the summary. It takes medication into account: if a fever reducer was given in the last hours, the indicator notes that the value may be affected by the medication. The app does **not** state when a child may return to school or daycare and gives no instruction; it only shows the fact and the user decides based on the school's rules and the doctor.
- Edit and delete any record from the timeline, with undo.

## 8. Notifications and reminders

- Reminder for the next measurement ("measure again in 2 hours").
- "Next dose allowed" notification once the minimum interval has passed.
- Local notifications only (no push server).
- Settings for the Android exact-alarm permission and guidance on battery-optimization exemptions.

## 9. Safety alerts (red flags)

Shown when specific criteria are met, with a "Contact a doctor" message and a call button:

- Infant under 3 months with a fever of ≥ 38.0 °C.
- Very high fever.
- Fever lasting more than 3 days.
- Fever that does not come down despite fever reducers.

The exact thresholds must be sourced from a recognized authority (e.g. AAP, NICE, or the national pediatric society of the user's country) before they are implemented, and they may differ by country or guideline. They live in a single rules file with a source and a "verified" flag, and are adjustable by the user. Until verified, red-flag alerts that depend on them are not shown.

## 10. Doctor contacts and quick contact

A local contact book inside the app, so the parent has the numbers at hand when needed, especially at night.

**Contact details**

- Name and specialty (pediatrician, family doctor, ENT, dermatologist, hospital/emergency, on-duty pharmacy, other).
- One or more phone numbers (e.g. clinic, mobile, reception), with a label.
- Email, address and opening hours (optional).
- Notes (e.g. "book appointments through reception").
- A **"primary pediatrician"** marker per child, and favorite contacts at the top of the list.
- Add a new contact or pick one from the phone's contacts through the system contact picker, without needing permission to read all contacts.
- A pre-installed, non-deletable **emergency number** entry. The number depends on the country (e.g. 112 in the EU, 911 in the US and Canada, 999 in the UK, 000 in Australia). It is chosen from the country the user selects in onboarding and can be changed. The values live in a verified data file, not in the code.
- The app **ships no doctor database**: the user enters the details, so there are no outdated or wrong numbers.

**Quick actions**

- **One-tap call** from the list, from the child's screen and from the red-flag alerts. By default it opens the phone dialer with the number filled in and the user presses "call" (no permission needed). An optional setting enables truly direct calling, which requires the CALL_PHONE permission.
- **Send email** with a prefilled subject and body, through the phone's email app (no server or SMTP of our own).
- Optional: SMS or WhatsApp through the respective apps on the phone.
- Show on a map / navigate to the clinic or hospital (opens an external maps app).

**Email to a doctor**

- A "Send to doctor" button from a fever episode: prefilled text with the child's name/age, current and peak temperature, fever duration, medications with times, and symptoms.
- Attach the **PDF** summary and, if the user chooses, selected photos.
- The user sees and edits the message before it is sent; nothing is sent automatically.
- Warning before sending: email is usually unencrypted and this contains a minor's health data; the user decides consciously what to include.

**Integration with other features**

- In the red-flag alerts (§9), the "Contact a doctor" button opens the child's primary pediatrician and the emergency number.
- Contacts are included in the backup and in export/import (§11).
- Optionally, the primary pediatrician's name appears in the doctor summary (PDF).

## 11. Sharing and export (no server)

Because there is no sync, cooperation between parents happens through an explicit user action:

- **"Share status":** a ready-made message or image with the latest temperature and the latest dose (with time), sent through WhatsApp, Viber, SMS and so on.
- **Pediatrician summary (PDF):** episode, chart, medications, symptoms, optionally photos. The summary language can be chosen separately from the app language (useful when the doctor speaks a different language).
- **Data export** to CSV or JSON.
- **Automatic backup to a folder of the user's choice:** the user picks a folder through the Storage Access Framework (e.g. a folder synced with Google Drive or another service, or an SD card) and the app writes there periodically (e.g. daily and after each new episode). We run no server; the copy is entirely controlled by the user.
  - Contains the database, contacts and, optionally, photos (a separate choice because of size and sensitivity).
  - Keeps the last N copies and automatically deletes older ones.
  - A visible "last successful backup: ..." indicator and a notification if a backup fails or has not happened for a long time.
  - Optional password encryption. Note: without the password the backup cannot be restored.
  - Restore on a new phone from the same file.
- **Full backup (ZIP)** with the database, photos and doctor contacts, for moving to a new phone.
- **Import** of a backup or of records from another phone, with duplicate protection (unique ID per record).

## 12. Privacy and security

- No ads, no analytics, no identifiers.
- Optional app lock with PIN or biometrics.
- The database and photos live in the app's private storage; Android Auto Backup must be configured deliberately.
- A clear privacy policy and the data-safety declaration for Google Play.
- Disclaimer: "This app does not replace a doctor."

## 13. Languages and localization

The app is **English by default** and supports multiple languages. Every user-facing string is translatable from the first milestone.

**Languages (ASSUMED — owner to confirm)**

- **At launch:** English (default), Spanish, French, German, Portuguese (Brazil and Portugal), Italian, Greek.
- **Planned next:** Arabic (right-to-left), Simplified Chinese, Hindi, Russian, Turkish, Japanese.
- The architecture must support right-to-left layouts and non-Latin scripts from the start, even if those languages ship later.

**Behavior**

- The app follows the device language by default; the user can override it in Settings → Language (using the Android per-app language setting).
- If the device language is not supported, the app falls back to English.
- A language is offered in the picker only when all medical/safety strings in that language have been human-reviewed. Unreviewed languages are not shipped.

**What is localized**

- All UI text, notifications, error messages, the onboarding disclaimer, exported/shared text, email templates and the PDF summary.
- Dates, times (12/24-hour), numbers and decimal separators, durations ("2h 10m"), plurals.
- Default units: temperature (°C/°F) and weight (kg/lb) by country, both changeable.
- Country selection in onboarding (prefilled from the device region) drives the emergency number and default units.

**What is not translated**

- User-entered text (names, notes, medication brand names, contact details) stays exactly as typed.
- Stored data is language-independent: symptoms, measurement methods, units and ingredients are stored as stable codes and displayed in the current language, so records remain correct when the user changes language.

**Content that needs special care**

- Safety alerts, the disclaimer, the early-dose and duplicate-ingredient warnings, and the ingredient names are translated by qualified human translators and reviewed by a medical professional where possible. Machine translation may be used only for drafting non-safety UI text.
- Active-ingredient names differ by region (e.g. paracetamol vs acetaminophen) and are localized per language.
- Strings avoid inflection problems (e.g. use "Temperature check: Anna" instead of inserting a name into a possessive sentence).

## 14. User experience

- Dark theme and a dim night mode (three themes chosen in Settings: Light · Dark · Night).
- **Home layout:** quick buttons "Temperature" and "Dose", and a large red round "+" button centered in the bottom bar (between the 2nd and 3rd of the four tabs: Home · Timeline · Contacts · Settings) with a badge showing the total number of entries, for the other entries (symptom, note, photo). At the default font size the Home screen fits one phone screen without scrolling.
- Home-screen widget and quick tile for one-tap logging.
- Accessibility: large font sizes, good contrast, TalkBack.
- Layouts tolerate longer translated text (e.g. German) and mirror correctly in right-to-left languages.

## 15. Out of scope (deliberately)

- Per-kilogram dose calculation.
- Diagnosis, an AI "doctor", photo analysis.
- Cloud sync and user accounts.
- Ads and usage analytics.

## 16. Implementation phases

**MVP**

1. Child profiles.
2. Temperature and medication logging with timer.
3. Timeline with edit/undo.
4. Photos on records (capture/pick, private storage, compression, deletion).
5. Local notifications.
6. "Share status".
7. Doctor contacts with calling (and the country-based emergency number).
8. Automatic backup to a folder of the user's choice (database + contacts; photos optional).
9. Localization foundation: all strings in resources, English default, launch languages, per-app language setting, locale-aware formatting.

**Phase 2**

- Chart and fever episodes. *(Decision, proposed by Claude and accepted by the owner: the period-selectable Home chart stays in Phase 2. The MVP Home shows the status and medication cards without the chart; the mockup is simplified accordingly. Reasons: the chart is only meaningful with a verified fever threshold per country, and it is not needed for the quick-log goal.)*
- "Fever-free for X hours" counter.
- Siblings home screen (a card per child with an active episode).
- Red flags.
- PDF for the pediatrician and email to a doctor with attachments, with a selectable summary language.
- Export/import and ZIP backup.
- Widget and quick tile.
- App lock.
- Right-to-left and additional languages (Arabic, then the planned set).

**Phase 3 (optional)**

- Photo comparison.
- Bluetooth thermometer connection.
- Import/export through QR or file for manual merging between parents.

## 17. Open risks

- **Data loss:** without a server, if the phone is lost the history (and photos) are lost unless a copy exists. Mitigated by the automatic backup to a user folder (§11), but only if the user enables it; therefore the app asks for it to be set up on first use.
- **Storage space:** photos grow quickly; compression and a limit are required.
- **Notification reliability:** Android (especially on some manufacturers) can delay or kill background notifications.
- **Medical content:** the red-flag thresholds and medication intervals must come from a documented source and be reviewed by a pediatrician. Guidelines differ between countries, so rule sets may need to be per-region.
- **Medical translation:** a wrong or ambiguous translation of a safety message is a safety risk. Safety strings need qualified translators; languages ship only when reviewed.
- **Emergency numbers:** wrong or missing numbers for a country are dangerous; the data needs a verified source and a user-editable fallback.
- **Email with health data:** ordinary email is not encrypted; responsibility for sending lies with the user and it must be made clear to them.
- **Direct calling:** the CALL_PHONE permission is sensitive and may affect the Google Play declaration; that is why the default is the dialer.
- **Automatic backup:** writing to a user-chosen folder can fail if the folder is deleted or changed, or the sync service stops; failure notifications are required. If a cloud service syncs the folder, health data (and photos) leave the device at the user's responsibility; the file should be encrypted.
- **Fever-free counter:** risks being misread as medical or school guidance; it must be presented as a data indicator, with the effect of fever reducers flagged.
- **Wrong child:** with several children at once, logging a medication for the wrong child is dangerous; clear identification and confirmation are required.
- **Text expansion and scripts:** longer translations (German), right-to-left layouts and non-Latin scripts can break layouts and truncate safety text; they must be tested.
- **Regulation:** even with local storage, Google Play's requirements for health apps and the data-safety declaration apply.
