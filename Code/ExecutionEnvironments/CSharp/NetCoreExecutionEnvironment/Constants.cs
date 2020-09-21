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
        public static string SolutionBaseFolder = "Exercise_";
        public static string SolutionName = "RunCode";
        public static string CodeProjectName = "App";
        public static string UnitTestsProjectName = "UnitTests";
        public static string TemplateFolder = "TemplateFolder";
        public enum CommandType
        {    
            CREATE_SOLUTION_WINDOWS,
            CREATE_SOLUTION_LINUX,
            EXECUTE_TESTS_WINDOWS,
            EXECUTE_TESTS_LINUX,
            EXECUTE_CODE_WINDOWS,
            EXECUTE_CODE_LINUX
        }

    }
}
