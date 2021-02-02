# Introduction

This application lets the user compare the checksum of a selectable file with its target checksum inside a JavaFX GUI.
The available algorithms for computing the checksum are the following:

* MD5
* SHA1
* SHA256
* SHA512

# Structure

This application bases on the MVC pattern where the view is located in the directory
_src/main/resources/org.example.fxml_.

The correct language bundle is chosen in the `initLanguage()` method inside the `App` class
which marks the entry point of the application.
Currently, the GUI supports German and English.

A separate `Start` class is needed for making the JAR created with the Maven goal `package` correctly. 
This class only contains the main method and calls the _launch()_ method of the `App` class which 
will initialize the actual application.

# Build and start JAR

1. Execute the Maven goal `package`
2. The JAR file _.target/ChecksumVerifier-1.0-SNAPSHOT.jar_ includes all dependencies and can directly
   be started with a double click if Java 11 is installed on the device