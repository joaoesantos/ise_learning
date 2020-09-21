using Microsoft.AspNetCore.Http;
using NetCoreExecutionEnvironment.Commands.Arguments;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Exceptions;
using System;
using System.Diagnostics;
using System.IO;
using System.Reflection;
using System.Threading;
using static NetCoreExecutionEnvironment.Constants;

namespace NetCoreExecutionEnvironment.Utils
{
    public class CommandLineUtils
    {
        private static readonly string _commandFilesFolder = Path.Combine("Commands", "Files");

        public static ExecutableResult ExecuteCommandFile(CommandType ct, ICommandArguments commandArguments, int timeout = Timeout.Infinite)
        {
            try
            {
                string filePath = Path.Combine(Directory.GetCurrentDirectory(), _commandFilesFolder, ct.GetFileName());
                //launch new process
                using(Process np = new Process())
                {
                    np.StartInfo.UseShellExecute = false;
                    np.StartInfo.FileName = filePath;
                    np.StartInfo.CreateNoWindow = true;
                    np.StartInfo.Arguments = commandArguments.getArgumments();
                    np.StartInfo.RedirectStandardError = true;
                    np.StartInfo.RedirectStandardOutput = true;
                    np.Start();

                    bool ok = np.WaitForExit(timeout);
                    if (!ok)
                    {
                        throw new RunEnvironmentException(StatusCodes.Status408RequestTimeout, Constants.ExceptionType.REQUEST_TIMEOUT, "Program ran for too long. Please make changes in your code", Constants.ExceptionInstance.TIMEOUT, "Program ran for too long");
                    }
                    long runningTime = Convert.ToInt64((np.ExitTime - np.StartTime).TotalMilliseconds);
                    string result = ""; 
                    bool wasError = false;

                    //indicates that everything went ok
                    if (np.ExitCode == 0)
                    {
                        result = np.StandardOutput.ReadToEnd();
                    }
                    else
                    {
                        wasError = true;
                        result = np.StandardError.ReadToEnd();
                    }

                    return new ExecutableResult(result, wasError, runningTime);
                }
            }
            catch (Exception e)
            {
                throw new RunEnvironmentException(StatusCodes.Status500InternalServerError, Constants.ExceptionType.INTERNAL_SERVER_ERROR, e.Message, Constants.ExceptionInstance.COMMAND_ERROR, "Error while executing command files");
            }
        }
    }
}
