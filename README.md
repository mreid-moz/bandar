Bandar
======

Monkey in the middle

CDA Quickstart:
---------------

### Build and start the server:
  - `mvn clean && mvn package`
  - `mvn exec:java`

### Run some sample queries:
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
