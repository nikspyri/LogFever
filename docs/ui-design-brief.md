# Log Fever — UI Design Brief for Figma

A document for whoever designs the app in Figma (a person or Claude). It describes what the app is, who it is for, what each screen contains, and **where you are free to take initiative**. The full feature specification is in `features.md`; if the two disagree, that file wins on *what* the app does and this one wins on *how* it looks.

---

## 1. The app in brief

**Log Fever** is an Android app for parents who want to log their child's fever: temperature, medication given and when, symptoms, photos (e.g. a rash), and to have the doctor's phone numbers at hand.

It works **entirely on the phone, with no account and no server**. Data never leaves the device unless the user sends it (message, email, PDF).

The app **does not diagnose, does not suggest doses and does not advise treatment**. It records, reminds, and helps the parent contact a doctor.

## 2. Who uses it and when

- **User:** a parent or caregiver, often worried and tired.
- **Critical moment:** 3 a.m., the child has a fever, the parent holds the child in one arm and the phone in the other hand, in a dark room.
- **Second moment:** the next morning at the pediatrician, where they want to show quickly what happened over the last hours.
- There are often **two or more children**.
- **Languages:** English by default, designed for many languages (see §5 and §10). Users read the app in their own language and in their own country's formats.

**The scenario the app must win:** open the app → log a temperature or a medication dose in **≤ 3 taps**, one-handed, without the user re-entering "which child", "what time" or "how much was the last dose".

## 3. Design principles

1. **Calm.** A gentle, reassuring tone. No red alarms without a reason, no panic in the copy. Even the safety warnings are clear and calm.
2. **Speed over beauty.** Defaults everywhere (time = now, last medication, active child). Few steps, big buttons.
3. **Dose safety.** Logging medication is the riskiest action: the child's name and the time of the previous dose must always be visible before saving.
4. **Forgiveness.** Every mistake is fixable: edit time/value, delete with undo. Correction must be easy to discover (negative example: a competing app where users could not find how to change a wrong medication time).
5. **At a glance.** The home screen answers three questions without scrolling: *What is the fever now? What medicine was given and when? When is the next dose allowed?*
6. **Visible privacy.** The user should understand that data stays on the phone (shown in onboarding and in settings).
7. **Never color alone.** Fever status is conveyed with an icon and text as well.
8. **Language-proof.** Layouts work in any language: longer text, different scripts, right-to-left. No text baked into images or icons.

## 4. Where you are free (and where not)

**Free — take initiative:**

- Visual identity: colors, typography, illustration, the app's name/logo, the app icon (original, no copying of existing ones).
- How temperature is entered: stepper, dial, value list, numeric keypad, or a combination. Propose and justify.
- Home screen layout, card style, how the chart is presented.
- Micro-interactions, animations, empty states, icons.
- Microcopy: propose real English text, not lorem ipsum.
- Extra screens or states you consider necessary (e.g. help, confirmations). Mark them "proposed".

**Binding (do not change without flagging):**

- The app is Android, with Material 3 as the base (you may adapt it, not ignore it).
- No feature that needs an account, a server, ads or an AI "doctor".
- No screen suggests a medication dose.
- Minimum touch targets ≥ 48dp, primary actions ≥ 56dp.
- Works in dark theme and with large fonts (up to 200%).
- Medical warning text is short, calm and does not sound like a diagnosis.
- Everything must work in multiple languages, including right-to-left (see §10).

## 5. Design system (for you to define)

Propose and document on the Foundations page in Figma:

- **Colors** as semantic tokens (not arbitrary hex): primary, surface, on-surface, error, and a **fever status scale** with 4 levels (normal, low-grade, fever, high fever). Each level has an icon and a label. Check WCAG AA contrast in light and dark. Add a separate "night mode" palette (low brightness, no bright white).
- **Typography** with full coverage for the launch languages (Latin including accents, Greek, and later Cyrillic, Arabic, Devanagari, CJK). Check accents, capitals and line heights. Large numerals for temperature.
- **Spacing, radius, elevation, icons** (one consistent set). Icons that imply direction (back, forward, send) must mirror in right-to-left; icons that do not (clock, phone, camera) must not.
- **Copy tone:** warm, short, reassuring. Avoid exclamation marks and panic words. Write strings so they survive translation: no sentences built by joining fragments, no idioms, no humor that depends on wordplay.

## 6. Navigation structure

