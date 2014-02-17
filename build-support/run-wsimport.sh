#!/bin/bash

GENERATED_DIR=../src/generated/java

wsimport -p gov.nih.nlm.uts.webservice.content -d $GENERATED_DIR -keep https://uts-ws.nlm.nih.gov/services/nwsContent?wsdl
wsimport -p gov.nih.nlm.uts.webservice.security -d $GENERATED_DIR -keep https://uts-ws.nlm.nih.gov/services/nwsSecurity?wsdl
wsimport -p gov.nih.nlm.uts.webservice.metadata -d $GENERATED_DIR -keep https://uts-ws.nlm.nih.gov/services/nwsMetadata?wsdl
wsimport -p gov.nih.nlm.uts.webservice.finder -d $GENERATED_DIR -keep https://uts-ws.nlm.nih.gov/services/nwsFinder?wsdl
wsimport -p gov.nih.nlm.uts.webservice.history -d $GENERATED_DIR -keep https://uts-ws.nlm.nih.gov/services/nwsHistory?wsdl
wsimport -p gov.nih.nlm.uts.webservice.semnet -d $GENERATED_DIR -keep https://uts-ws.nlm.nih.gov/services/nwsSemanticNetwork?wsdl

# Delete the compiled class files
rm `find $GENERATED_DIR -name "*.class"`
