using NetCoreExecutionEnvironment.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;


namespace NetCoreExecutionEnvironment.Commands
{
    public class CommandOS
    {
        private static CommandOS _instance;
        public static CommandOS Instance
        {
            get
            {
                if(_instance == null)
                {
                    _instance = new CommandOS();
                }

                return _instance;
            }
        }



        public Constants.CommandType CommandCreateSolution { get; private set; }
        public Constants.CommandType CommandRunCode { get; private set; }
        public Constants.CommandType CommandRunTests { get; private set; }

        private CommandOS()
        {
            if (RuntimeUtils.IsWindows)
            {
                CommandCreateSolution = Constants.CommandType.CREATE_SOLUTION_WINDOWS;
                CommandRunCode = Constants.CommandType.EXECUTE_CODE_WINDOWS;
                CommandRunTests = Constants.CommandType.EXECUTE_TESTS_WINDOWS;
            } else if (RuntimeUtils.IsLinux && RuntimeUtils.IsMacOs)
            {
                CommandCreateSolution = Constants.CommandType.CREATE_SOLUTION_LINUX;
                CommandRunCode = Constants.CommandType.EXECUTE_CODE_LINUX;
                CommandRunTests = Constants.CommandType.EXECUTE_TESTS_LINUX;
            }
            else
            {
                throw new Exception("Operating System not supported");
            }
        }
    }
}
