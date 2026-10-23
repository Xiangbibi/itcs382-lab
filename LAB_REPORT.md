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
| 5 | Fixed |46eec60 |
| 6 | Fixed | 46eec60 |
| 7 | Fixed | TODO (รอบ 4) |
| 8 | Kept | Printing to the console is the output of a command-line program. |
| 9 | Fixed | 0ce2fc6 |
| 10 | Kept | Printing to the console is the output of a command-line program. |
| 11 | Kept | Printing to the console is the output of a command-line program. |
| 12 | Fixed | 73ec65e |
| 13 | Fixed | 73ec65e |

Number of SonarQube issues that remain after your fixes: TODO

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| TODO | TODO | TODO |
| TODO | TODO | TODO |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
TODO
```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | | |
| AI tool and exact model | | |
| Prompt (B or C as given, or your changed prompt in full) | | |
| Builds with mvn package (yes or no) | | |
| Number of classes, interfaces, enums and records | | |
| SonarQube issues, total | | |
| SonarQube issues, by rule | | |
| AI review findings in Check 1 | | |
| AI review findings in Check 2 | | |
| Review output file in the reviews folder | | |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

5.2 When should you not trust the AI review? Give an example from this lab.

5.3 What will you do differently in Project Phase 2 after this lab?

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Gemini 3.8 Flash| review code ParcelPoint with Prompt A|reviews/parcelpoint-review.md| Open the file at the line where the AI ​​failed to provide the actual code, and include the SonarQube results as well as the program execution output. |
