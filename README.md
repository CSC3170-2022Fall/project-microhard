[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=9434865&assignment_repo_type=AssignmentRepo)

# CSC3170 Course Project

## Project Overall Description

This is our implementation for the course project of CSC3170, 2022 Fall, CUHK(SZ). For details of the project, you can refer to [project-description.md](project-description.md). In this project, we will utilize what we learned in the lectures and tutorials in the course, and implement either one of the following major jobs:

<!-- Please fill in "x" to replace the blank space between "[]" to tick the todo item; it's ticked on the first one by default. -->

- [ ] **Application with Database System(s)**
- [X] **Implementation of a Database System**

## Team Members

Our team consists of the following members, listed in the table below (the team leader is shown in the first row, and is marked with 🚩 behind his/her name):

<!-- change the info below to be the real case -->

| Student ID | Student Name | GitHub Account (in Email)  | GitHub Username | Actual Contribution |
| ---------- | ------------ | -------------------------- | ---------- | ------------ |
| 120090609  | 刘恒睿 🚩    | 120090609@link.cuhk.edu.cn | [RichardRui9](https://github.com/RichardRui9) |*fill in the blank* |
| 120090860  | 李楠轩       | 120090860@link.cuhk.edu.cn | [George-Mac](https://github.com/George-Mac) |Basic: Help completing Table. <br />Enhanced: Case-Insensitive. <br />Presentation: Make <br />first part of PPT <br />and give a presentation of that part. |
| 120090495  | 张家荣       | 120090495@link.cuhk.edu.cn | [Yae-mikooo](https://github.com/Yae-mikooo) | *fill in the blank*|
| 119010269  | 宋泽方       | 119010269@link.cuhk.edu.cn | [Song199010](https://github.com/Song199010) | *fill in the blank*|
| 120090565  | 胡文涵       | 120090565@link.cuhk.edu.cn | [Clarice927](https://github.com/Clarice927) | *fill in the blank*|
| 119010216  | 路东竹渊     | 119010216@link.cuhk.edu.cn | [Tim-Lu-cuhksz](https://github.com/Tim-Lu-cuhksz) |Basic: Implement `selectStatement`, `selectClause` in `CommandInterpreter.java` <br />Enhanced: Multi-relation Join & Aggregate Functions <br />Presentation: Present the seond part|
| 120090771  | 邱纬纶       | 120090771@link.cuhk.edu.cn | [alanqwl](https://github.com/alanqwl) | *fill in the blank*|
| 120090224  | 杨尚霖       | 120090224@link.cuhk.edu.cn | [UnitedSnakes](https://github.com/CSC3170-2022Fall/project-microhard/commits?author=UnitedSnakes) | *fill in the blank*|
| 120090470  | 李鹏         | 120090470@link.cuhk.edu.cn | [pengleee](https://github.com/pengleee) | *fill in the blank*|

## Project Specification

<!-- You should remove the terms/sentence that is not necessary considering your option/branch/difficulty choice -->

After thorough discussion, our team made the choice and the specification information is listed below:

- Our option choice is: **Option 3**

## How to run our program

### Use Makefile

1. Go to the file Table.java and change the read path![截屏2022-12-28 下午10.44.53](/Users/linanxuan/Library/Application Support/typora-user-images/截屏2022-12-28 下午10.44.53.png)
2. Enter "cd proj1" in the terminal
3. Enter "make check" in the terminal![截屏2022-12-28 下午10.46.59](/Users/linanxuan/Library/Application Support/typora-user-images/截屏2022-12-28 下午10.46.59.png)

### Run Main.java

1. Go to the file Table.java and change the read path![截屏2022-12-28 下午10.46.19](/Users/linanxuan/Library/Application Support/typora-user-images/截屏2022-12-28 下午10.46.19.png)
2. Run the main.java file

## Project Abstract

<!-- TODO -->

Abstract – Microhard

This CSC3170 project, based on a design from UCB CS61B, Fall 2014, requires our famous group Microhard to include a very simple query language to write a miniature relational database management system (DBMS).

The prototype Java codes provided by UCB have initiated a skeleton for DBMS and the first step for Microhard is to fill in the missing Java codes in five prototype Java files. As of December 05, Microhard has finished this job and successfully implemented the miniature DBMS. This DBMS reads query language tokens from terminal input and then invokes appropriate Java functions to perform specific operations.

Microhard then decides to add to the query language more syntaxes like "group by" (for more details please refer to Todo.md) and optimize the speed and efficiency of the DBMS. These updates will be released soon.

Please do not hesitate to contact us if we can be of assistance.

Microhard
December 05, 2022

## Program Design
The project mainly consists of 7 Java files: `CommandInterpreter.java`, `Tokenizer.java`, `Database.java`, `Table.java`, `Row.java`, `Condition.java` and `Column.java`.

`CommandInterpreter.java` uses `Tokenizer.java` to parse user commands, and passes appropriate tests. `Tokenizer.java` interacts with database or creates conditions. `Database.java` is the container for tables. `Table.java` is the brains of the project. It writes and reads from files, and also performs db operations when given commands by `CommandIntepreter.java`. `Condition.java` represents a where condition command in a select clause. `Row.java` represents a single row of data. `Column.java` acts as an index of a column in a list of rows. Besides, there are `Utils.java`, `DBException.java`, `Main.java` in the project. 

In *testing* folder, there are database files `blank.db`, `enrolled.db`, `schedule.db` and `students.db`. Tester Python files and sample input/output are also included. 

## Functionality Implementation
### Basic Functions
1. Load table from .db: It uses the `readTable()` method in `Table.java` to read the contents of db file and return as a table.
2. Omit comments in expressions
3. Basic *select from ... where* operation
4. Create table from select clause
5. Join two tables
### Enhanced Functions
1. Case-Insensitive to User’s Input
2. Multiple-Relations Joint: Users can specify more than two tables after `from` statement in the query expressions. After multi-relation join operation, one can specify certain conditions to select rows from the joined table. The basic idea is that we iteratively invoke `table.select` from `Table.java` to join the first two relations, and join the obtained relation with the third relation. Finally, we will obtain a joined table with non-repeated columns from each separate relation, after which `ConditionClause` is applied. More details can be found in `CommandInterpreter.java`.
3. Select All: Users can use `select *` to see all the data from the table. A `for` loop is used to get all the column names of the table. Then, normal `select` by the total names is executed.
4. Condition “OR”: We support conditions with a mixture of `and` operation and `or` operation.
5. Results “Order by”: Users can reorder the result table in ascending/descending order based on the value(s) of one or more columns, in which `asc` or none for 'ascending' and `desc` for 'descending'. More details can be found in `selectStatement` method, `CommandInterpreter.java` file.
6. Multi-Load: We suppoer loading tables in one line, e.g.`load students, enrolled, schedule;` instead of `load students;\nload enrolled;\nload schedule;`. When facing errors, it will throw the first error that encountered.
7. Aggregate Function: Users can use `avg`, `count`, `min`, `max` to calculate the corresponding values. Note that only `count` supports non-numerical attribute. If a non-numerical attribute appear in the argument of `avg`, `min`, `max`, an error will occur. To support this functionality, `select_aux` is written to detect the presence of any aggregate function name. If an aggregate function exists, the pertinent value is computed and returned as a single-value table.

Please visit documentation [here](TestCases.md) for more test details.

## Difficulty Encountered & Solutions
Facing difficulty when implementing enhanced function "order by", cause by changing HashMap into ArrayList, this caused printing multiple rows with the same selected condition. The correct one should be a single row with a unique condition.



## Presentation Video Link

https://cuhko365-my.sharepoint.com/:v:/g/personal/120090609_link_cuhk_edu_cn/Ed3DS1e8eoVPqPzizAdmyNQBEqQWCmFjDLqoRN7TOhw2nw?e=cVpTYw
