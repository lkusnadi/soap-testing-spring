# soap-endpoint
Demonstration of SOAP endpoint testing using Spring

This project shows how to run SOAP endpoint testing using **Apache CXF** framework.

This is assuming that the SOAP web service has been deployed and running. The purpose is to have a nightly test run on UAT or PROD web service endpoint.

Apache CXF uses WSDL file to bootstrap the SOAP web service and client codes.

To try this code, use STS. Make sure to put the target/generated/src/main/java in the buildpath of STS.