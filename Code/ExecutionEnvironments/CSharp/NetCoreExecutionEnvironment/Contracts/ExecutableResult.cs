using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Contracts
{
    public class ExecutableResult
    {
        public readonly string rawResult;
        public readonly bool wasError;
        public readonly long executionTime;

        public ExecutableResult(string rawResult, bool wasError, long executionTime)
        {
            this.rawResult = rawResult;
            this.wasError = wasError;
            this.executionTime = executionTime;
        }
    }
}
