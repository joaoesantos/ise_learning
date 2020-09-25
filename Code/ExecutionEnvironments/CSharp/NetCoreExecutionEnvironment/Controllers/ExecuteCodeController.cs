using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using NetCoreExecutionEnvironment.Commands.Contracts;
using NetCoreExecutionEnvironment.Contracts;
using NetCoreExecutionEnvironment.Exceptions;

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
             
            string solutionFolder = $"{Constants.SolutionBaseFolder}_{Guid.NewGuid()}";
            string cleanedContent = FileUtils.RemoveNewLines(value.code);
            string testContents = FileUtils.RemoveNewLines(value.unitTests);

            using (CommandFacade)
            {
                string solutionPath = await CommandFacade.CopyBaseSolution(Path.Combine(di.FullName, solutionFolder), true);
                string codeFilePath = Path.Combine(solutionPath, Constants.CodeProjectName, Constants.BaseCodeFile);
                if (!value.executeTests)
                {
                    await DocumentManager.DocumentManager.ReplaceFileContents(codeFilePath, cleanedContent);
                    result = await CommandFacade.RunCode(Path.Combine(solutionPath, Constants.CodeProjectName), timeout);
                } else
                {
                    string testFilePath = Path.Combine(solutionPath, Constants.UnitTestsProjectName, Constants.UnitTestFile);
                    await DocumentManager.DocumentManager.ReplaceFileContents(testFilePath, testContents);
                    result = await CommandFacade.RunTests(Path.Combine(solutionPath, Constants.UnitTestsProjectName), timeout);
                }
                
            }
            return Ok(result);
        }

    }
}
