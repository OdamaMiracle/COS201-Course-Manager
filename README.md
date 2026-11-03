# COS201 Student Course Management System

A Java console application that lets a student record and manage the courses they are taking for the semester. Built as the **COS 201 (Programming I) mid-semester lab assessment** at MIVA Open University.

## Features

| # | Menu option | What it does |
|---|-------------|--------------|
| 1 | Add Course | Adds a course with a code, title and credit units, after validating the input |
| 2 | View All Courses | Shows every course in an aligned table |
| 3 | Search Course by Code | Finds a course by its code (any casing) using a recursive search |
| 4 | Compute Total Units | Adds up all credit units using a recursive method |
| 5 | Save to File | Writes the course list to `courses.txt` |
| 6 | Load from File | Reads the course list back from `courses.txt` |
| 7 | Exit Program | Ends the program |

Saved courses are also loaded automatically when the program starts.

## Project structure

```
.
├── Main.java                    # Menu and user interface (recursive menu)
├── CourseManager.java           # Course list, validation, search, total, save and load
├── Course.java                  # Data class: code, title, units
├── InvalidCourseException.java  # Custom exception for invalid course data
├── courses.txt                  # Sample saved course data
└── .gitignore
```

## Requirements

- Java Development Kit (JDK) 11 or newer
- A terminal, or VS Code with the *Extension Pack for Java*

On Debian or Ubuntu:

```bash
sudo apt update
sudo apt install default-jdk
```

## How to run

From the project folder:

```bash
javac *.java
java Main
```

Run the program from the folder that contains `courses.txt`, because the save file is read from and written to the current directory.

## Example session

```
==============================================
   STUDENT COURSE MANAGEMENT SYSTEM (COS 201)
==============================================

------------- MAIN MENU -------------
 1. Add Course
 2. View All Courses
 3. Search Course by Code
 4. Compute Total Units
 5. Save to File
 6. Load from File
 7. Exit Program
-------------------------------------
Enter your choice (1-7): 3

--- Search Course ---
Enter course code: IFT211
[Found]
  Code  : IFT211
  Title : Digital Logic Design
  Units : 2
```

## Input rules

- **Course code:** 3 letters followed by 3 digits, for example `COS201`. Spaces and lowercase are cleaned up automatically, so `cos 201` becomes `COS201`.
- **Course title:** cannot be empty. It is trimmed and converted to Title Case.
- **Credit units:** a whole number from 1 to 6.
- **Duplicates:** a course code that already exists is rejected.

Invalid input shows a clear error message and returns to the menu without crashing.

## File format

`courses.txt` stores one course per line as `CODE|TITLE|UNITS`:

```
COS201|Programming I|3
GST201|Use of English|2
```

Lines that are badly formatted are skipped when loading, and the program reports how many were skipped.

## Where the assessment requirements are met

- **Classes and objects:** `Course`, `CourseManager`, `InvalidCourseException`
- **Methods for each operation:** one method per menu action
- **Decision-making:** a `switch` statement handles the menu choice
- **String processing:** `trim()`, `toUpperCase()`, regular expressions and Title Case conversion
- **Iteration:** `for` loops to display courses and a `while` loop to read the file
- **Recursion:**
  - `Main.runMenu()` calls itself to show the menu again after each action (it stops on Exit)
  - `CourseManager.findIndexByCode()` searches the list recursively
  - `CourseManager.totalUnits()` sums the units recursively
- **Exception handling:** `InvalidCourseException`, `NumberFormatException`, `FileNotFoundException` and `IOException`
- **File I/O:** `BufferedReader` and `BufferedWriter` with try-with-resources

## Author

Odama Miracle Akwanufo
COS 201: Programming I, MIVA Open University
