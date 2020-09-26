using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Transactions;

namespace NetCoreExecutionEnvironment.Exceptions
{
    public class RunEnvironmentException : Exception
    {
        public int StatusCode { get; private set; }
        public string ExceptionType { get; private set; }

        public string Detail { get; private set; }

        public string Instance { get; private set; }

        public string Title { get; private set; }

        public RunEnvironmentException(int statusCode, string exceptionType, string detail, string instance, string title) : base(detail)
        {
            StatusCode = statusCode;
            ExceptionType = exceptionType;
            Detail = detail;
            Instance = instance;
            Title = title;
        }
    }

}
