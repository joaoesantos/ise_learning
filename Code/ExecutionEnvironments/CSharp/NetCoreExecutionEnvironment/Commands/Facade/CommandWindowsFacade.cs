using Microsoft.AspNetCore.Http;
using NetCoreExecutionEnvironment.Commands.Contracts;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Exceptions;
using NetCoreExecutionEnvironment.Utils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Reflection.Metadata.Ecma335;
using System.Threading;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Facade
{
    public class CommandWindowsFacade : AbstractCommandFacade
    {
        public override Task<ExecutableResult> RunCode(string solutionDirectory, int timeout = Timeout.Infinite)
        {
            return Task.Run(() =>
            {
                return CommandLineUtils.ExecuteCommand($"dotnet run --project {solutionDirectory}", timeout);
            });

        }

        public async override Task<ExecutableResult> RunTests(string solutionDirectory, int timeout = Timeout.Infinite)
        {
            ExecutableResult res = await Task.Run(() =>
            {
                return CommandLineUtils.ExecuteCommand($"dotnet test {solutionDirectory}", timeout);
            });

            if (!res.WasError)
            {
                int idx = res.RawResult.LastIndexOf("Microsoft");
                res.RawResult = res.RawResult.Substring(idx);
            }

            return res;
        }
    }
}
