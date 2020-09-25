using Microsoft.AspNetCore.Http;
using NetCoreExecutionEnvironment.Commands.Contracts;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Exceptions;
using System;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Facade
{
    public abstract class AbstractCommandFacade : ICommandFacade
    {
        private List<ExecEnvironmentDirectory> _directoriesCreated;
        private static object _lock = new object();
        public async Task<string> CopyBaseSolution(string directory, bool toBeDelete)
        {
            if (_directoriesCreated == null)
            {
                _directoriesCreated = new List<ExecEnvironmentDirectory>();
            }

            DirectoryInfo di = Directory.GetParent(Directory.GetCurrentDirectory());
            string templateFolder = Path.Combine(di.FullName, Constants.TemplateFolder);
            await DocumentManager.DocumentManager.DirectoryCopy(templateFolder, directory);
            lock (_lock)
            {
                _directoriesCreated.Add(new ExecEnvironmentDirectory(directory, toBeDelete));
            }

            return Path.Combine(directory, Constants.SolutionName);
        }

        public Task<string> CreateDirectory(string directory)
        {
            return Task.Run(() =>
            {
                return Directory.CreateDirectory(directory).FullName;
            }); ;
        }

        public void Dispose()
        {
            Task.Run(() =>
            {
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
                catch (Exception e)
                {
                    //Ignored
                }
            });
        }

        public abstract Task<ExecutableResult> RunCode(string solutionDirectory, int timeout);

        public abstract Task<ExecutableResult> RunTests(string solutionDirectory, int timeout);
    }
}
