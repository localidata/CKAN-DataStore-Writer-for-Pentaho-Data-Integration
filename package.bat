@echo off
call mvn clean compile package -Dmaven.test.skip=true
copy source\target\ckan_updater-1.0.jar ckan_updater.jar
rem copy ckan_updater.jar D:\Pentaho\pdi-ce-7.0.0.0-25\data-integration\plugins\steps\ckan-updater-plugin\ckan_updater.jar
pause



