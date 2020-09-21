﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Arguments
{
    public class CreateSolutionWithTests : ICommandArguments
    {
        private string baseDirectory;
        private string solutionName;
        private string appName;
        private string testAppName;

        public CreateSolutionWithTests(string baseDirectory, string solutionName, string appName, string testAppName)
        {
            this.baseDirectory = baseDirectory;
            this.solutionName = solutionName;
            this.appName = appName;
            this.testAppName = testAppName;
        }

        public string getArgumments()
        {
            return $"{baseDirectory} {solutionName} {appName} {testAppName}";
        }
    }
}