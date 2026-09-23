@echo off
setlocal
cd /d "%~dp0"
py -3 build.py
if errorlevel 1 python build.py
pause
