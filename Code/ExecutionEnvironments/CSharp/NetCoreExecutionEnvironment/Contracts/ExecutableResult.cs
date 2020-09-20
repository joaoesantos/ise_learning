using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Contracts
{
    public class ExecutableResult
    {
        public string RawResult { get; private set; }
        public bool WasError { get; private set; }
        public long ExecutionTime { get; private set; }

        public ExecutableResult(string rawResult, bool wasError, long executionTime)
        {
            RawResult = rawResult;
            WasError = wasError;
            ExecutionTime = executionTime;
        }
    }
}
