@echo off
call mvn clean compile package -Dmaven.test.skip=true
copy source\target\ckan_updater-1.0.jar ckan_updater.jar
pause



