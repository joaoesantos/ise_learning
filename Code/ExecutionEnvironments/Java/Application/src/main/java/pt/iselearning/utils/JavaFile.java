package pt.iselearning.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class JavaFile {
    /**
     * Creates a Java file.
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
    }
}
