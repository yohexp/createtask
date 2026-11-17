# QuickNote

A small offline notes app built with Java, XML layouts, and native Material 3 Expressive components. No Jetpack Compose, database, network, or login.

## Build and run

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Run the `app` configuration on an Android 6.0+ device or emulator.

Command line:

```bash
export JAVA_HOME=/opt/android-studio/jbr
./gradlew assembleDebug
```

The project uses Gradle 9.5 and AGP 9.3.1. Android Studio’s embedded JDK is a full JDK; the system Java 21 runtime in this environment does not include `javac`.

APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## AP CSP Create Task mapping

| Requirement | Where it lives |
|---|---|
| Input | Title/body fields and category chips in `NoteEditorSheet`; search query in `SearchView` |
| Output | Note list, note count subtitle, empty states, Snackbar messages |
| List | `ArrayList<Note> notes` in `NoteList` |
| List storage | `notes.add(0, note)` in `NoteList.add`; JSON save/load in `NoteStore` |
| List use | `filterNotes(notes, query)` and `notes.get(i)` in `NoteList` |
| Procedure | `NoteList.filterNotes(ArrayList<Note> notes, String query)` |
| Parameters | The note list and the query string |
| Sequencing | Creates results list, normalizes query, loops, adds matches, returns results |
| Selection | `if` checks title, body, and category |
| Iteration | `for` loop over the note list |
| Procedure call | `return filterNotes(notes, query);` in `NoteList.search` |
| Purpose | Fast offline note capture and retrieval |

## PPR screenshot targets

- **List:** `NoteList.java` containing `private final ArrayList<Note> notes`, `notes.add(0, note)`, `notes.get(i)`, and the search call.
- **Procedure:** `filterNotes` in `NoteList.java` plus the call in `search` directly above it.
- Keep comments out of those screenshots. The procedure and list code contain no comments.

## Video flow (under 60 seconds)

1. Tap the FAB.
2. Enter a title, body, and category.
3. Save and show the note card, count, and confirmation.
4. Open search, type a matching word, and show the filtered result.
5. Close search.

Do not show names, school names, or other identifying information in the video.
