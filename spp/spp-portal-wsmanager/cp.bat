@echo off
:START
if "%cp%" == "" goto without
:WITH
set cp=%cp%;%1
goto END
:WITHOUT
set cp=%1
:END