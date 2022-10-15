# easyweb
A very simple java framework contains data import/export.  Source code usage:
- Use Git download(clone) source code,
  - git clone https://github.com/chenyh-a/easyweb
- Initiate example data in your database(Mysql or MariaDB)
  - Create a DB named "test";
  - Import example data in Mysql Client window using command above:
    - use test
    - source /git/easyweb/data.sql (use your own path)
- use Maven to compile using command above:
  - mvn package
- Rename generated package file "easyweb-0.0.1.war" in target dir to "easyweb.war", then copy it to tomcat dir "webapps".
- type url http://locahost:8080/easyweb/ in your browser to start it.
- Or simply import the source code folder as a project in IDE, e.g. Eclipse, etc. and run/debug project in IDE.
