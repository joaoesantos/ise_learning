using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Arguments
{
    public class RunTests : ICommandArguments
    {
        private string baseDirectory;
        private string solutionName;

        public RunTests(string baseDirectory, string solutionName)
        {
            this.baseDirectory = baseDirectory;
            this.solutionName = solutionName;
        }
        public string getArgumments()
        {
            return $"{baseDirectory} {solutionName}";
        }
    }
}
