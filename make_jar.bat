mvn clean package && del download\*.jar && xcopy /Y target\*.jar download && git add download/*.jar
pause