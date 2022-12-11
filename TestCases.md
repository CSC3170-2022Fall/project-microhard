# Test Cases for Presentation

This file includes all test cases that we would like to present in front of TA and Professor Clement. The test cases are divided into two parts: basic tests and enhanced tests. Basic tests are the one provided by UCB template, while enhanced tests comprise functionalities beyond the basic part. 

For those who have made an effort to develop the enhanced part, please fill in some *extreme cases* in the following section that you think it is worth presenting and can make our work more competitive. ***Please also indicate if any of the test cases are redundant and can be removed or merged. We just find that there are two many test cases and the time is limited!!!***

## UCB Basic Tests
***
### Test 1: *load*
```
load students;
load enrolled;
load schedule;
```
### Test 2: *basic select from where*
```
/* Comments */
select SID, Firstname 
from students
where Lastname = ’Chan’;
```
### Test 3: *select from two tables*
```
select Firstname, Lastname, Grade
from students, enrolled 
where CCN = ’21001’;
```
### Test 4: *create table*
```
/*Create table*/
create table enrolled2 as select SID
from enrolled, schedule
where Dept = ’EECS’ and Num = ’61A’;
/*Print created table*/
print enrolled2;
/*Select from created table*/
select Firstname, Lastname 
from students, enrolled2;
```

## Enhanced Tests
***
### Test 5: *select from multiple tables*
```
select SID, Grade, Year
from students, enrolled, schedule
where Year = '2003';
```
### Test XX: *functionality to be tested*
```
/*To Do*/
```