1. Files read: `parcelpoint/src/main/java/parcelpoint/Locker.java`, `parcelpoint/src/main/java/parcelpoint/Main.java`, `parcelpoint/src/main/java/parcelpoint/Parcel.java`, `parcelpoint/src/main/java/parcelpoint/ParcelService.java`.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | Check 1 | Main.java:7 | Resource leak: `Scanner` is instantiated but never closed.

 | Should fix | Enclose the `Scanner` initialization in a try-with-resources block. |
| 2 | Check 1 | ParcelService.java:6 | Unused import: `java.util.Date`.

 | Should fix | Remove the unused `import java.util.Date;` statement. |
| 3 | Check 1 | ParcelService.java:12 | Unused field: `maxParcels`.

 | Should fix | Remove the unused `maxParcels` field or enforce it when storing parcels. |
| 4 | Check 1 | ParcelService.java:41 | Probable bug: String comparison using `==` instead of `equals()`.

 | Must fix | Replace `parcel.getCode() == code` with `parcel.getCode().equals(code)`. |
| 5 | Check 1 | ParcelService.java:72 | Resource leak: `FileWriter` is opened but never closed or flushed.

 | Must fix | Use a try-with-resources statement to manage and close the `FileWriter`. |
| 6 | Check 1 | ParcelService.java:76 | Empty catch block suppressing `IOException`.

 | Should fix | Log or handle the exception instead of leaving the catch block empty. |
| 7 | Check 1 | ParcelService.java:27, 65 | Repeated string literal: `"STORED"` appears multiple times.

 | Note | Define a constant or enum for parcel status values. |
| 8 | Check 1 | Main.java:6-25 | Method too long or complex: `main` handles parsing, flow control, and multiple command dispatches in a single block.

 | Note | Extract command parsing and dispatch logic into helper methods. |
| 9 | Check 2 | ParcelService.java:24-36 | Procedural cohesion: `storeParcel` performs a sequence of loosely related operations (matching locker, mutating parcel, sending notification, persisting state).

 | Should fix | Extract notification and file persistence calls to separate specialized services or listeners. |
| 10 | Check 2 | ParcelService.java:38-48 | Procedural cohesion: `pickUp` combines locker lookup, state mutation, file saving, and fee calculation.

 | Should fix | Delegate fee calculation and persistence to dedicated handlers outside the locker pickup logic. |
| 11 | Check 2 | ParcelService.java:9-78 | Weak class cohesion: `ParcelService` mixes locker management, fee calculation, console notifications, and CSV persistence.

 | Must fix | Split out CSV persistence, fee calculation, and notification responsibilities into separate classes. |
| 12 | Check 2 | ParcelService.java:50 | Feature envy: `fee` operates entirely on parcel properties without using any `ParcelService` state.

 | Should fix | Move the fee calculation method to `Parcel` or a dedicated pricing strategy class. |
| 13 | Check 2 | ParcelService.java:24 | Stamp coupling: `storeParcel` accepts a full `Parcel` composite object when it primarily queries its size.

 | Note | Pass only the required attributes or retain the entity if locker assignment requires identity. |
| 14 | Check 2 | ParcelService.java:50 | Stamp coupling: `fee` accepts an entire `Parcel` object but only accesses `getSize()`.

 | Note | Change the method parameter from `Parcel` to `int size`. |
| 15 | Check 2 | ParcelService.java:59 | Control coupling: `printParcels` uses boolean flag `forStaff` to dictate internal control flow and display formats.

 | Should fix | Split into two distinct methods: `printStaffParcels()` and `printCustomerParcels()`. |
| 16 | Check 2 | Main.java:21 | Law of Demeter violation: `Main` reaches through `service.getLockers()` to filter on `Locker::isFree`.

 | Should fix | Add a `countFreeLockers()` query method directly to `ParcelService`. |
| 17 | Check 2 | Locker.java:4, Parcel.java:6, ParcelService.java:52-56 | Connascence of Convention: Implicit integer encoding for sizes (1 = small, 2 = medium, 3 = large) shared across classes.

 | Should fix | Replace the integer size codes with a shared `Size` enum. |
| 18 | Check 2 | Parcel.java:7 | Non-private field: `status` is declared `public`.

 | Must fix | Make `status` private and provide getter and setter methods. |
| 19 | Check 2 | ParcelService.java:20 | Encapsulation leak: `getLockers()` returns the mutable internal list reference.

 | Must fix | Return an unmodifiable view using `Collections.unmodifiableList(lockers)`. |

* Check 1 (Code quality): 8 findings
* Check 2 (Cohesion, coupling and connascence): 11 findings