@echo off
setlocal ENABLEDELAYEDEXPANSION
if defined CLASSPATH (set CLASSPATH=%CLASSPATH%;.) else (set CLASSPATH=.)
FOR /R .\lib %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G
Echo The Classpath definition is %CLASSPATH%
...
java Jewel.Batch.JewelBatchRunner
