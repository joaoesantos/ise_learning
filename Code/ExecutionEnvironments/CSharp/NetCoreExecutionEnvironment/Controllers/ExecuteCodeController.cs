using System;
using System.IO;
using Microsoft.AspNetCore.Mvc;
using NetCoreExecutionEnvironment.Commands;
using NetCoreExecutionEnvironment.Commands.Arguments;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Utils;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace NetCoreExecutionEnvironment.Controllers
{

    [Route("")]
    [ApiController]
    public class ExecuteCodeController : ControllerBase
    {

        // POST
        [HttpPost]
        public ActionResult<ExecutableResult> Post([FromBody] Executable value)
        {
            Guid guid = new Guid();
            DirectoryInfo di = Directory.GetParent(Directory.GetCurrentDirectory());
            string solutionFolder = $"{Constants.SolutionBaseFolder}_{guid}";

            string cleanedContent = FileUtils.RemoveNewLines(value.code);
            string testContent = FileUtils.RemoveNewLines(value.unitTests);

            CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandCreateSolution, new CreateSolutionWithTests(di.FullName, Constants.TemplateFolder, solutionFolder));
            DocumentManager.WriteFile(cleanedContent, Constants.BaseCodeFile, Path.Combine(di.FullName, solutionFolder, Constants.SolutionName, Constants.CodeProjectName));

            ExecutableResult result;
            if (!value.executeTests)
            {
                // run code
                result = CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandRunCode, new RunCode(di.FullName, solutionFolder, Constants.SolutionName, Constants.CodeProjectName));

            }
            else
            {
                DocumentManager.WriteFile(testContent, Constants.UnitTestFile, Path.Combine(di.FullName, solutionFolder, Constants.SolutionName, Constants.UnitTestsProjectName));
                result = CommandLineUtils.ExecuteCommandFile(CommandOS.Instance.CommandRunTests, new RunTests(di.FullName, solutionFolder, Constants.SolutionName));
            }

            return Ok(result);
        }

    }
}
