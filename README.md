Bandar
======

Monkey in the middle


Overview
========

Bandar is a server for providing REST access to heterogeneous backend data
sources.

See the 'Quickstart' section below for the tl;dr.

Each basic data source is located in its own namespace. Currently, that
includes:

- Filesystem (/file)
  - Provides access to a directory of files on the server
- [CDA - Community Data Access](https://github.com/webdetails/cda) (/cda)
  - Provides access to CDA Data sources
- [CVB - Community VFS Browser](https://github.com/webdetails/cvb) (/cvb)
  - Provides access to data using VFS by leveraging
    [Pentaho Data Integration](http://kettle.pentaho.com/)

Each data source has a name, and can optionally list its contents for end-user
discovery.

Data Sources
============

Filesystem
----------

A simple way to serve files.  The location on the Bandar server's filesystem is
specified in the `bandar-query.yml` configuration file.

A GET request to `/file` lists the available files, and a GET request to
`/file/myfile.txt` returns the contents of the given file.

Files may be inside subdirectories, though such files will not be listed in the
base `/file` list.  For example, you can GET `/file/path/to/anotherfile.zip`.

There is sanity checking to make sure clients cannot fetch files outside of
the specified directory (for example by fetching `/file/../../etc/passwd`).

### Extending Filesystem provider
Simply drop new files into the configured directory and they will be available.

----

CDA
---

Community Data Access (CDA) is a Pentaho plugin designed for accessing data
with great flexibility.

More information can be found on the [Webdetails CDA page](http://www.webdetails.pt/ctools/cda.html)

Bandar's integration of CDA uses a filesystem-based repository to store the
`.cda` files, and provides a basic means of interrogating each file to
determine what parameters can be supplied and what data access names are
available.

A GET request to `/cda` lists the available CDA endpoints.

A GET request to a given endpoint, such as the included example
`/cda/sample-kettle` will return the data in JSON format by default.  You may
also specify an alternate output format by supplying a file extension like
`/cda/sample-kettle.xml` or `/cda/sample-kettle.csv`

A CDA file may contain multiple "Data Access IDs".  You can list the available
ones via a GET to `/cda/sample-kettle/queries`.  The "id" field in the result
is what is used to specify a given query / data access.

To fetch the results of a specific Data Access, use the following form:
`/cda/<cda file>[.<type]/id`
So to fetch ID `3` from `sample-kettle` in excel format:
`/cda/sample-kettle.xls/3`

A CDA file may also accept parameters, which can be listed similarly to queries
using `/cda/sample-kettle/parameters`.  To specify values for parameters, they
are added as query parameters.  For example:
`/cda/sample-kettle.json?fooFilter=Hello`

### Extending CDA
To add new CDA endpoints, drop a CDA file into the specified repository.
The example configuration is located in `src/main/resources/cda.spring.xml`.

----

CVB
---

Community VFS Browser (CVB) is a layer on top of CPF / CPK which allow
access-controlled browsing of VFS locations.

The general use is similar to the other endpoints, in that you can hit `/cvb`
and get a list of endpoints. These include:
- listFiles - list the files at the specified URI. Example:
  - `curl http://localhost:8080/cvb/listFiles?URI=%2Ftmp%2Fbandar`
- downloadFile - TODO
- getLocations - list the locations configured in `cvb.xml`

### Extending CVB
To add new endpoints, add kettle jobs (.kjb) or kettle transformations
(.ktr) in the configured location. CPK (which powers CVB) caches a list of
valid endpoints, so you also need to refresh the cache after making changes.

There is a task for refreshing the cache, visit it using:
`curl -X POST http://localhost:8081/tasks/refresh-cvb`

----


CDA / CVB Quickstart:
---------------------

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

  `curl http://localhost:8080`

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

- Get a list of files in another dir:

  `curl http://localhost:8080/cda/listFiles?URI=%2Ftmp%2Fbandar`

- Refresh the CVB endpoints:

  `curl -X POST http://localhost:8081/tasks/refresh-cvb`

