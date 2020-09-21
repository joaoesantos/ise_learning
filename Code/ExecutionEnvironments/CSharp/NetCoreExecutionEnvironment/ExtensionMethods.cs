using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static NetCoreExecutionEnvironment.Constants;

namespace NetCoreExecutionEnvironment
{
    public static class ExtensionMethods
    {
        public static string GetFileName(this CommandType ct)
        {
            switch (ct)
            {
                case CommandType.CREATE_SOLUTION_WINDOWS:
                    return "create_solution_windows_with_tests.bat";
                case CommandType.CREATE_SOLUTION_LINUX:
                    return "create_solution_linux_with_tests.sh";
                case CommandType.EXECUTE_CODE_LINUX:
                    return "runcode_linux.sh";
                case CommandType.EXECUTE_CODE_WINDOWS:
                    return "runcode_windows.bat";
                case CommandType.EXECUTE_TESTS_LINUX:
                    return "execute_tests_linux.sh";
                case CommandType.EXECUTE_TESTS_WINDOWS:
                    return "execute_tests_windows.bat";
                default:
                    throw new NotSupportedException("Command type doesn't have a file defined");
            }
        }
    }
}
