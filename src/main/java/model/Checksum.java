package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Checksum {

    /*==================================================*
     *==                ENUM VALUES                   ==*
     *==================================================*/

    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    /*==================================================*
     *==                   LOGGER                     ==*
     *==================================================*/

    private static final Logger LOGGER = LogManager.getLogger(Checksum.class);

    /*==================================================*
     *==                   FIELDS                     ==*
     *==================================================*/

    private String algorithm;

    /*==================================================*
     *==                CONSTRUCTORS                  ==*
     *==================================================*/

    Checksum(String algorithm) {
        this.algorithm = algorithm;
    }

    /*==================================================*
     *==                 ACCESSORS                    ==*
     *==================================================*/

    public String getAlgorithm() {
        return algorithm;
    }
}
