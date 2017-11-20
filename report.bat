rem release the archive file or document to the certain directory
mvn -f .\dbx-sdk\ clean package

rem copy archive file to report directory
xcopy .\dbx-sdk\target\dbx-sdk-0.0.1.jar .\release\archive\0.0.1\

rem release the api document
mvn -f .\dbx-sdk javadoc:javadoc