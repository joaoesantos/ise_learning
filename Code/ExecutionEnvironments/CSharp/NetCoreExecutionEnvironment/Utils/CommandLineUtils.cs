using NetCoreExecutionEnvironment.Commands.Arguments;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using static NetCoreExecutionEnvironment.Constants;

namespace NetCoreExecutionEnvironment.Utils
{
    public class CommandLineUtils
    {
        private static readonly string _commandFilesFolder = Path.Combine("Commands", "Files");
        public static CommandInfo ExecuteCommandFile(CommandType ct, ICommandArguments commandArguments)
        {
            string filePath = Path.Combine(Directory.GetCurrentDirectory(), _commandFilesFolder, ct.GetFileName());
            try
            {
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

                    np.WaitForExit();
                    long runningTime = (np.ExitTime - np.StartTime).Ticks;
                    string result = ""; 
                    bool wasError = false;

                    //indicates if everything went ok
                    if (np.ExitCode != 0)
                    {
                        result = np.StandardOutput.ReadToEnd();
                    }
                    else
                    {
                        wasError = true;
                        result = np.StandardError.ReadToEnd();
                    }

                    return new CommandInfo(runningTime, result, wasError);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Error executing command File");
                Console.WriteLine(e.Message);
                throw e;
            }
        }
    }
}
