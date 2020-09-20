using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class DocumentManager
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="fileContent"></param>
        /// <param name="filename"></param>
        /// <param name="directoryPath"></param>
        /// <returns></returns>
        public static string WriteFile(string fileContent, string filename, string directoryPath )
        {
            if (!Directory.Exists(directoryPath.ToString()))
            {
                Directory.CreateDirectory(directoryPath);
            }

            string filePath = Path.Combine(directoryPath, filename);
            using (FileStream fs = File.Create(filePath))
            {
                fs.Write(Encoding.UTF8.GetBytes(fileContent));
            }

            return filePath;
        }
    }
}
