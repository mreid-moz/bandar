Bandar
======

Monkey in the middle

CDA / CVB Quickstart:
---------------

### Customize configuration:
  - edit the following files to specify correct paths on your machine:
    - bandar-query.yml
    - src/main/resources/cda.spring.xml
    - src/test/resources/cda/repo/sample-kettle.cda
    - src/test/resources/cpk/cvb.xml
  - create a dummy "simple-jndi" directory (not sure why this is necessary...)
    - `mkdir simple-jndi`

### Build and start the server:
  - `mvn clean && mvn package`
  - `mvn exec:java`

### Run some sample queries:
- List all endpoints:

  `curl http://localhost:8080

- List available CDA endpoints:

  `curl http://localhost:8080/cda`

- Get default output for kettle example:

  `curl http://localhost:8080/cda/sample-kettle`

- Get xml output for kettle example:

  `curl http://localhost:8080/cda/sample-kettle.xml`

- Get html output for kettle example:

  `curl http://localhost:8080/cda/sample-kettle.html`

- Get json output and specify a parameter:

  `curl "http://localhost:8080/cda/sample-kettle.json?fooFilter=Hola"`

- List available parameters:

  `curl http://localhost:8080/cda/sample-kettle/parameters`

- List available CVB endpoints:

  `curl http://localhost:8080/cvb`

- Get a list of files in the root dir:

  `curl http://localhost:8080/cda/listFiles`

- Refresh the CVB endpoints:

  `curl -X POST http://localhost:8081/tasks/refresh-cvb`

