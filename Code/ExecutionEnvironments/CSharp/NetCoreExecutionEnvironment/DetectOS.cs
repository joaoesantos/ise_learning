using Microsoft.Extensions.DependencyInjection;
using NetCoreExecutionEnvironment.Commands.Contracts;
using NetCoreExecutionEnvironment.Commands.Facade;
using NetCoreExecutionEnvironment.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class DetectOS
    {

        public static string ConsolePrompt { get; private set; }
        public static string ConsolePromptArgumentStart { get; private set; }

        public static Guid guid = new Guid();

        public static void RegisterCommandFacade(IServiceCollection services)
        {
            if (RuntimeUtils.IsWindows)
            {
                ConsolePrompt = "cmd.exe";
                ConsolePromptArgumentStart = "/C";
                services.AddScoped<ICommandFacade, CommandWindowsFacade>();
                return;
            }
            
            if (RuntimeUtils.IsLinux || RuntimeUtils.IsMacOs)
            {
                ConsolePrompt = @"/bin/bash";
                ConsolePromptArgumentStart = "-c";
                services.AddScoped<ICommandFacade, CommandLinuxFacade>();
                return;
            }

            throw new Exception("Operating System not supported");

        }
    }
}
