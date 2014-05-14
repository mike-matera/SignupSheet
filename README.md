SignupSheet
===========

The Signup Sheet is a web appliation that help match volunteers with jobs that are needed to support an event or party. It was written for the California Foundation for Advancement of Electronic Arts (http://cfaea.net/).

1. Project Technologies

* Google Web Toolkit (http://www.gwtproject.org/). GWT translates Java source code into JavaScript code for execution in the browser. Its primary strenght is that it enables mobility between server-side and client-side components. 

* Google App Engine (https://developers.google.com/appengine). Google App Engine provides the server component of this project. Hosting is paid for by computer/storage utilization and is very cheap (between $0 and $5 per week). Persistence is handled by GAE's High Replication Database (https://developers.google.com/appengine/docs/java/datastore/). HRD imposes restrictions on data consistency but guarantees very high availability. 

* ANTLR Parser Generator (http://antlr.org). Volunteer positions are defined using a simple text-based configuration language. The configuration (called the schema) is compiled by server and client components. The project contains a fork of the ANTLR 3 runtime to support the generated parsers.

2. Project Structure

2.1. Top Level Modules 

There are two top-level GWT modules:

* AntlrGWT  - GWT safe fork of the ANTLR 3 runtime
* SignupApp - Web application project. 

2.2. AntlrGWT Structure

src/org/antlr/AntlrGWT.gwt.xml - Module Definition
src/org/antlr/gwt/AntlrGWT.java - Trivial module entry point
src/org/antlr/gwt/runtime - Runtime ANTLR code

2.3. SignupApp Structure

src/com/fatboycentral/SignupApp.gwt.xml - Module Definition
src/com/fatboycentral/client - Client-side Code
src/com/fatboycentral/server - Server-side Code
src/com/fatboycentral/shared - Code shared by both the client and server
src/com/fatboycentral/shared/parser - Location of the generated parsers (*.g is the parser definition)
