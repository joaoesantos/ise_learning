@echo off
Rem Arguments section
set base_dir=%1
set solution_name=%2

cd /d %base_dir%

Rem Run tests
dotnet test ./%solution_name%

EXIT