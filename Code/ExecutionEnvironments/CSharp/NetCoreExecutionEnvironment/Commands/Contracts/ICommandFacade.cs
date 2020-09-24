using NetCoreExecutionEnvironment.Contracts;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Contracts
{
    /// <summary>
    /// Interface that represents all commands present in this application.
    /// When a new Operating System is added a new implementation of this interface should be added to the solution
    /// </summary>
    public interface ICommandFacade : IDisposable
    {

        /// <summary>
        /// Creates directory at given full path
        /// </summary>
        /// <param name="directory">Full path where user wants to create directory</param>
        /// <returns>Full path to created directory</returns>
        public Task<string> CreateDirectory(string directory);

        /// <summary>
        /// Copy files and sub-directories of the template solution to a given directory
        /// </summary>
        /// <param name="directory">Folder name where the new solution files will be copied into</param>
        /// <param name="toBeDelete">Boolean that indicates if the directory, sub-directories and files are to be deleted when instance is disposed</param>
        /// <returns>Full path of new solution's full path</returns>
        public Task<string> CopyBaseSolution(string directory, bool toBeDelete);

        /// <summary>
        /// Command to run a solution's tests
        /// </summary>
        /// <param name="solutionDirectory">Full path where a solution is present</param>
        /// /// <param name="timeout">timeout defined for command</param>
        /// <returns>Command results</returns>
        public Task<ExecutableResult> RunTests(string solutionDirectory, int timeout);

        /// <summary>
        /// Command to run a solution main project
        /// </summary>
        /// <param name="solutionDirectory">Full path where a solution is present</param>
        /// <param name="timeout">timeout defined for command</param>
        /// <returns>Command results</returns>
        public Task<ExecutableResult> RunCode(string solutionDirectory, int timeout);

        public Task ReplaceProgramCode(string code, string filePath);

        public Task ReplaceUnitTestsCode(string unitTests, string filePath);


    }
}
