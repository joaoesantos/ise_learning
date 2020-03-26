package pt.iselearning.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class KotlinFile {
    private static final Logger LOGGER = LogManager.getLogger(KotlinFile.class);

    /**
     * Creates a Kotlin file.
     *
     * @param fullPathToFile
     * @param fileContent
     * @throws IOException
     */
    public static void createJavaFile(Path fullPathToFile, String fileContent) throws IOException {
        File folder = fullPathToFile.getParent().toFile();
        if(!folder.exists()) {
            folder.mkdirs();
        }
        try(FileOutputStream outputStream = new FileOutputStream(fullPathToFile.toString())) {
            byte[] strToBytes = fileContent.getBytes();
            outputStream.write(strToBytes);
        }
        LOGGER.info(String.format("Kotlin file %s created.", fullPathToFile));
    }
}
