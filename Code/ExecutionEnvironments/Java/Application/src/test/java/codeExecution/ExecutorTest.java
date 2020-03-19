package codeExecution;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecutorTest {

    @Test
    public void testMergingClasspath() {
        String expectedClasspath = "D:\\Repositorios\\projeto_final\\Code\\ExecutionEnvironments\\Java\\Application\\" +
                ".\\main\\java\\server;D:\\Repositorios\\projeto_final\\Code\\ExecutionEnvironments\\Java\\" +
                "Application\\.\\main\\java\\exception;D:\\Repositorios\\projeto_final\\Code\\ExecutionEnvironments" +
                "\\Java\\Application\\.\\main\\java\\codeExecution";

        Path originalPath = Paths.get(".", "main", "java", "codeExecution");
        Path[] pathsList = new Path[]{
                Paths.get(".", "main", "java", "server"),
                Paths.get(".", "main", "java", "exception")
        };
        String mergedClasspath = Executor.mergeClasspathPaths(originalPath, pathsList);
        Assert.assertEquals(expectedClasspath, mergedClasspath);
    }
}
