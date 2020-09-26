using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Commands.Facade
{
    /// <summary>
    /// Represents directories created when executing commands
    /// </summary>
    public class ExecEnvironmentDirectory
    {
        /// <summary>
        /// Represents directory's full path
        /// </summary>
        public string Directory { get; set; }

        /// <summary>
        /// Represents if directory is supposed to be deleted
        /// </summary>
        public bool DeleteAfterUse { get; set; }

        public ExecEnvironmentDirectory(string directory, bool toBeDelete)
        {
            Directory = directory;
            DeleteAfterUse = toBeDelete;
        }


    }
}
