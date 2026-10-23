# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/Xiangbibi/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | Eclipse Temurin 25.0.5+8-LTS |
| Maven | Apache Maven 3.10.0  |
| IDE | Visual Studio Code 1.141.0 |
| SonarQube for IDE | 6.1.1 |
| AI tool and exact model | Gemini 3.8 Flash |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 79dade3

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 2 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 3 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 4 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |
| 5 | java:S2143 | ParcelService.java:1 | Use the "java.time" API for date and time. |
| 6 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 7 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 8 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 9 | java:S4973 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 10 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 11 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 12 | java:S2095 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 13 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 2026-10-23 |
| AI tool and exact model | Gemini, 3.8 Flash |
| Commit reviewed | 79dade3 |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | 	attached the files |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | Main.java:8 | Scanner `in` is never closed | AI | Resource leak (also shown by the Java extension) | yes, no close() call or try-with-resources |
| 2 | ParcelService.java:6 | Unused import java.util.Date | both | java:S1128 | yes, nothing in the file uses Date |
| 3 | ParcelService.java:12 | Unused private field maxParcels | both | java:S1068 | yes, nothing reads it |
| 4 | ParcelService.java:42 | Strings compared with == instead of equals() | both | java:S4973 | yes, it made `pickup P1 5` print Fee: 0.0 and leave the parcel STORED |
| 5 | ParcelService.java:76 | FileWriter is never closed | both | java:S2095 | yes, parcels.csv was empty after running |
| 6 | ParcelService.java:80 | Empty catch block hides IOException | both | java:S108 | yes, the block has no code or comment |
| 7 | ParcelService.java:28, 68 | String literal "STORED" repeated | AI | Code smell (repeated literal), status should be an enum | no, it appears only twice, and the AI gave wrong lines (27, 65) |
| 8 | Main.java:6-27 | main is too long and complex | AI | Long method | no, it is only about 20 lines of simple if-else dispatch |
| 9 | ParcelService.java:24-37 | storeParcel does several jobs in sequence | AI | Procedural cohesion | yes, it finds a locker, sets status, sends SMS and saves the file |
| 10 | ParcelService.java:39-50 | pickUp does several jobs in sequence | AI | Procedural cohesion | yes, it finds the locker, changes status, saves the file and calculates the fee |
| 11 | ParcelService.java:9-83 | One class manages lockers, fees, console output and CSV saving | AI | Weak class cohesion | yes, these are four different responsibilities |
| 12 | ParcelService.java:52 | fee uses no data of ParcelService | AI | Weak class cohesion (feature envy) | yes, it uses only the parcel size and days |
| 13 | ParcelService.java:24 | storeParcel takes a whole Parcel (stamp coupling) | AI | Stamp coupling | no, the method uses size, code, student ID and status, so passing the object is reasonable |
| 14 | ParcelService.java:52 | fee takes a whole Parcel but uses only getSize() | AI | Stamp coupling | yes, an int size would be enough |
| 15 | ParcelService.java:64 | Boolean flag forStaff changes what printParcels does | AI | Control coupling | yes, the flag selects two different outputs |
| 16 | Main.java:22 | Main reaches through service.getLockers() to count free lockers | AI | Strong coupling (reaching through an object) | yes, ParcelService could offer a countFreeLockers() method |
| 17 | Locker.java:5, Parcel.java:6, ParcelService.java:56-61 | Size codes 1, 2, 3 must be read the same way in three classes | AI | Connascence of convention | yes, nothing in the code names the codes |
| 18 | Parcel.java:7 | Field status is public | AI | Non-private field | yes, ParcelService changes it directly |
| 19 | ParcelService.java:20 | getLockers() returns the internal list | AI | Exposed internal collection | yes, callers can add or remove lockers |
| 20 | Main.java:9, 15, 17, 22; ParcelService.java:30, 67, 69 | System.out used instead of a logger (7 places) | SonarQube | java:S106 | yes, but low importance for a command-line program |
| 21 | ParcelService.java:1 | Use java.time instead of Date | SonarQube | java:S2143 | yes, but it only follows from the unused Date import |


2.1 Name two problems that only the AI review found.
(1) ParcelService has weak class cohesion (row 11): it manages lockers, calculates fees, prints to the console and writes the CSV file. (2) The size codes 1, 2 and 3 are connascence of convention (row 17): Parcel, Locker and ParcelService must all read the numbers in the same way, and nothing in the code states the meaning. SonarQube reported neither of them, because they are design problems and not code-quality rules.

