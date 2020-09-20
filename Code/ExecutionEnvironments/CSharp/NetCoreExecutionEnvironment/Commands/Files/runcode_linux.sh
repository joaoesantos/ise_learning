@echo off
Rem Arguments section
set base_dir=%1
set uuid=%2
set app_name=%3
Rem set app_content=%5
Rem set test_content=%6

Rem Create folders and projects names
set app_project_name=%app_name%\%app_name%.csproj%
set newdir=Exercise_%uuid%
cd /d %base_dir%
mkdir %newdir%
cd /d %newdir%

Rem Create solution and projects
dotnet new sln
dotnet new console -n %app_name%
dotnet sln add %app_project_name%

EXIT