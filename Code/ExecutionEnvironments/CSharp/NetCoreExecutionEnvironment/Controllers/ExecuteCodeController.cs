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
        public ActionResult<ExecutableResult> Post([FromBody] Executable value)
        {
            Guid guid = new Guid();
            string cleanedContent = FileUtils.RemoveNewLines(value.code);
            DirectoryInfo di = Directory.GetParent(Directory.GetCurrentDirectory());
            ExecutableResult result = null;
            string solutionName = $"{Constants.SolutionBaseName}_{guid}";
            string testContent = string.Empty;

            if (RuntimeUtils.IsWindows)
            {
                if (!value.executeTests)
                {
                    // run code
                    result = CommandLineUtils.ExecuteCommandFile(CommandType.CREATE_SOLUTION_WINDOWS_WITH_TESTS, new CreateSolutionWithTests(di.FullName, solutionName, Constants.CodeProjectName, Constants.UnitTestsProjectName));
                    
                }
                else
                {
                    //create tests file
                    testContent = FileUtils.RemoveNewLines(value.unitTests);

                    CommandLineUtils.ExecuteCommandFile(CommandType.CREATE_SOLUTION_WINDOWS_WITH_TESTS, new CreateSolutionWithTests(di.FullName, solutionName, Constants.CodeProjectName, Constants.UnitTestsProjectName));
                    DocumentManager.WriteFile(cleanedContent, Constants.BaseCodeFile, Path.Combine(di.FullName, solutionName, Constants.CodeProjectName));
                    DocumentManager.WriteFile(testContent, Constants.UnitTestFile, Path.Combine(di.FullName, solutionName, Constants.UnitTestsProjectName));
                    result = CommandLineUtils.ExecuteCommandFile(CommandType.EXECUTE_TESTS_WINDOWS, new RunTests(di.FullName, solutionName));
                    
                }
            } else if (RuntimeUtils.IsLinux)
            {
                if (!value.executeTests)
                {
                    // run code
                    result = CommandLineUtils.ExecuteCommandFile(CommandType.CREATE_SOLUTION_WINDOWS_WITH_TESTS, new CreateSolutionWithTests(di.FullName, solutionName, Constants.CodeProjectName, Constants.UnitTestsProjectName));

                }
                else
                {
                    //create tests file
                    testContent = FileUtils.RemoveNewLines(value.unitTests);

                    CommandLineUtils.ExecuteCommandFile(CommandType.CREATE_SOLUTION_LINUX_WITH_TESTS, new CreateSolutionWithTests(di.FullName, solutionName, Constants.CodeProjectName, Constants.UnitTestsProjectName));
                    DocumentManager.WriteFile(cleanedContent, Constants.BaseCodeFile, Path.Combine(di.FullName, solutionName, Constants.CodeProjectName));
                    DocumentManager.WriteFile(testContent, Constants.UnitTestFile, Path.Combine(di.FullName, solutionName, Constants.UnitTestsProjectName));
                    result = CommandLineUtils.ExecuteCommandFile(CommandType.EXECUTE_TESTS_LINUX, new RunTests(di.FullName, solutionName));
                }
            }



            return Ok(result);
        }

    }
}
