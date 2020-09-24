using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using NetCoreExecutionEnvironment.Commands;
using NetCoreExecutionEnvironment.Commands.Contracts;
using NetCoreExecutionEnvironment.Commands.Facade;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Exceptions;
using NetCoreExecutionEnvironment.Utils;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace NetCoreExecutionEnvironment.Controllers
{

    [Route("")]
    [ApiController]
    public class ExecuteCodeController : ControllerBase
    {
        private IConfiguration Configuration;
        private ICommandFacade CommandFacade;

        public ExecuteCodeController(IConfiguration configuration, ICommandFacade commandFacade)
        {
            Configuration = configuration;
            CommandFacade = commandFacade;
        }

        // POST
        [HttpPost]
        public async Task<ActionResult<ExecutableResult>> Post([FromBody] Executable value)
        {
            //Guid guid = new Guid();
            ExecutableResult result;
            int timeout = Convert.ToInt32(Configuration["TimeoutTime"]);
            DirectoryInfo di;
            try
            {
              di = Directory.GetParent(Directory.GetCurrentDirectory());
            } catch(Exception e)
            {
                throw new RunEnvironmentException(StatusCodes.Status500InternalServerError, Constants.ExceptionType.INTERNAL_SERVER_ERROR, e.Message, Constants.ExceptionInstance.CONTROLLER_FILE_SYSTEM, "Error while using file system");
            }
             
            string solutionFolder = $"{Constants.SolutionBaseFolder}_{DetectOS.guid}";

            string cleanedContent = FileUtils.RemoveNewLines(value.code);
            string testContent = FileUtils.RemoveNewLines(value.unitTests);

            using (CommandFacade)
            {
                string solutionPath = await CommandFacade.CopyBaseSolution(Path.Combine(di.FullName, solutionFolder), false);
                if (!value.executeTests)
                {
                    result = await CommandFacade.RunCode(Path.Combine(solutionPath, Constants.CodeProjectName), timeout);
                } else
                {
                    result = await CommandFacade.RunTests(Path.Combine(solutionPath, Constants.UnitTestsProjectName), timeout);
                }
                
            }

            //Console.WriteLine("Exec 1");
            //CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandCreateSolution, new CreateSolutionWithTests(di.FullName, Constants.TemplateFolder, solutionFolder));
            //ExecutableResult result;
            //using (DocumentManager dm = new DocumentManager(Path.Combine(di.FullName, solutionFolder), true))
            //{
            //    dm.WriteFile(cleanedContent, Constants.BaseCodeFile, Path.Combine(di.FullName, solutionFolder, Constants.SolutionName, Constants.CodeProjectName));


            //    if (!value.executeTests)
            //    {
            //        Console.WriteLine("Exec 2");
            //        // run code
            //        result = CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandRunCode, new RunCode(di.FullName, solutionFolder, Constants.SolutionName, Constants.CodeProjectName), timeout);

            //    }
            //    else
            //    {
            //        Console.WriteLine("Exec 3");
            //        dm.WriteFile(testContent, Constants.UnitTestFile, Path.Combine(di.FullName, solutionFolder, Constants.SolutionName, Constants.UnitTestsProjectName));
            //        result = CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandRunTests, new RunTests(di.FullName, solutionFolder, Constants.SolutionName), timeout);
            //    }
            //}


            return Ok(result);
        }

    }
}
