# Test Cases for Presentation

This file includes all test cases that we would like to present in front of TA and Professor Clement. The test cases are divided into two parts: basic tests and enhanced tests. Basic tests are the one provided by UCB template, while enhanced tests comprise functionalities beyond the basic part. 

For those who have made an effort to develop the enhanced part, please fill in some *extreme cases* in the following section that you think it is worth presenting and can make our work more competitive. ***Please also indicate if any of the test cases are redundant and can be removed or merged. We just find that there are two many test cases and the time is limited!!!***

***
## UCB Basic Tests

### Test 1: *load* and *print*
```
load students;
load enrolled;
load schedule;

print schedule;

==========result===========
Contents of schedule:
  21228 61A EECS 2-3MWF 1 Pimentel F 2003      
  21231 61A EECS 1-2MWF 1 Pimentel S 2004      
  21229 61B EECS 11-12MWF 155 Dwinelle F 2003  
  21232 61B EECS 1-2MWF 2050 VLSB S 2004       
  21103 54 Math 1-2MWF 2050 VLSB F 2003        
  21105 54 Math 1-2MWF 1 Pimentel S 2004       
  21001 1A English 9-10MWF 2301 Tolman F 2003  
  21005 1A English 230-5TuTh 130 Wheeler S 2004
```
### Test 2: *basic select from where*
```
/* Comments */
select SID, Firstname 
from students
where Lastname = ’Chan’;

==========result===========
Search results:
  102 Valerie
  106 Yangfan
```
### Test 3: *select from two tables*
```
select Firstname, Lastname, Grade
from students, enrolled 
where CCN = ’21001’;

==========result===========
Search results:
  Jason Knowles B
  Valerie Chan B+
  Shana Brown B+
  Yangfan Chan B
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

==========result===========
Contents of enrolled2:
  101
  102
  104
  105
  106
  
Search results:
  Jason Knowles
  Valerie Chan
  Thomas Armstrong
  Shana Brown
  Yangfan Chan
```

***
## Enhanced Tests

### Test 5: *Case Insensitivity*
```
SELecT SID, Firstname
FROm students
whERE SID > '103';

==========result===========
Search results:
  104 Thomas
  105 Shana
  106 Yangfan
```

### Test 6: *select from multiple tables*
```
select SID, Grade, Year
from students, enrolled, schedule
where Year = '2003';

==========result===========
Search results:
  101 B 2003
  101 B 2003
  102 A 2003
  102 B+ 2003
  104 A- 2003
  104 B+ 2003
  105 A 2003
  105 B+ 2003
  106 A 2003
  106 B 2003
```

### Test 7: *select all*
```
select *
from students, enrolled, schedule
where Year = '2003' and Grade = 'B';

==========result===========
Search results:
  101 Knowles Jason F 2003 EECS 7000 21228 B 61A EECS 2-3MWF 1 Pimentel F 2003
  101 Knowles Jason F 2003 EECS 7000 21001 B 1A English 9-10MWF 2301 Tolman F 2003
  106 Chan Yangfan F 2003 LSUnd 9999 21001 B 1A English 9-10MWF 2301 Tolman F 2003
```

### Test 8: *order by one column (ascending order)*
```
select SID, Firstname, Lastname, Grade
from students, enrolled
order by Lastname;

==========result===========
Search results:
  104 Thomas Armstrong A-
  104 Thomas Armstrong B+
  104 Thomas Armstrong A-
  104 Thomas Armstrong A-
  105 Shana Brown A
  105 Shana Brown B+
  102 Valerie Chan A
  102 Valerie Chan A-
  102 Valerie Chan A
  102 Valerie Chan B+
  106 Yangfan Chan A
  106 Yangfan Chan B
  106 Yangfan Chan A
  101 Jason Knowles B
  101 Jason Knowles B+
  101 Jason Knowles A-
  101 Jason Knowles B
  103 Jonathan Xavier B+
  103 Jonathan Xavier B+
```

### Test 9: *order by multiple columns (descending order)*
```
select SID, Fistname, Grade
from students, enrolled
order by SID, Firstname desc;

==========result===========
Search results:
  106 Yangfan A
  106 Yangfan B
  106 Yangfan A
  105 Shana A
  105 Shana B+
  104 Thomas A-
  104 Thomas B+
  104 Thomas A-
  104 Thomas A-
  103 Jonathan B+
  103 Jonathan B+
  102 Valerie A
  102 Valerie A-
  102 Valerie A
  102 Valerie B+
  101 Jason B
  101 Jason B+
  101 Jason A-
  101 Jason B
```

### Test 10: *condition or & and*
```
select SID, Fistname, Grade
from students, enrolled, schedule
where Dept = 'EECS' and Year = '2003' or SID = '103';

==========result===========
Search results:
  101 Jason B
  102 Valerie A
  103 Jonathan B+
  103 Jonathan B+
  104 Thomas A-
  104 Thomas B+
  105 Shana A
```

### Test 11: *aggregate function*
```
/*avg*/
select avg(Scholarship)
from students
where YearEnter = '2003';

select SID, avg(Scholarship) from students;

/*count*/
select count(*)
from students;

/*min*/
select min(SID) from students;
select min(Lastname) from students;

/*max*/
select max(Scholarship) from students;

==========result===========
Search results:
  8416

Error: Aggregate function without Group By does not allow other columns to exist!

Search results:
  6

Search results:
  101
  
Error: There seem to be problems with the column. Try select another one!

Search results:
  11250
```
