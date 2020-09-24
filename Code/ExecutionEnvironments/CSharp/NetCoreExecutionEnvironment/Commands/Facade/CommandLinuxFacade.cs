using NetCoreExecutionEnvironment.Commands.Contracts;
using NetCoreExecutionEnvironment.Contracts;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Facade
{
    public class CommandLinuxFacade : ICommandFacade
    {
        public Task<string> CopyBaseSolution(string directory, bool toBeDelete)
        {
            throw new NotImplementedException();
        }

        public Task<string> CreateDirectory(string directory)
        {
            throw new NotImplementedException();
        }

        public void Dispose()
        {
            throw new NotImplementedException();
        }

        public Task ReplaceProgramCode(string code, string filePath)
        {
            throw new NotImplementedException();
        }

        public Task ReplaceUnitTestsCode(string unitTests, string filePath)
        {
            throw new NotImplementedException();
        }

        public Task<ExecutableResult> RunCode(string solutionDirectory, int timeout)
        {
            throw new NotImplementedException();
        }

        public Task<ExecutableResult> RunTests(string solutionDirectory, int timeout)
        {
            throw new NotImplementedException();
        }
    }
}
