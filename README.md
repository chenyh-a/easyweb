# easyweb
A simple java framework contains data import/export routine with high performance.  Source code usage:
- Use Git download(clone) source code,
  - git clone https://github.com/chenyh-a/easyweb
- Initiate example data in your database(Mysql or MariaDB)
  - Open mysql client command window and create a DB named "test"(script below):
    - create database test default character set utf8mb4;
  - Import example data in Mysql Client window using script below:
    - use test
    - source /git/easyweb/data.sql (use your own path)
- Modify DB connection parameters in config file src/main/resources/application.properties, such as DB url/username/password...
- Use Maven to compile/package it using command above:
  - mvn package
- Rename generated war file "easyweb-0.0.1.war" in "target" dir to "easyweb.war", then copy it to tomcat dir "webapps".
- Type url http://locahost:8080/easyweb/ in your browser to start it.
- Or simply import the source code folder as a project in IDE, e.g. Eclipse, etc. and run/debug project in IDE.
