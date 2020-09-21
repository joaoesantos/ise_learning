using System;
using System.IO;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using NetCoreExecutionEnvironment.Commands;
using NetCoreExecutionEnvironment.Commands.Arguments;
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

        public ExecuteCodeController(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        // POST
        [HttpPost]
        public ActionResult<ExecutableResult> Post([FromBody] Executable value)
        {
            Guid guid = new Guid();

            int timeout = Convert.ToInt32(Configuration["TimeoutTime"]);
            DirectoryInfo di;
            try
            {
              di = Directory.GetParent(Directory.GetCurrentDirectory());
            } catch(Exception e)
            {
                throw new RunEnvironmentException(StatusCodes.Status500InternalServerError, Constants.ExceptionType.INTERNAL_SERVER_ERROR, e.Message, Constants.ExceptionInstance.CONTROLLER_FILE_SYSTEM, "Error while using file system");
            }
             
            string solutionFolder = $"{Constants.SolutionBaseFolder}_{guid}";

            string cleanedContent = FileUtils.RemoveNewLines(value.code);
            string testContent = FileUtils.RemoveNewLines(value.unitTests);

            CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandCreateSolution, new CreateSolutionWithTests(di.FullName, Constants.TemplateFolder, solutionFolder));
            DocumentManager.WriteFile(cleanedContent, Constants.BaseCodeFile, Path.Combine(di.FullName, solutionFolder, Constants.SolutionName, Constants.CodeProjectName));

            ExecutableResult result;
            if (!value.executeTests)
            {
                // run code
                result = CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandRunCode, new RunCode(di.FullName, solutionFolder, Constants.SolutionName, Constants.CodeProjectName), timeout);

            }
            else
            {
                DocumentManager.WriteFile(testContent, Constants.UnitTestFile, Path.Combine(di.FullName, solutionFolder, Constants.SolutionName, Constants.UnitTestsProjectName));
                result = CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandRunTests, new RunTests(di.FullName, solutionFolder, Constants.SolutionName), timeout);
            }

            return Ok(result);
        }

    }
}
