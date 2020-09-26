using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Contracts
{
    public class ExecutableResult
    {
        public string RawResult { get; set; }
        public bool WasError { get; set; }
        public long ExecutionTime { get; set; }

        public ExecutableResult(string rawResult, bool wasError, long executionTime)
        {
            RawResult = rawResult;
            WasError = wasError;
            ExecutionTime = executionTime;
        }
    }
}
