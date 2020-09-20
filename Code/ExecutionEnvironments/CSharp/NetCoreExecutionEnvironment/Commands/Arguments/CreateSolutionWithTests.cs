using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Arguments
{
    public class CreateSolutionWithTests : ICommandArguments
    {
        private string baseDirectory;
        private string uuid;
        private string appName;
        private string testAppName;
        private string codeContent;
        private string testsContent;

        public CreateSolutionWithTests(string baseDirectory, string uuid, string appName, string testAppName, string codeContent, string testsContent)
        {
            this.baseDirectory = baseDirectory;
            this.uuid = uuid;
            this.appName = appName;
            this.testAppName = testAppName;
            this.codeContent = codeContent;
            this.testsContent = testsContent;
        }

        public string getArgumments()
        {
            return $"{baseDirectory} {uuid} {appName} {testAppName} {codeContent} {testsContent}";
        }
    }
}