- **Bottom navigation (4 tabs):** Home · Timeline · Contacts · Settings.
- **Primary action:** a large red round "+" button, centered in the bottom navigation bar between the 2nd and 3rd tab, with a badge showing the total number of entries logged (this child, or everyone on the siblings home). It sits above the bar and does not take a tab slot. Temperature and Dose have their own quick buttons on Home; "+" is for the other entries (Symptom · Note · Photo). *(ASSUMED: in the mockups "+" opens the symptom screen directly; whether it should open a "New entry" bottom sheet instead is for the owner to confirm.)* The red is a brand action colour, deliberately different from the fever status colours, and the button always carries the "+" icon and an accessible label ("Add entry, N entries logged in total").
- **Child selector** always visible at the top when there are several children (avatar + name).
- The proposed structure may change if you propose a better one, with justification.

---

## 7. Screens: what each one contains

For each screen, also design the **states**: normal, empty, loading, error, and where mentioned, warning.

### 7.1 Onboarding (first launch)

1. **Welcome:** what the app does, that everything stays on the phone, that it does not replace a doctor.
2. **Language and country:** language (prefilled from the device) and country (prefilled from the device region). Explain that country sets the emergency number and default units. Changeable later.
3. **Disclaimer:** short and clear, with explicit acceptance (an "I understand" button).
4. **First child:** name (or nickname), date of birth (or age), optional weight and photo.
5. **Backup setup:** choose a folder; explain "no server, if the phone is lost the data is lost". Can be skipped, with a reminder later.
6. **Notifications:** explain why they are needed before the system request appears. Can be skipped.
7. **Optional:** add a pediatrician (leads to the contacts screen).

### 7.2 Home (one child)

- Header: child name/avatar, child selector.
- **Current status card:** last temperature (very large), color + icon + status label, time and measurement method, a small trend indicator (rising/falling).
- **Medication card:** last medication and time ("2h 10m ago"), and the **time until the next allowed dose** per medication, with a clear "allowed now" / "in 1h 40m" indication.
- **"Fever-free for X hours" counter** (shown when applicable), with a note if the value may be affected by a recent fever reducer.
- **Temperature chart inside the status card**, with a period selector of four buttons: **24 h · 2 days · 3 days · 7 days** (default 24 h). Changing the period rescales the time axis and the points. Line with a ring per reading, the latest reading filled, medication doses marked with a triangle marker, a legend in text, peak value shown, and a text description for screen readers. **No fever-threshold line** is drawn until a verified rule exists for the selected country (see §14). There is no separate chart screen at this stage; the fuller episode chart stays in 7.11 (Phase 2).
- **Quick actions** (large buttons, Figma-style quick log): "Temperature" and "Dose". Calling a doctor lives in the Contacts tab.
- The "+" button in the bottom bar (see §6) and the entry count. The recent-entries list is not on Home in the current mockups; the Timeline tab covers it.
- **Fit:** at the default font scale the Home screen must fit one phone screen (844 dp tall reference) **without scrolling**; the mockup budget is about 780 dp of content plus the bar. At large font scales scrolling is acceptable.
- Any active safety alerts (see 7.12).
- *Empty state:* "No entries yet" with one large "Log a temperature" button.

### 7.3 Home (several children with an active episode)

- A card per child, stacked (or side by side on a large screen). Each card has the child's name/avatar/color, current/last temperature and time, last medication and time until the next dose, and its own quick buttons.
- The child's name is **always visible and distinct** (color + avatar) to avoid logging for the wrong child.
- Children without an active episode appear collapsed in a lower section.

### 7.4 "New entry" bottom sheet

Five large options with icon and label: Temperature · Medication · Symptom · Note · Photo. An indication of which child the entry is for (changeable).

### 7.5 New temperature

- Child (prominent, changeable).
- **Temperature value:** a large input element (propose the method), step 0.1, default value = the last one. Immediate status indication (color + text) as the value changes. Warning for implausible values (e.g. 3.7 or 73). Accepts both "." and "," as the decimal separator; displays in the locale's format.
- **Measurement method** (chips): rectal, oral, armpit, ear, forehead/non-contact. Remembers the last choice.
- **Date and time row:** always shows the day and the time (e.g. "Today 7 Oct" and a large "14:45") with a status line. Two buttons: **Now** locks the current time into the record (status "Time locked"; before it is pressed the line says "Not locked yet"), and **Change** opens a bottom panel with a day selector (‹ ›, up to 7 days back, future dates not allowed) and hour and minute steppers (−/+, minutes in steps of 5), plus "Set to now and lock" and "Done". Any change also locks the time. The date matters because a late entry can fall on another calendar day (e.g. logging at 00:10 for 23:50). The earlier "10 / 20 / 30 minutes ago" chips are dropped.
- **Log symptoms** (opens the symptom screen) and **Attach photo** (e.g. of the thermometer display) as two buttons under the time row.
- "Save" button (large, fixed at the bottom, reachable with the thumb).
- After saving: return to home with a snackbar "Saved · Undo".
- *Red-flag path:* if the value or the child's profile meets a safety criterion, the alert screen/card (7.12) appears instead of a plain snackbar.

