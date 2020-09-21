﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NetCoreExecutionEnvironment
{
    public class DocumentManager
    {

        public static string WriteFile(string fileContent, string filename, string directoryPath)
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
        }
    }
}