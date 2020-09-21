﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Arguments
{
    public class RunTests : ICommandArguments
    {
        private string baseDirectory;
        private string solutionFolder;
        private string solutionName;

        public RunTests(string baseDirectory, string solutionFolder, string solutionName)
        {
            this.baseDirectory = baseDirectory;
            this.solutionName = solutionName;
            this.solutionFolder = solutionFolder;
        }
        public string getArgumments()
        {
            return $"{baseDirectory} {solutionFolder} {solutionName}";
        }
    }
}
