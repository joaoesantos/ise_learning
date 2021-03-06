﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.NetworkInformation;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class Constants
    {
        public static string BaseCodeFile = "Program.cs";
        public static string UnitTestFile = "UnitTest1.cs";
        public static string LibraryFile = "Class1.cs";
        public static string SolutionBaseFolder = "Exercise_";
        public static string SolutionName = "RunCode";
        public static string CodeProjectName = "App";
        public static string LibraryProjectName = "Library";
        public static string UnitTestsProjectName = "UnitTests";
        public static string TemplateFolder = "TemplateFolder";

        public class ExceptionType {
            public static string INTERNAL_SERVER_ERROR = "Internal Server Error";
            public static string REQUEST_TIMEOUT = "Request timedout";
        }

        public class ExceptionInstance
        {
            public static string TIMEOUT = "/execute/dotnet/timeout";
            public static string COMMAND_ERROR = "/execute/dotnet/command";
            public static string FILE_SYSTEM = "/execute/dotnet/filesystem";
            public static string CONTROLLER_FILE_SYSTEM = "/execute/dotnet/controller/filesystem";
        }

    }
}
