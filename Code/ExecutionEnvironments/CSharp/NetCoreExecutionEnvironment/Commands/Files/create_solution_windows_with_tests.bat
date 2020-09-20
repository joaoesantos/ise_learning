@echo off
Rem Arguments section
set base_dir=%1
set solution_name=%2
set app_name=%3
set test_app_name=%4
REM set app_content=%5
REM set test_content=%6

echo app_content %app_content%
echo test_content %test_content%


Rem Create folders and projects names
set app_project_name=%app_name%\%app_name%.csproj%
set test_app_project_name=%test_app_name%\%test_app_name%.csproj
cd /d %base_dir%
mkdir %solution_name%
cd /d %solution_name%

Rem Create solution and projects
dotnet new sln
dotnet new console -n %app_name%
dotnet sln add %app_project_name%
dotnet new xunit -n %test_app_name%
dotnet sln add %test_app_project_name%

Rem Add code project as to test project 
dotnet add ./%test_app_name%/%test_app_name%.csproj reference ./%app_name%/%app_name%.csproj  

EXIT