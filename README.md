# ChecksumVerifier

## Introduction

This application lets the user compare the checksum of a selectable file with its target checksum inside a JavaFX GUI.
The available algorithms for computing the checksum are the following:

* MD5
* SHA1
* SHA256
* SHA512

## Screenshot

![Screenshot](Screenshot.png)

## Included dependencies

Consider [pom.xml](pom.xml) for more detailed information.

| Dependency            | Version |
|-----------------------|---------|
| Java                  | 17      |
| JavaFX                | 17.0.2  |
| Log4j                 | 2.18.0  |
| JUnit                 | 5.9.0   |
| Maven Compiler Plugin | 3.10.1  |
| Maven Assembly Plugin | 3.4.2   |
| Versions Maven Plugin | 2.11.0  |
| JavaFX Maven Plugin   | 0.0.8   |

## Structure

As the entry point for this application a  separate `Start` class is used for making the JAR created with the Maven 
goal `package` work correctly.
This class only contains the main method and calls the _launch()_ method of the `App` class which
will initialize the actual application.

The application bases on the MVC pattern where the view is located in the directory
_src/main/resources/org.example.fxml_. 
Since there is only one model and controller class those classes are not described here in more detail.

The correct language bundle is chosen in the `initLanguage()` method inside the `App` class.
Currently, the GUI supports German and English.


## Run the application via Maven

1. Set the correct `javafx-graphics` classifier (`win`, `linux` or `mac`) inside [pom.xml](pom.xml)
2. Run `mvn javafx:run`

## Build and start JAR

1. Run `mvn clean package`
2. Run `java -jar ./target/ChecksumVerifier.jar`