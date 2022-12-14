package org.example.utils;

import org.example.model.Checksum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;


public class ChecksumComputerTest {

    /*==================================================*
     *==                    FIELDS                    ==*
     *==================================================*/

    ChecksumComputer compMD5;
    ChecksumComputer compSHA1;
    ChecksumComputer compSHA256;
    ChecksumComputer compSHA512;

    /*==================================================*
     *==                    SETUP                     ==*
     *==================================================*/

    @BeforeEach
    public void setUp() {
        compMD5 = new ChecksumComputer(Checksum.MD5);
        compSHA1 = new ChecksumComputer(Checksum.SHA1);
        compSHA256 = new ChecksumComputer(Checksum.SHA256);
        compSHA512 = new ChecksumComputer(Checksum.SHA512);
    }

    /*==================================================*
     *==                getChecksum()                 ==*
     *==================================================*/

    @Test
    public void getChecksum_GiveInvalidFile_ReturnEmpty() {

        // test file, not existing
        Path pathToNonExisting = Path.of("./not-existing");
        if (Files.exists(pathToNonExisting)) {
            throw new AssertionError("Test file must not exist!");
        }
        File file = pathToNonExisting.toFile();

        // test each possible checksum algorithm
        for (Checksum alg : Checksum.values()) {
            Optional<String> checksum = new ChecksumComputer(alg).getChecksum(file);
            Assertions.assertTrue(checksum.isEmpty());
            Assertions.assertEquals(Optional.empty(), checksum);
        }
    }

    @Test
    public void getChecksum_GiveValidFile_ReturnCorrectChecksums() {

        // test file, must exist (or throws an exception)
        URL fileUrl = getClass().getClassLoader().getResource("test_checksum.txt");
        if (fileUrl == null) {
            throw new AssertionError("Test file not found!");
        }
        File file = new File(fileUrl.getFile());

        // MD5
        Optional<String> checksum = compMD5.getChecksum(file);

        Assertions.assertTrue(checksum.isPresent());
        Assertions.assertEquals("d41d8cd98f00b204e9800998ecf8427e", checksum.get());

        // SHA1
        checksum = compSHA1.getChecksum(file);

        Assertions.assertTrue(checksum.isPresent());
        Assertions.assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", checksum.get());

        // SHA256
        checksum = compSHA256.getChecksum(file);

        Assertions.assertTrue(checksum.isPresent());
        Assertions.assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", checksum.get());

        // SHA512
        checksum = compSHA512.getChecksum(file);

        Assertions.assertTrue(checksum.isPresent());
        Assertions.assertEquals("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e", checksum.get());
    }

    /*==================================================*
     *==               verifyChecksum()               ==*
     *==================================================*/

    @Test
    public void verifyChecksum_GiveComputedChecksum_ReturnTrue() {

        // test file, must exist (or throws an exception)
        URL fileUrl = getClass().getClassLoader().getResource("test_checksum.txt");
        if (fileUrl == null) {
            throw new AssertionError("Test file not found!");
        }
        File file = new File(fileUrl.getFile());

        // MD5
        Optional<String> checksum = compMD5.getChecksum(file);
        Assertions.assertTrue(checksum.isPresent());

        Optional<Boolean> correctChecksum = compMD5.verifyChecksum(file, checksum.get());

        Assertions.assertTrue(correctChecksum.isPresent());
        Assertions.assertTrue(correctChecksum.get());

        // SHA1
        checksum = compSHA1.getChecksum(file);
        Assertions.assertTrue(checksum.isPresent());

        correctChecksum = compSHA1.verifyChecksum(file, checksum.get());

        Assertions.assertTrue(correctChecksum.isPresent());
        Assertions.assertTrue(correctChecksum.get());

        // SHA256
        checksum = compSHA256.getChecksum(file);
        Assertions.assertTrue(checksum.isPresent());

        correctChecksum = compSHA256.verifyChecksum(file, checksum.get());

        Assertions.assertTrue(correctChecksum.isPresent());
        Assertions.assertTrue(correctChecksum.get());

        // SHA512
        checksum = compSHA512.getChecksum(file);
        Assertions.assertTrue(checksum.isPresent());

        correctChecksum = compSHA512.verifyChecksum(file, checksum.get());

        Assertions.assertTrue(correctChecksum.isPresent());
        Assertions.assertTrue(correctChecksum.get());
    }

    @Test
    public void verifyChecksum_GiveInvalidChecksum_ReturnFalse() {

        // test file, must exist (or throws an exception)
        URL fileUrl = getClass().getClassLoader().getResource("test_checksum.txt");
        if (fileUrl == null) {
            throw new AssertionError("Test file not found!");
        }
        File file = new File(fileUrl.getFile());

        // set invalid checksum (correct is a hexadecimal string)
        Optional<String> invalidChecksum = Optional.of("invalid");

        // MD5
        Optional<Boolean> falseChecksum = compMD5.verifyChecksum(file, invalidChecksum.get());

        Assertions.assertTrue(falseChecksum.isPresent());
        Assertions.assertFalse(falseChecksum.get());

        // SHA1
        falseChecksum = compSHA1.verifyChecksum(file, invalidChecksum.get());

        Assertions.assertTrue(falseChecksum.isPresent());
        Assertions.assertFalse(falseChecksum.get());

        // SHA256
        falseChecksum = compSHA256.verifyChecksum(file, invalidChecksum.get());

        Assertions.assertTrue(falseChecksum.isPresent());
        Assertions.assertFalse(falseChecksum.get());

        // SHA512
        falseChecksum = compSHA512.verifyChecksum(file, invalidChecksum.get());

        Assertions.assertTrue(falseChecksum.isPresent());
        Assertions.assertFalse(falseChecksum.get());
    }
}
