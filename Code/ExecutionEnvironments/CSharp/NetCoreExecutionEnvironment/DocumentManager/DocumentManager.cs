using Microsoft.AspNetCore.Http;
using NetCoreExecutionEnvironment.DocumentManager;
using NetCoreExecutionEnvironment.Exceptions;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment.DocumentManager
{
    public class DocumentManager
    {

        /// <summary>
        /// Method for copying directory contents to other
        /// </summary>
        /// <param name="src">Full path of the source directory</param>
        /// <param name="dist">Full path of the destination directory. If this directory doesnt exist one is created at this path</param>
        /// <returns></returns>
        public static async Task DirectoryCopy(string src, string dist)
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

        /// <summary>
        /// Replace fil content
        /// </summary>
        /// <param name="filePath">Full path to file</param>
        /// <param name="text">New file content</param>
        /// <returns></returns>
        public static async Task ReplaceFileContents(string filePath, string text)
        {
            using (StreamWriter sw = File.CreateText(filePath))
            {
                await sw.WriteAsync(text);
            }
        }
    }
}
