using NetCoreExecutionEnvironment.Commands.Contracts;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Utils;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Facade
{
    public class CommandLinuxFacade : AbstractCommandFacade
    {
        public override Task<ExecutableResult> RunCode(string solutionDirectory, int timeout)
        {
            return Task.Run(() =>
            {
                return CommandLineUtils
                .ExecuteCommand($"{DetectOS.ConsolePromptArgumentStart} " + $"\"dotnet run --project {solutionDirectory}\"" , timeout);
            });
        }

        public override Task<ExecutableResult> RunTests(string solutionDirectory, int timeout)
        {
            return Task.Run(() =>
            {
                Console.WriteLine("sd:" + solutionDirectory);
                ExecutableResult res = CommandLineUtils
                .ExecuteCommand($"{DetectOS.ConsolePromptArgumentStart} " + @"""" + $"dotnet test {solutionDirectory}" + @"""", timeout);
                if (!res.WasError)
                {
                    int idx = res.RawResult.LastIndexOf("Microsoft");
                    res.RawResult = res.RawResult.Substring(idx);
                }

                return res;
            });
            
        }
    }
}
