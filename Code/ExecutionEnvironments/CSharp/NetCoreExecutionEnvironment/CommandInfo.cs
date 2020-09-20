using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class CommandInfo
    {
        public readonly long ProcessDuration;
        public readonly string StandardOutput;

        public readonly bool WasError;

        public CommandInfo(long processDuration, string standardOutput, bool wasError)
        {
            ProcessDuration = processDuration;
            StandardOutput = standardOutput;
            WasError = wasError;
        }
    }
}
