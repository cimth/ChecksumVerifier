package org.example.utils;

import org.example.model.Checksum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Optional;

public class ChecksumComputer {

    /*==================================================*
     *==                   LOGGER                     ==*
     *==================================================*/

    private static final Logger LOGGER = LogManager.getLogger(ChecksumComputer.class);

    /*==================================================*
     *==                   FIELDS                     ==*
     *==================================================*/

    private Checksum checksum;

    /*==================================================*
     *==                CONSTRUCTORS                  ==*
     *==================================================*/

    public ChecksumComputer(Checksum checksum) {
        this.checksum = checksum;
    }

    /*==================================================*
     *==               PUBLIC METHODS                 ==*
     *==================================================*/

    /**
     * Computes the checksum for the given file and returns it as string.
     *
     * @param file the file for which the checksum is to be computed
     * @return the checksum as string or empty
     */
    public Optional<String> getChecksum(File file) {

        // init return value with empty for the case there occurs an error while processing the file
        Optional<String> checksum = Optional.empty();

        // get hash of file as byte array
        Optional<byte[]> hash = this.getByteChecksumFromFile(this.checksum.getAlgorithm(), file);

        // convert the byte array to the string to be returned
        if (hash.isPresent()) {
            checksum = Optional.of(this.convertByteArrayToHexString(hash.get()));
        }

        // return the checksum as string or empty if an error had occurred
        return checksum;
    }

    /**
     * Compares the checksum of the given file with the given target checksum.
     *
     * @param file the file for which the checksum should be verified
     * @param targetChecksum the should-be checksum
     * @return true or false when comparison succeeds, else empty
     */
    public Optional<Boolean> verifyChecksum(File file, String targetChecksum) {

        // init result with empty if an error occurs while comparing
        Optional<Boolean> result = Optional.empty();

        // compare if possible
        Optional<String> actualChecksum = getChecksum(file);
        if (actualChecksum.isPresent()) {
            result = Optional.of(targetChecksum.equals(actualChecksum.get()));
        }

        // return true or false when comparison succeeded, else empty
        return result;
    }

    /*==================================================*
     *==              PRIVATE METHODS                 ==*
     *==================================================*/

    /**
     * Computes the checksum with the given algorithm for the given file.
     *
     * @param algorithm the checksum algorithm, like SHA256
     * @param file the file for which the checksum is to be computed
     * @return the checksum as byte array or empty
     */
    private Optional<byte[]> getByteChecksumFromFile(String algorithm, File file) {

        // initialize return value with empty if an error occurs while processing the file
        Optional<byte[]> hash = Optional.empty();

        // process the file to build it's checksum as byte array
        try (InputStream in = new FileInputStream(file)) {

            // initialize needed components
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] curBlock = new byte[4096];
            int length;

            // go through each block and update the checksum for it
            while ((length = in.read(curBlock)) > 0) {
                md.update(curBlock, 0, length);
            }

            // save hash for further processing
            hash = Optional.of(md.digest());

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        // return actual hash or empty if an error had occured
        return hash;
    }

    /**
     * Converts the given byte array to it's hexadecimal string representation.
     *
     * @param bytes the array to convert
     * @return the hexadecimal string converted from the byte array
     */
    private String convertByteArrayToHexString(byte[] bytes) {

        // StringBuilder for more efficiency
        StringBuilder hexString = new StringBuilder();

        // convert each byte to it's hexadecimal representation
        for (byte b : bytes) {

            // calculate the hexadecimal representation of the current byte
            String hex = Integer.toHexString(0xFF & b);

            // add leading zero if needed
            if (hex.length() == 1) {
                hexString.append('0');
            }

            // add hexadecimal representation to result
            hexString.append(hex);
        }

        // return converted string
        return hexString.toString();
    }

}