### 7.6 New medication (dose)

- **Child:** very prominent, with avatar, so the user sees it before saving.
Order of the screen, top to bottom (mockup):

1. **Child** (name and initial, top right of the header, plus the name again on the Save button).
2. **Previous dose card, at the top:** the last dose of the selected medicine, for example "7.5 ml · today 12:50", "1 h 55 min ago · recorded by you", labelled with the active ingredient. It updates when another medicine is selected. Because the card shows the *selected medicine's* last dose, the duplicate-ingredient check (below) must additionally look across all products that share the same active ingredient.
3. **Medicine selector:** a field with a down arrow that opens a list of the user's medicines (name, active ingredient, a tick on the selected one) and, at the end, **"+ Add medicine"**, which opens the Add medicine screen (7.17).
4. **Amount given:** large value with −/+ buttons in 0.5 steps and a unit switch **ml / Pills** (the label reads "pill" for 1 and "pills" otherwise; half pills are allowed). The app never suggests an amount; it may prefill the last amount recorded for that medicine, and a line says "Check the medicine label for the right amount". Further units (mg, suppository) are not designed yet and need the owner's decision.
5. **Date and time given:** the same row and Now / Change panel as in 7.5.
6. **Notes** (optional text field).
7. **Next-dose reminder (optional):** a switch "Remind me for the next dose". When on, it shows the reminder time (dose time plus the medicine's minimum interval, "next day" when it passes midnight). When off: "No notification will be sent." Turning it on requests the notification permission in context (see CLAUDE.md permissions policy).
8. **Save dose for [child]** button, with an Undo snackbar afterwards.

Warnings are **deliberately not designed yet** and will be handled in a later pass: the early-dose warning (when the minimum interval has not passed; calm, "Cancel" as the default, "Log anyway" secondary) and the duplicate active-ingredient warning. The mockups contain one early-dose screen as a placeholder for the later work; in the prototype, Save on Paracetamol leads to it. Both warnings remain mandatory per CLAUDE.md rule 5.

### 7.7 New symptom / New note

- **Symptom:** multi-select chips (cough, runny nose, vomiting, diarrhea, abdominal pain, rash, sore throat, headache, poor appetite, drowsiness/irritability), optional fluid/urine fields, time, note, photo.
- **Note:** free text, time, photo.

### 7.8 Photos

- **Attach:** a "Photo" button on every entry form with "Camera" and "Gallery" options. Thumbnails of the attachment, removable before saving.
- **Full-screen viewer:** zoom, navigation between the record's photos, date/time, link to the record, delete button (with confirmation).
- **Child/episode photo gallery:** a chronological grid, filter by episode.
- **Side-by-side comparison** of two photos (Phase 3): design it as a proposal.
- An indication that photos stay private in the app.

### 7.9 Timeline

- Grouped by day, with labels ("Today", "Yesterday", date).
- Each entry: type icon, key details (temperature value with color, or medication and dose, or symptom), time, photo thumbnail if any, a one-line note.
- Filters (chips): All · Temperature · Medication · Symptoms · Photos · Notes. Period selection. Child switching.
- **Easily discoverable edit/delete:** tapping an entry opens a detail with clear "Edit" and "Delete". Delete → "Undo" snackbar.
- *Empty state:* a friendly message and a log button.

### 7.10 Entry detail / edit

All fields of the entry editable (value, method, time, medication, dose, note, photos). A visible "edited" indicator. Delete button.

### 7.11 Chart / Fever episode (Phase 2)

- Temperature chart with a 24 hours / 3 days / 7 days selector.
- Medication doses marked on the chart (icon or label per medication).
- A different marker shape/style per **measurement method** (they are not directly comparable).
- Horizontal status bands (with text, not only color).
- Tooltip/panel with details when the user touches a point.
- Episode summary: start, duration, peak temperature, medications given, the "fever-free" counter.
- Accessible alternative: a table of values for TalkBack.
- In right-to-left languages, decide with a native reviewer whether the time axis mirrors; document the decision.

### 7.12 Safety alert (red flag)

- A calm but clearly visible card or full-width screen, not an intimidating popup.
- A message such as "A reading like this in an infant of this age needs contact with a doctor" (the exact text and criteria come from a verified source, not from the designer; use placeholder text marked "PLACEHOLDER").
- Primary actions: **"Call pediatrician"** (the child's primary pediatrician) and **"Call emergency number"** (the country's number).
- Secondary: "Send summary to doctor", "Dismiss".
- No diagnosis, no treatment instructions.

### 7.13 Doctor contacts

- **List:** favorites/primary pediatrician at the top, grouped by specialty. The list always contains a permanent, non-deletable **emergency number** card (the number depends on the selected country).
- Each row: name, specialty, main number, **quick-action buttons** (Call · Email).
- **Contact detail:** all phone numbers with labels (clinic/mobile/reception), email, address (with "Navigate"), opening hours, notes. Actions: Call, Email, SMS/WhatsApp, Navigate.
- **Add/edit:** a form, pick from the phone's contacts, set "primary pediatrician" per child.
- **Email to a doctor:** a preview screen with prefilled text (child, age, temperatures, medications with times, symptoms), attachment options (PDF summary, selected photos), a warning about unencrypted email, and an "Open in email app" button.
- *Empty state:* "Add your pediatrician so you always have them at hand" and an add button.

### 7.14 Share status

A preview screen of the message or image ("Last temperature 38.4 at 03:10 · Given X at 02:50") with a "Share" button (opens the Android share sheet). Content options (with/without the child's name).

### 7.15 Pediatrician summary (PDF, Phase 2)

A PDF page preview: child details, episode, chart, medication table, symptoms, optionally photos. Content options before generation, and a **summary language** selector separate from the app language. Share/print.

### 7.16 Child profiles

A list of children, add/edit (name, date of birth, weight, photo/color, notes such as allergies or doctor instructions, primary pediatrician), archive/delete a child with a clear warning about what is lost.

### 7.17 Medication library

The user's medication list: active ingredient, brand names, form, minimum interval between doses, default unit. Add/edit. The user confirms the interval; no dose recommendations appear.

**Add medicine** (opened from "+ Add medicine" in the dose screen). Fields, top to bottom:

- **Name on the box** (free text, the brand name as printed).
- **Active ingredient:** a dropdown (Paracetamol, Ibuprofen, "Other…"). It is stored as a stable ingredient ID, never as display text, because the duplicate-ingredient warning depends on it. "Other…" needs its own flow (design later).
- **Form:** **Syrup** or **Pills**. It sets the unit used for the label dose (ml or pills).
- **Photo of the box** (optional): Take photo / Retake / Remove. It follows the photo rules in features §6 (private storage, EXIF removed) and the screen says it stays on the phone.
- **Dose on the label:** the user types what the label or the doctor says, with −/+ in 0.5 steps. It is stored only as a *reference*; the text says the app never suggests a dose.
- **Minimum time between doses:** hours, −/+ in 1 h steps. It comes from the label or the doctor and drives the next-dose timer and the reminder.
- **Save medicine.**

The values visible in the mockup (5 ml, 1 pill, 6 h) are samples. In the app the label dose and the minimum interval start **empty** and are entered by the user, because a pre-filled number would act as a dose recommendation. The dose-interval rules in `safety_rules.json` are separate and are used only for checks, per CLAUDE.md rule 1.

### 7.18 Settings

- Children and profiles.
- Units (°C/°F, kg/lb).
- Country (emergency number, default units).
- **Fever threshold** (adjustable).
- Notifications (types, sound, battery-optimization exemptions, exact-alarm permission with guidance).
- Theme: light / dark / automatic / night mode.
- **Language** (see 7.24).
- App lock (PIN/biometrics).
- Calling: "Open dialer" (default) or "Direct call".
- **Backup** (see 7.19) and export.
- Privacy: what is stored and where.
- About / Disclaimer / Version.

### 7.19 Backup and export

- **Status:** "Last successful backup: yesterday 22:10" with a green/warning indicator, destination folder.
- Setup: choose a folder, frequency, number of copies kept, include photos (with a size estimate), optional password encryption (with a clear warning "without the password it cannot be restored").
- **Restore** from a file, with a confirmation screen and a summary (how many children/records).
- Export CSV/JSON.
- *Error state:* backup failure with a clear cause and a corrective action.

### 7.20 App lock

A PIN/biometric screen, calm, which does not reveal content in the recent-apps view.

### 7.21 System notifications

Design the look and actions:
- "Next dose of [medication] is allowed for [child name]" (actions: "Log dose" · "Later").
- "Time to measure [child name]'s temperature again" (action: "Log"). Prefer phrasing that avoids possessives, e.g. "Temperature check: [child name]", because possessives do not translate well.
- Backup failure.
The child's name must appear in notifications when there are several children; provide an option to hide it on the lock screen.

### 7.22 Widget and Quick Tile (Phase 2)

- **Home-screen widget:** current temperature, time until the next dose, quick-log buttons. Sizes 2×2 and 4×2.
- **Quick tile** in quick settings to open a new entry.

### 7.23 Permissions, errors and special states

Design screens or dialogs for: notifications denied, exact alarms denied, storage full, camera failure, corrupted backup file, device time that looks wrong.

### 7.24 Language and region

- **Language picker:** a list of supported languages shown in their own names (English, Español, Français, Deutsch, Português, Italiano, Ελληνικά, and later العربية, 中文, हिन्दी, Русский, Türkçe, 日本語), with "System default" at the top. A short note that the app will restart the screen to apply it.
- **Country picker:** the country for emergency numbers and default units, with the resulting emergency number displayed so the user can verify it.
- **Formats preview:** a small example showing how date, time, number and temperature will look.
- States: a language whose safety text has not been reviewed is not listed.

---

## 8. Key flows to prototype

1. **Night-time dose (the most important):** Home → "Medication" → confirm child and time since the previous dose → Save → Undo. Count the taps.
2. **Early dose:** the same path but with the early-dose warning.
3. **Red flag:** new temperature in a small infant → alert → call pediatrician.
4. **Fixing a mistake:** Timeline → entry → change time → save; and delete → Undo.
5. **Siblings:** Home with two children → log for the second child.
6. **Sending to the doctor:** episode → "Send summary" → email preview with attachments.
7. **Rash photo:** new symptom → photo → appears in the timeline and full-screen view.
8. **Onboarding → first log.**
9. **Change language:** Settings → Language → pick one → the same screen in the new language, including a right-to-left language.

## 9. Accessibility and context of use

- WCAG AA contrast at least, in both theme modes.
- Test every screen at 130% and 200% font size.
- TalkBack labels on every icon/button; a table alternative for charts.
- One-handed use: primary actions in the lower part of the screen.
- Do not rely on gestures only (swipe to delete) without a visible alternative.

## 10. Localization and internationalization

**Languages (ASSUMED):** launch with English (default), Spanish, French, German, Portuguese, Italian, Greek. Plan for Arabic (right-to-left), Simplified Chinese, Hindi, Russian, Turkish and Japanese.

**Design rules**

- **Text expansion.** Assume text can be 30–40% longer than English (German is the usual stress test) and design buttons, chips, tabs and cards to wrap or grow without truncating safety-relevant text. Never use fixed-width labels.
- **Right-to-left.** Provide mirrored frames for the key screens. Use start/end rather than left/right. Mirror directional icons and navigation, not clocks, camera or phone icons.
- **Scripts.** Check line height and font fallback for Arabic, Devanagari and CJK; make sure large temperature numerals look right in each script's numerals if the locale uses them.
- **Numbers and formats.** Show examples with locale-specific decimal separators (38.4 vs 38,4), 12/24-hour time, and date order. The temperature input must accept both "." and ",".
- **Plurals and durations.** Languages have different plural rules (some have more than two forms). Do not build strings like "2 hours" by concatenation; design the slot so the whole phrase can change.
- **Names in sentences.** Avoid sentences where a child's name is inflected or possessive; prefer constructions like "Temperature check: Anna".
- **No text in images.** Illustrations, icons and onboarding art must contain no words.
- **Region-dependent content.** The emergency number, default units and medication brand names vary by country; never hardcode one country's values into a design as if universal.

## 11. Deliverables in Figma

**File structure (pages):**

1. **Cover & Readme:** description, version, assumptions, open questions.
2. **Foundations:** colors (semantic tokens, light/dark/night), typography, spacing, radius, icons, copy tone.
3. **Components:** buttons, chips, temperature input fields, cards (status, medication, child), timeline row, bottom sheet, snackbar with undo, warning dialogs, child avatar, photo thumbnail. With variants and states (default, pressed, disabled, error).
4. **Screens — Light** and **Screens — Dark/Night:** all screens in section 7, with a fixed Android frame (e.g. 360×800 dp) and an extra frame at large font size for the 5 most critical screens.
5. **Localization:** the same key screens with a long-text stress test (German-length strings), a right-to-left version (Arabic), and a CJK/Devanagari check; the language picker and country picker.
6. **Flows / Prototype:** the flows in section 8, linked.
7. **Handoff:** notes for the developer (spacing, tokens, behavior, states, references to `features.md`).

**Rules:**

- Name layers with a logical naming scheme (e.g. `Home/ChildCard/Active`).
- Use auto layout and responsive behavior (two widths: compact and larger).
- Use real English text and realistic data; no lorem ipsum. Keep all strings in one place (a text style/variable set) so they can be exported for translation.
- State explicitly what is **your own proposal** beyond this document and what is **placeholder medical text** awaiting verification.

## 12. Questions I expect you to propose answers to

1. What is the best way to enter temperature one-handed at night?
2. How do we show the time until the next dose so it is not misread as a "dose recommendation"?
3. How do we visually distinguish the children so a record is never logged for the wrong child?
4. How can onboarding be short without skipping the backup?
5. What is the right intensity for warnings (early dose, red flag) without causing panic?
6. What name and visual identity suit an app that must inspire calm and trust, in many languages and cultures?
7. How should the layout and chart behave in right-to-left languages?

## 13. What we do not want

- Gamification, rewards, streaks.
- Ads, social features, user-behavior analytics.
- An AI "advisor" or any screen that diagnoses or suggests a dose.
- Frightening illustration, or overly "childish" illustration that talks down to the parent.
- Copying the look of existing apps or of protected characters/logos.
- Anything that works in only one language or one country.

## 14. Reference mockups and placeholders (read before implementing)

A first set of 12 screens exists as a design canvas: Home (with the period-selectable chart), Log temperature, Log dose, Add medicine, Dose early warning (placeholder), Timeline, Contacts, Onboarding, Settings, Log symptoms, Home with two children, Home in night mode. Use them as a **layout and tone reference**, not as a spec. They are incomplete: the note form, the photo viewer, the plain dark theme, the "Other…" ingredient flow, the final dose warnings and several flows are not drawn. The screens that did change in this pass, and what changed, are described in sections 6, 7.2, 7.5, 7.6 and 7.17.

Values in the mockups that are **placeholders and must never be copied into code or strings as facts**:

- The fever threshold behind the "Fever range" / "Below fever range" label (the mockup uses 38.0 °C). The real value comes from `safety_rules.json` for the selected country. While the rule is `"verified": false`, do not show a fever status label at all.
- The dose interval ("6 h", "next dose allowed from 18:50") and the early-dose warning wording. The interval comes from the rules or the user's own medication setting. Warning text is a `safety_` string and needs human review.
- Emergency number and country (`[NUMBER]`, `[COUNTRY]`): only from `emergency_numbers.json`.
- All sample data: names (Maya, Leo), temperatures, times, medicine names and amounts, doctor details.
- The symptom list in the symptom screen: final codes live in `docs/features.md`, not in the mockup.

Binding behaviours shown in the mockups: child name visible on every logging form; "Cancel" is the default action on the early-dose warning; each child has a distinct colour plus initial plus name; status is always icon plus words; minimum text 13 px and touch targets 56 px; calm copy with the "does not replace a doctor" line.

More mockup values that are samples or open decisions:

- All numbers in the chart (readings, peak, the four periods) and the entry counts on the "+" button are sample data.
- Home and the siblings home in the mockup show a "Fever range" label. As above, hide it while the rule is unverified; the chart then has no status colouring and no threshold line.
- Night mode appears as its own screen in the mockup. In the app it is a theme (Light · Dark · Night, chosen in Settings), not a separate screen. Its chart is static in the mockup but interactive in the app.
- The siblings home has no chart in the mockup. Whether each child's card opens a chart is open.
- The "+" button opens the symptom screen in the prototype; the final behaviour (direct, or a "New entry" sheet) is for the owner to confirm.

Interaction details worth keeping: Now / Change time handling and the no-future-date rule (7.5), the medicine dropdown with "+ Add medicine" (7.6), the pill / ml switch and 0.5 steps (7.6), the 24 h / 2 days / 3 days / 7 days selector (7.2).
