package utils;

import model.Checksum;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChecksumComputerTest {

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
            Optional<String> checksum = ChecksumComputer.getChecksum(alg, file);
            assertTrue(checksum.isEmpty());
            assertEquals(Optional.empty(), checksum);
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
        Optional<String> checksum = ChecksumComputer.getChecksum(Checksum.MD5, file);

        assertTrue(checksum.isPresent());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", checksum.get());

        // SHA1
        checksum = ChecksumComputer.getChecksum(Checksum.SHA1, file);

        assertTrue(checksum.isPresent());
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", checksum.get());

        // SHA256
        checksum = ChecksumComputer.getChecksum(Checksum.SHA256, file);

        assertTrue(checksum.isPresent());
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", checksum.get());

        // SHA512
        checksum = ChecksumComputer.getChecksum(Checksum.SHA512, file);

        assertTrue(checksum.isPresent());
        assertEquals("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e", checksum.get());
    }
}
