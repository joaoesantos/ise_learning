using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class FileUtils
    {
        public static string RemoveNewLines(string content)
        {
            return string.IsNullOrWhiteSpace(content) ? string.Empty : content.Replace(Environment.NewLine, string.Empty);
        }
    }
}
