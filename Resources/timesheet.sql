CREATE DATABASE TES;

CREATE USER 'EMPLOYEE'@'localhost' IDENTIFIED BY 'PASSWORD';
CREATE USER 'EMPLOYEE'@'%' IDENTIFIED BY 'PASSWORD';

GRANT ALL PRIVILEGES ON EMPLOYEE.* TO EMPLOYEE@localhost WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON EMPLOYEE.* TO EMPLOYEE@"%" WITH GRANT OPTION;

-- CREATE TABLE EMPLOYEE
CREATE TABLE TES.EMPLOYEE
(
	EMPLOYEE_ID         INTEGER      NOT NULL,
    EMPLOYEE_NAME       VARCHAR(100) NOT NULL,
	EMPLOYEE_USERNAME   VARCHAR(50)  NOT NULL UNIQUE,
	PRIMARY KEY(EMPLOYEE_ID),
    FOREIGN KEY(EMPLOYEE_USERNAME) REFERENCES TES.CREDENTIALS(EMPLOYEE_USERNAME)
 );

-- create table CREDENTIALS
CREATE TABLE TES.CREDENTIALS
(
	EMPLOYEE_USERNAME   VARCHAR(50) NOT NULL,
    EMPLOYEE_PASSWORD   VARCHAR(50) NOT NULL,
    PRIMARY KEY(EMPLOYEE_USERNAME)
); 

-- create table TIMESHEET
CREATE TABLE TES.TIMESHEET
(
    EMPLOYEE_ID     INTEGER  NOT NULL,
    END_WEEK        DATE     NOT NULL,
    OVERTIME        DECIMAL  NULL,
    FLEXTIME        DECIMAL  NULL,
	PRIMARY KEY(EMPLOYEE_ID, END_WEEK),
	FOREIGN KEY(EMPLOYEE_ID) REFERENCES TES.EMPLOYEE(EMPLOYEE_ID)
);

-- create table TIMESHEETROW
CREATE TABLE TES.TIMESHEETROW
(
    EMPLOYEE_ID     INTEGER       NOT NULL,
    END_WEEK        DATE          NOT NULL,
    PROJECT_ID      INTEGER       NOT NULL,
	WORK_PACKAGE    VARCHAR(40)   NOT NULL,
	NOTES           VARCHAR(200)  NULL,
	SAT             DECIMAL       NULL,
    SUN             DECIMAL       NULL,
    MON             DECIMAL       NULL,
    TUE             DECIMAL       NULL,
    WED             DECIMAL       NULL,
    THU             DECIMAL       NULL,
    FRI             DECIMAL       NULL,
	PRIMARY KEY(EMPLOYEE_ID, END_WEEK, PROJECT_ID, WORK_PACKAGE),
    FOREIGN KEY(EMPLOYEE_ID) REFERENCES TES.EMPLOYEE(EMPLOYEE_ID),
    FOREIGN KEY(END_WEEK) REFERENCES TES.TIMESHEET(END_WEEK)
);
  

-- Load data into EMPLOYEE table
INSERT INTO TES.EMPLOYEE VALUES('1', 'Admin admin', 'admin');
INSERT INTO TES.EMPLOYEE VALUES('2', 'Karanveer Khanna', 'kv');
INSERT INTO TES.EMPLOYEE VALUES('3', 'Ryan Liang', 'rl');
INSERT INTO TES.EMPLOYEE VALUES('4', 'Test test', 'tt');

-- Load data into CREDENTIALS table
INSERT INTO TES.CREDENTIALS VALUES('admin', 'password');
INSERT INTO TES.CREDENTIALS VALUES('kv', 'password');
INSERT INTO TES.CREDENTIALS VALUES('rl', 'password');
INSERT INTO TES.CREDENTIALS VALUES('tt', 'password');

-- Load data into TIMESHEET table
INSERT INTO TES.TIMESHEET VALUES('1', '2018-11-09');
INSERT INTO TES.TIMESHEET VALUES('1', '2018-10-26');
INSERT INTO TES.TIMESHEET VALUES('2', '2018-11-02');
INSERT INTO TES.TIMESHEET VALUES('2', '2018-11-09');
INSERT INTO TES.TIMESHEET VALUES('2', '2018-10-26');
INSERT INTO TES.TIMESHEET VALUES('3', '2018-11-02');
INSERT INTO TES.TIMESHEET VALUES('3', '2018-11-09');
INSERT INTO TES.TIMESHEET VALUES('3', '2018-10-26');
INSERT INTO TES.TIMESHEET VALUES('4', '2018-11-02');
INSERT INTO TES.TIMESHEET VALUES('4', '2018-11-09');
INSERT INTO TES.TIMESHEET VALUES('4', '2018-10-26');

-- Load data into TIMESHEETROW table
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '2', 'WP 1', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '1', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '2', 'WP 3', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '4', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '2', 'WP 1', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '1', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '2', 'WP 3', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '4', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '2', 'WP 1', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '1', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '2', 'WP 3', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '4', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '2', 'WP 1', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '1', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '1', 'WP 1', 'NOTES 1', NULL, NULL, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '1', 'WP 2', 'NOTES 2', NULL, NULL, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '2', 'WP 3', 'NOTES 3', NULL, NULL, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '4', 'WP 4', 'NOTES 4', NULL, NULL, '2.2', '1.2','4.2','1.2', NULL);
