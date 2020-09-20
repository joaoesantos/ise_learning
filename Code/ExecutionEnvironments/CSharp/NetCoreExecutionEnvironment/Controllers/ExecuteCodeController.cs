using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using NetCoreExecutionEnvironment.Commands.Arguments;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Utils;
using static NetCoreExecutionEnvironment.Constants;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace NetCoreExecutionEnvironment.Controllers
{

    [Route("")]
    [ApiController]
    public class ExecuteCodeController : ControllerBase
    {

        // POST
        [HttpPost]
        public ExecutableResult Post([FromBody] Executable value)
        {
            Guid guid = new Guid();
            string cleanedContent = FileUtils.RemoveNewLines(value.code);
            DirectoryInfo di = Directory.GetParent(Directory.GetCurrentDirectory());
            CommandInfo ci = null;

            if (!value.executeTests)
            {
                // run code
                if (RuntimeUtils.IsWindows)
                {
                    ci = CommandLineUtils.ExecuteCommandFile(CommandType.CREATE_SOLUTION_WINDOWS_WITH_TESTS, new CreateSolutionWithTests(di.FullName, guid.ToString(), "App", "AppTest", cleanedContent, ""));
                    Console.WriteLine("time:" + ci.ProcessDuration + ",std:" + ci.StandardOutput + ",wasError:" + ci.WasError);
                }
            }
            else
            {
                //create tests file
                string testContent = FileUtils.RemoveNewLines(value.unitTests);
                if (RuntimeUtils.IsWindows)
                {
                    ci = CommandLineUtils.ExecuteCommandFile(CommandType.CREATE_SOLUTION_WINDOWS_WITH_TESTS, new CreateSolutionWithTests(di.FullName, guid.ToString(), "App", "AppTest", cleanedContent, testContent));
                    Console.WriteLine("time:" + ci.ProcessDuration + ",std:" + ci.StandardOutput + ",wasError:" + ci.WasError);
                }
            }

            return new ExecutableResult(ci.StandardOutput, ci.WasError, ci.ProcessDuration);
        }

    }
}
