using Microsoft.AspNetCore.Http;
using NetCoreExecutionEnvironment.Exceptions;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class DocumentManager
    {
        public async Task DirectoryCopy(string src, string dist)
        {
            DirectoryInfo dir = new DirectoryInfo(src);
            if (!dir.Exists)
            {
                throw new RunEnvironmentException(
                    StatusCodes.Status500InternalServerError,
                    Constants.ExceptionType.INTERNAL_SERVER_ERROR, $"Directory {src} not found", Constants.ExceptionInstance.CONTROLLER_FILE_SYSTEM, "Error while using file system");
            }

            DirectoryInfo nd = Directory.CreateDirectory(dist);

            foreach (string filename in Directory.EnumerateFiles(src))
            {

                using (FileStream st = File.Open(filename, FileMode.Open))
                {
                    string fp = Path.Combine(nd.FullName, filename.Substring(filename.LastIndexOf(Path.DirectorySeparatorChar) + 1));
                    using (FileStream ds = File.Create(fp))
                    {
                        await st.CopyToAsync(ds);
                    }
                }
            }

            foreach (string dirName in Directory.EnumerateDirectories(src))
            {
                string folderName = dirName.Substring(dirName.LastIndexOf(Path.DirectorySeparatorChar) + 1);
                await DirectoryCopy(dirName, Path.Combine(nd.FullName, folderName));
            }

        }
    }
}
