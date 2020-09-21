using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Arguments
{
    public class RunCode : ICommandArguments
    {
        private string baseDirectory;
        private string solutionFolder;
        private string solutionName;
        private string projectName;

        public RunCode(string baseDirectory, string solutionFolder, string solutionName, string projectName)
        {
            this.baseDirectory = baseDirectory;
            this.solutionName = solutionName;
            this.solutionFolder = solutionFolder;
            this.projectName = projectName;
        }
        public string getArgumments()
        {
            return $"{baseDirectory} {solutionFolder} {solutionName} {projectName}";
        }
    }
}
