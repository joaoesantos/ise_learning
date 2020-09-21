using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Arguments
{
    public class CreateSolutionWithTests : ICommandArguments
    {
        private string baseDirectory;
        private string templateDirectory;
        private string newDirectory;
 

        public CreateSolutionWithTests(string baseDirectory, string templateDirectory, string newDirectory)
        {
            this.baseDirectory = baseDirectory;
            this.templateDirectory = templateDirectory;
            this.newDirectory = newDirectory;
        }

        public string getArgumments()
        {
            return $"{baseDirectory} {templateDirectory} {newDirectory}";
        }
    }
}
