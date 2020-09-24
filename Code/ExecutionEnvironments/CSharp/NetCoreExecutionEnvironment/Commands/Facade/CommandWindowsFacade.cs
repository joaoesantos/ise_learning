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
    public class CommandWindowsFacade : ICommandFacade
    {
        private List<ExecEnvironmentDirectory> _directoriesCreated;
        private static object _lock = new object();
        public CommandWindowsFacade()
        {

        }

        public async Task<string> CopyBaseSolution(string directory, bool toBeDelete)
        {
            if (_directoriesCreated == null)
            {
                _directoriesCreated = new List<ExecEnvironmentDirectory>();
            }

            DirectoryInfo di = Directory.GetParent(Directory.GetCurrentDirectory());
            string templateFolder = Path.Combine(di.FullName, Constants.TemplateFolder);
            await new DocumentManager().DirectoryCopy(templateFolder, directory);
            lock (_lock)
            {
                _directoriesCreated.Add(new ExecEnvironmentDirectory(directory, toBeDelete));
            }
            
            return Path.Combine(directory, Constants.SolutionName);

        }

        public Task<string> CreateDirectory(string directory)
        {
            throw new NotImplementedException();
        }

        public void Dispose()
        {
            new Thread(() => {
                try
                {
                        if (_directoriesCreated.Count > 0)
                        {
                            lock (_lock)
                            {
                                foreach (ExecEnvironmentDirectory dir in _directoriesCreated)
                                {
                                    if (dir.DeleteAfterUse)
                                    {
                                        _directoriesCreated.Remove(dir);
                                        Directory.Delete(dir.Directory, true);
                                    }
                                }
                            }
                        }

                }
                catch (Exception)
                {
                    //Ignored
                    //throw new RunEnvironmentException(StatusCodes.Status500InternalServerError, Constants.ExceptionType.INTERNAL_SERVER_ERROR, e.Message, Constants.ExceptionInstance.FILE_SYSTEM, "Error while deleting folders");
                }
            }).Start();
        }

        public Task<ExecutableResult> RunCode(string solutionDirectory, int timeout = Timeout.Infinite)
        {
            return Task.Run(() =>
            {
                return CommandLineUtils.ExecuteCommand($"dotnet run --project {solutionDirectory}", timeout);
            });

        }

        public Task<ExecutableResult> RunTests(string solutionDirectory, int timeout = Timeout.Infinite)
        {
            return Task.Run(() =>
            {
                return CommandLineUtils.ExecuteCommand($"dotnet test {solutionDirectory}", timeout);
            });
        }

        public Task ReplaceProgramCode(string code, string filePath)
        {
            throw new NotImplementedException();
        }

        public Task ReplaceUnitTestsCode(string unitTests, string filePath)
        {
            throw new NotImplementedException();
        }
    }
}
