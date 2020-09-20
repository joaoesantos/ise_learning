using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.NetworkInformation;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class Constants
    {
        public static string CodeFilesDirectory = "CodeFiles";
        public static string BaseCodeFile = "Program.cs";
        public static string UnitTestFile = "UnitTest1.cs";
        public static string SolutionBaseName = "Exercise_";
        public static string CodeProjectName = "App";
        public static string UnitTestsProjectName = "AppTests";
        public enum CommandType
        {
            CREATE_SOLUTION_WINDOWS_WITH_TESTS,
            CREATE_SOLUTION_LINUX_WITH_TESTS,        
            CREATE_SOLUTION_WINDOWS_WITHOUT_TESTS,
            CREATE_SOLUTION_LINUX_WITHOUT_TESTS,
            EXECUTE_TESTS_WINDOWS,
            EXECUTE_TESTS_LINUX,
            EXECUTE_CODE_WINDOWS,
            EXECUTE_CODE_LINUX
        }

    }
}
