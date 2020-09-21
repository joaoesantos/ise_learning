@echo off
Rem Arguments section
set base_dir=%1
set template_dir=%2
set new_dir=%3

Rem Create folders
mkdir %base_dir%/%new_dir%

Rem Copy template files
xcopy %base_dir%\%template_dir% %base_dir%\%new_dir% /h /i /c /k /e /r /y

EXIT