using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.Contracts
{
    public class Executable
    {
        public string code { get; set; }
        public string unitTests { get; set; }
        public bool executeTests { get; set; }
    }
}