2.2 Name one problem that only SonarQube found, or write none.
java:S106, "Replace this use of System.out by a logger", which SonarQube reported 7 times in Main.java and ParcelService.java. The AI review did not mention it.
2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.
Finding 13 said that storeParcel has stamp coupling because it takes a whole Parcel. This is not a real problem, because the method uses the size, the code, the student ID and the status of the parcel, so it needs almost the whole object. I checked each AI finding by opening the file at the line that the AI gave and comparing it with the code. I also compared the findings with the SonarQube results and with the real output of the program (Fee: 0.0, status still STORED, empty parcels.csv). Many line numbers given by the AI were wrong by 1 to 5 lines, so I used the real line numbers in the table.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Kept | Printing to the console is the output of a command-line program. |
| 2 | Kept | Printing to the console is the output of a command-line program. |
| 3 | Kept | Printing to the console is the output of a command-line program. |
| 4 | Kept | Printing to the console is the output of a command-line program. |
| 5 | Fixed | 46eec60 |
| 6 | Fixed | 46eec60 |
| 7 | Fixed | 4b08f5e |
| 8 | Kept | Printing to the console is the output of a command-line program. |
| 9 | Fixed | 0ce2fc6 |
| 10 | Kept | Printing to the console is the output of a command-line program. |
| 11 | Kept | Printing to the console is the output of a command-line program. |
| 12 | Fixed | 73ec65e |
| 13 | Fixed | 73ec65e |

Number of SonarQube issues that remain after your fixes: 7
TODO

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Non-private field (weak encapsulation): Parcel.status was public | Made status private in Parcel.java and added getStatus() and setStatus(); ParcelService now uses them | 0248ccb |
| Control coupling: the boolean flag forStaff in printParcels decided what the method did | Split printParcels(boolean) into printStaffParcels() and printWaitingParcels(), and changed the two calls in Main.java | 1297e0e |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
store P1 6501 2
SMS to 6501: parcel P1 is in locker 2
Locker 2
pickup P1 5
Fee: 30.0
staff
P1 6501 PICKED_UP
Free lockers: 3
quit

parcels.csv:
P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

1. Files read: `bikeshare-good/src/main/java/com/bikeshare/App.java`, `Bike.java`, `BikeShareService.java`, `CliMenu.java`, `CsvRideRepository.java`, `MemberType.java`, `Rental.java`, `RideRepository.java`, `Station.java`.

2.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | Check 1 | App.java:10 | The `Scanner` on System.in is never closed (resource leak warning). | Note | Wrap it in try-with-resources, or keep it open on purpose and add a comment. |
| 2 | Check 1 | CsvRideRepository.java:27 | The member ID typed by the user is written to rides.csv without escaping, so an ID with a comma or a new line breaks the columns of the file (probable bug). | Should fix | Reject or quote IDs that contain a comma, a quote or a new line. |
| 3 | Check 1 | CliMenu.java:43, 46, 69 | A wrong station or member type makes `valueOf` fail, and the catch blocks at lines 51 and 81 show Java's raw message ("No enum constant com.bikeshare.Station.X") to the user. | Note | Catch the error at each `valueOf` and print "Unknown station" or "Unknown member type" with the allowed values. |
| 4 | Check 1 | CliMenu.java:40; BikeShareService.java:31 | An empty member ID is accepted, so a rental can be stored under an empty string. | Note | Reject a blank member ID in `rentBike`. |
| 5 | Check 2 | RideRepository.java:6; BikeShareService.java:56-64; CsvRideRepository.java:18 | `saveRide` takes seven positional parameters, so a change of order or count must be made in three places (connascence of position). The member type and the stations are passed as Strings through `name()`, which drops the enum type (connascence of type). | Should fix | Pass one `Ride` record instead of seven loose values. |
| 6 | Check 2 | BikeShareService.java:54, 57, 59, 62 | The service reaches through `Rental` with `rental.getBike().getId()` and `rental.getMemberType().calculateFee(...)`, which is strong coupling. | Note | Add `getBikeId()` and `calculateFee()` to `Rental`. |
| 7 | Check 2 | CliMenu.java:62, 74 | The menu fetches the whole `Rental` (stamp coupling) only to read the bike ID through `rental.getBike().getId()`. It checks for an active rental although `returnBike` checks it again (BikeShareService.java:48-51), and it depends on five classes (BikeShareService, Rental, Bike, MemberType, Station). | Should fix | Let `returnBike` return a small result with the bike ID and the fee, so the menu needs neither `getActiveRental` nor `Rental`. |
| 8 | Check 2 | CliMenu.java:42, 45, 68 | Station names and member type names are typed by hand in the prompts ("LIBRARY / CANTEEN / DORM", "STUDENT / STAFF") and repeat the enums, so a new station or member type also needs these strings edited (connascence of convention). | Should fix | Build the prompt text from `Station.values()` and `MemberType.values()`. |
| 9 | Check 2 | RideRepository.java:3, 6; CliMenu.java:82-83 | The interface declares `IOException`, so the file detail leaks to `returnBike` (BikeShareService.java:44) and to the menu. The menu message hard-codes "rides.csv" although App.java:8 chooses the file name (connascence of meaning). | Note | Wrap the error in an own exception and let the repository name the file. |

