@echo off
Rem Arguments section
set base_dir=%1
set dir_name=%2
set solution_name=%3
set project=%4

Rem Run tests
dotnet run --project %base_dir%/%dir_name%/%solution_name%/%project%

EXIT