-------------------------------------------------------------------
-- This file describes the organization of the database scripts. --
-------------------------------------------------------------------

WARNING: each table must have is own creation script
WARNING: all data are put in an unique script, except for localized data

-----------
DDL scripts:
------------
One root script : SPP_DatabaseSetup.sql
One script per table SPP : see directory SPP_Tables
One script per table WSRP : see directory WSRP_Tables 
One script per modified VGN table : see directory VGN_Tables 
One script for SPP constraints : SPP_Tables/DB_CONSTRAINT.sql
One script for SPP sequence : SPP_Tables/DB_SEQUENCE.sql
One script for WSRP constraints : WSRP_Tables/DB_CONSTRAINT.sql
One script to clean everything : SPP_DropDatabase.sql
REMARK: DDL scripts must be passed by Oracle Admin

-----------
DML scripts:
------------
One unique script for data without localization needs : SPP_Data/SPP_Data.sql
	This script contains data for Vignette, WSRP and SPP tables	
Localized data are loaded with sqlloader: see directory SPP_Data/sqlldr
One unique script to remove all data: SPP_Data/SPP_Remove_Data.sql

-----------
Old scripts:
------------
Old scripts are kept under directory old
