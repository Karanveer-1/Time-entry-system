CREATE DATABASE TES;

CREATE USER 'EMPLOYEE'@'localhost' IDENTIFIED BY 'PASSWORD';
CREATE USER 'EMPLOYEE'@'%' IDENTIFIED BY 'PASSWORD';

GRANT ALL PRIVILEGES ON EMPLOYEE.* TO EMPLOYEE@localhost WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON EMPLOYEE.* TO EMPLOYEE@"%" WITH GRANT OPTION;

-- create table CREDENTIALS
CREATE TABLE TES.CREDENTIALS
(
	EMPLOYEE_USERNAME   VARCHAR(50) NOT NULL,
    EMPLOYEE_PASSWORD   VARCHAR(50) NOT NULL,
    PRIMARY KEY(EMPLOYEE_USERNAME)
); 

-- CREATE TABLE EMPLOYEE
CREATE TABLE TES.EMPLOYEE
(
	EMPLOYEE_ID         INTEGER      NOT NULL,
    EMPLOYEE_NAME       VARCHAR(100) NOT NULL,
	EMPLOYEE_USERNAME   VARCHAR(50)  NOT NULL UNIQUE,
	PRIMARY KEY(EMPLOYEE_ID),
    FOREIGN KEY(EMPLOYEE_USERNAME) REFERENCES TES.CREDENTIALS(EMPLOYEE_USERNAME)
 );

-- create table TIMESHEET
CREATE TABLE TES.TIMESHEET
(
    EMPLOYEE_ID     INTEGER  NOT NULL,
    END_WEEK        DATE     NOT NULL,
    OVERTIME        DECIMAL  NOT NULL DEFAULT 0,
    FLEXTIME        DECIMAL  NOT NULL DEFAULT 0,
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
	NOTES           VARCHAR(200)  NOT NULL,
	SAT             DECIMAL       NOT NULL DEFAULT 0,
    SUN             DECIMAL       NOT NULL DEFAULT 0,
    MON             DECIMAL       NOT NULL DEFAULT 0,
    TUE             DECIMAL       NOT NULL DEFAULT 0,
    WED             DECIMAL       NOT NULL DEFAULT 0,
    THU             DECIMAL       NOT NULL DEFAULT 0,
    FRI             DECIMAL       NOT NULL DEFAULT 0,
	PRIMARY KEY(EMPLOYEE_ID, END_WEEK, PROJECT_ID, WORK_PACKAGE),
    FOREIGN KEY(EMPLOYEE_ID, END_WEEK) REFERENCES TES.TIMESHEET(EMPLOYEE_ID, END_WEEK)
);


-- Load data into CREDENTIALS table
INSERT INTO TES.CREDENTIALS VALUES('admin', 'password');
INSERT INTO TES.CREDENTIALS VALUES('kv', 'password');
INSERT INTO TES.CREDENTIALS VALUES('rl', 'password');
INSERT INTO TES.CREDENTIALS VALUES('tt', 'password');

-- Load data into EMPLOYEE table
INSERT INTO TES.EMPLOYEE VALUES('1', 'Admin admin', 'admin');
INSERT INTO TES.EMPLOYEE VALUES('2', 'Karanveer Khanna', 'kv');
INSERT INTO TES.EMPLOYEE VALUES('3', 'Ryan Liang', 'rl');
INSERT INTO TES.EMPLOYEE VALUES('4', 'Test test', 'tt');

-- Load data into TIMESHEET table
INSERT INTO TES.TIMESHEET VALUES('1', '2018-11-09', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('1', '2018-10-26', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('2', '2018-11-02', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('2', '2018-11-09', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('2', '2018-10-26', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('3', '2018-11-02', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('3', '2018-11-09', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('3', '2018-10-26', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('4', '2018-11-02', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('4', '2018-11-09', '0', '0');
INSERT INTO TES.TIMESHEET VALUES('4', '2018-10-26', '0', '0');

-- Load data into TIMESHEETROW table
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '2', 'WP 1', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-10-26', '1', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '2', 'WP 3', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('1', '2018-11-09', '4', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '2', 'WP 1', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-10-26', '1', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '2', 'WP 3', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('2', '2018-11-09', '4', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '2', 'WP 1', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-10-26', '1', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '2', 'WP 3', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('3', '2018-11-09', '4', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '2', 'WP 1', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-10-26', '1', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '1', 'WP 1', 'NOTES 1', 0, 0, '1.2', '1.2','1.2','1.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '1', 'WP 2', 'NOTES 2', 0, 0, '2.2', '2.2','2.2','2.2','2.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '2', 'WP 3', 'NOTES 3', 0, 0, '3.2', '2.2','1.2','3.2','1.2');
INSERT INTO TES.TIMESHEETROW VALUES('4', '2018-11-09', '4', 'WP 4', 'NOTES 4', 0, 0, '2.2', '1.2','4.2','1.2', 0);