3. Number of findings: Check 1: 4. Check 2: 5.

Not found: methods with procedural or weaker cohesion; classes with weak class cohesion; fields that are not private; operations that return an internal collection; unused imports, fields or parameters; empty blocks; methods that are too long (the longest, `handleReturn`, has 29 lines); common or content coupling.

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.
Answer: No. SonarQube reported only code-quality issues in bikeshare-poor and none of them is about cohesion or coupling. The poor design (one class that holds the menu, the fee rules, the bike records and the file saving, public static fields, int codes, a boolean flag) is not reported by any rule.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.
Answer: Yes, the AI review told the two versions apart.  In bikeshare-poor it reported weak class cohesion, because one class holds the menu, the fee rules, the bike records and the file saving. For bikeshare-good it reported no weak cohesion and no non-private field, and its findings were small points such as the seven positional parameters of saveRide.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why? 
Answer: I would rather extend bikeshare-good. To add a visitor who pays 4 baht per minute, I only add one constant to the enum, VISITOR(4), in MemberType.java. The fee rule already lives in MemberType.calculateFee, and the menu and the service need no change except the text of the prompt. In bikeshare-poor the member type is an int code and the fee rules, the menu and the file saving are in one class, so I would have to find every place that tests the code (the menu, the fee calculation and the saving), add a new number, and check that I did not miss one. The good design keeps the fee rule in one place, so a new member type is a small change with a low chance of breaking other code.

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.
Answer: SonarQube checks code-quality rules on one file at a time, such as a string compared with == (java:S4973), a FileWriter that is never closed (java:S2095), an unused import (java:S1128) and an empty catch block (java:S108). It does not check design quality: it did not report that ParcelService has weak class cohesion, that the size codes 1, 2 and 3 are shared by three classes, or that bikeshare-poor puts everything in one class. It also does not run the program, so only my test run showed that the fee was 0.0 and parcels.csv was empty.

5.2 When should you not trust the AI review? Give an example from this lab.
Answer: I should not trust the AI review without checking it, because it gave wrong line numbers (often 1 to 5 lines off) and some findings that were wrong or too vague. For example, it called the whole Parcel parameter of storeParcel stamp coupling, but the method uses the size, code, student ID and status. It also said that the string "STORED" is repeated many times, but it appears only twice. I trust a finding only after I open the file at the line and compare it with the code, the SonarQube result or the output of the program.

5.3 What will you do differently in Project Phase 2 after this lab?
Answer: In Project Phase 2 I will run SonarQube while I write the code and fix the issues in small commits, instead of fixing everything at the end. I will use the AI review for design problems such as cohesion, coupling and magic number codes, and I will check every finding against the code before I accept it. I will also record the AI tool, the model and the date at the time of each use, so the declaration is easy to write.

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Gemini 3.8 Flash | Review of ParcelPoint with Prompt A (Task 2) | reviews/parcelpoint-review.md | Opened each file at the line given, compared with the code, SonarQube and the program output |
| Gemini 3.8 Flash | Wrote the BikeShare code with Prompts B and C (Task 4) | bikeshare-poor, bikeshare-good | Ran mvn package and the program, and read the code |
| Claude Sonnet 5.5 | Review of bikeshare-good with Prompt A (Task 4); help with setup, fixes and the wording of the report | reviews/bikeshare-good-review.md (and reviews/bikeshare-poor-review.md ถ้าให้ Claude รีวิว poor); code fixes in parcelpoint and report text | Built and ran the program after every fix, checked the line numbers and findings against the files |
