using Microsoft.AspNetCore.Http;
using NetCoreExecutionEnvironment.Commands;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Exceptions;
using System;
using System.Diagnostics;
using System.Threading;

namespace NetCoreExecutionEnvironment.Utils
{
    public class CommandLineUtils
    {
        /// <summary>
        /// Execute commands using system commant line
        /// </summary>
        /// <param name="args">Command to be ran in the command line
        /// Example windows: "/C echo hello"
        /// Example linux: "-c echo \""echo hello\"""
        /// </param>
        /// <param name="timeout">Timeout for command. Infinite by default</param>
        /// <returns>Instance of ExecutableResult</returns>
        public static ExecutableResult ExecuteCommand(string args, int timeout = Timeout.Infinite)
        {
            try
            {
                Stopwatch stopwatch = new Stopwatch();
                using (Process np = new Process())
                {
                    np.StartInfo.UseShellExecute = false;
                    np.StartInfo.FileName = DetectOS.ConsolePrompt;
                    np.StartInfo.CreateNoWindow = true;
                    np.StartInfo.Arguments = args;
                    np.StartInfo.RedirectStandardError = true;
                    np.StartInfo.RedirectStandardOutput = true;
                    np.Start();
                    stopwatch.Start();
                    string result = np.StandardOutput.ReadToEnd();
                    string errorText = np.StandardError.ReadToEnd();

                    bool ok = np.WaitForExit(timeout);
                    stopwatch.Stop();
                    if (!ok)
                    {
                        throw new RunEnvironmentException(StatusCodes.Status408RequestTimeout, Constants.ExceptionType.REQUEST_TIMEOUT, "Program ran for too long. Please make changes in your code", Constants.ExceptionInstance.TIMEOUT, "Program ran for too long");
                    }

                    bool wasError = np.ExitCode != 0;

                    return new ExecutableResult(wasError ? errorText : result, wasError, stopwatch.ElapsedMilliseconds);
                }
            }
            catch (Exception e)
            {
                throw new RunEnvironmentException(StatusCodes.Status500InternalServerError, Constants.ExceptionType.INTERNAL_SERVER_ERROR, e.Message, Constants.ExceptionInstance.COMMAND_ERROR, "Error while executing command files");
            }
        }
    }

   
}
