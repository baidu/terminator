Terminator - Service Virtualization
==========

Issue
----------
In the development / testing of a complex system, we often encounter when development / test in the case of other service module dependencies. For example, a system has two modules A and B, A B module depends on the service provided by the module:

1. B part of the function has not yet completed the development of lead A module development is blocked;

2. B module and some bad data structure, you can not develop a self-test to all situations;

3. A module for integration testing, wrote some automation use cases. However, due to uncontrollable B module, the module data B lead to frequent changes back to the data A module is also changed, and this time the module returns data-dependent B assertion will fail;

4. B module is not their own maintenance team, often unstable, leading to the development environment of the whole system unstable.

Solutions
----------

Service virtualization refers to an unstable virtual, unavailable, undeveloped full service. There are generally two methods:

1. For common pile agreement can preset the request and returns the value of the corresponding matching conditions, so that the system can be used to replace the pile is not completed before the real service development;

2. Recording Playback mode, the third-party services are available when the data recorded on the link, when unstable or unavailable, then replay the recorded data.

A major problem for which solutions I and II, the program focused on two issues three and four. Terminator (meaning: light and dark boundary line) to achieve the above two methods.

Terminator Each link can be viewed as a proxy that runs between the two services, now supports four modes of operation:

! [Function Summary] (WebContent / resources / images / function-summary.jpg)

TUNNEL: Tunnel mode, link service is responsible for receiving and forwarding data link, but is not stored, the equivalent of transparent state;

RECORD: Recording mode, link service requests and responses are stored on the link down, and the correspondence between the request to record responses;

REPLAY: Playback mode, direct link service returns a response was recorded when not connected to the rear end of the dependent services, when the request came when certain conditions are met;

STUB: Universal pile mode, link services can return results with pre-match rule, when the request came in line with pre-match rules that return results.

Overall architecture
----------
! [Terminator Architecture] (WebContent / resources / images / intro / architecture.png)

1. Network Communications: Socket mainly establish communication data transceiver link in the TCP layer, here is netty framework;

2. Protocol codecs: main binary packet protocol data or resolve to turn the data into binary data protocol, netty itself provides HTTP, SSL / TLS, WebSockets, Google Protocol Buffer codecs, if the need to expand You can define your own protocol codec;

3. Work mode processor: core of the system, recording playback now offers universal piles are implemented here. Here offers more expansion interface, you can achieve a new model based on customized requirements, such as the case of the back-end service goes down before the start of the recorded data. In addition to recording the playback mode, the signature class is a core component of its role is to identify a request for different systems may have different implementations; for generic pile pattern extraction class is a core component of his role is how to extract a request related to how to set the matching conditions for different systems (especially the agreement) may also have a different implementation. So these are the systems provide scalable interface.

4. APIs: For convenience (such as continuous integration) of use, the system basically all functions are available through the REST API.

Configuration and Deployment
----------
The terminator / src / database / {version} /terminator.sql executed in the MySQL database, {version} terminator.war corresponding version number;
Modified terminator / src / conf / configuration.properties database configuration;
(Optional) Modify the terminator / src / conf / configuration.properties in fileStorage.baseDir property, this is the recorded data storage location, data is generally produced larger disk space can be given a larger location. If you do not fill, default window system will be stored in the C: \ temp, linux system will be stored in the / tmp;
Execute ant build.xml, terminator.war will generate a directory in the terminator;
The terminator.war into any servlet container (eg tomcat) After starting, visit http: // IP: Port / terminator.

Manual and Development Guide
----------
After the deployment is successful, you can access the "User's Guide" and the "Developer's Guide"
