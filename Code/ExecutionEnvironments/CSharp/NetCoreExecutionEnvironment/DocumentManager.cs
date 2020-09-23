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
    public class DocumentManager : IDisposable
    {
        private string _mainFolderPath;
        private bool _deleteMainFolder;

        public DocumentManager(string mainFolderPath, bool deleteMainFolder)
        {
            _mainFolderPath = mainFolderPath;
            _deleteMainFolder = deleteMainFolder;
        }

        public string WriteFile(string fileContent, string filename, string directoryPath)
        {
            try
            {
                if (!Directory.Exists(directoryPath.ToString()))
                {
                    Directory.CreateDirectory(directoryPath);
                }

                string filePath = Path.Combine(directoryPath, filename);
                using (FileStream fs = File.OpenWrite(filePath))
                {
                    using (StreamWriter sw = new StreamWriter(fs))
                    {
                        fs.SetLength(0);
                        sw.Write(fileContent);
                    }
                }

                return filePath;
            } catch (Exception e)
            {
                throw new RunEnvironmentException(StatusCodes.Status500InternalServerError, Constants.ExceptionType.INTERNAL_SERVER_ERROR, e.Message, Constants.ExceptionInstance.FILE_SYSTEM, "Error while using file system");
            }

        }

        public void Dispose()
        {
            new Thread(() => {
                try
                {
                    if (_deleteMainFolder)
                    {
                        Directory.Delete(_mainFolderPath, true);
                    }
                    else
                    {
                        DirectoryInfo di = new DirectoryInfo(_mainFolderPath);
                        foreach (FileInfo file in di.EnumerateFiles())
                        {
                            file.Delete();
                        }
                        foreach (DirectoryInfo dir in di.EnumerateDirectories())
                        {
                            dir.Delete(true);
                        }
                    }
                }
                catch (Exception e)
                {
                    throw new RunEnvironmentException(StatusCodes.Status500InternalServerError, Constants.ExceptionType.INTERNAL_SERVER_ERROR, e.Message, Constants.ExceptionInstance.FILE_SYSTEM, "Error while deleting folders");
                }
            }).Start();

        }
    }
}
