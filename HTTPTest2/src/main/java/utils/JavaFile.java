package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class JavaFile {

    public static void createJavaFile(Path fullPathToFile, String fileContent) throws IOException {
        try(FileOutputStream outputStream = new FileOutputStream(fullPathToFile.toString())) {
            byte[] strToBytes = fileContent.getBytes();
            outputStream.write(strToBytes);
        }
    }
}
