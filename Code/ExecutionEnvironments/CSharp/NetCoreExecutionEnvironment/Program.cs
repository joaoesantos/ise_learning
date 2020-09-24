using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;
using System;
using System.Runtime.InteropServices;

namespace NetCoreExecutionEnvironment
{
    public class Program
    {
        public static void Main(string[] args)
        {
            Console.WriteLine(RuntimeInformation.OSDescription);
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder
                    .UseStartup<Startup>();
                });
    }
}
