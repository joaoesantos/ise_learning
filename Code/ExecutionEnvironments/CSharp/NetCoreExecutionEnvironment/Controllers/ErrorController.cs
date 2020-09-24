using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using NetCoreExecutionEnvironment.Exceptions;

namespace NetCoreExecutionEnvironment.Controllers
{

    [ApiController]
    public class ErrorController : ControllerBase
    {
        [Route("/error")]
        public IActionResult ErrorHandler()
        {
            var context = HttpContext.Features.Get<IExceptionHandlerFeature>();
            Exception exception = context?.Error;
            ObjectResult problem;

            if (exception is RunEnvironmentException)
            {
                RunEnvironmentException appException = exception as RunEnvironmentException;
                problem = Problem(
                    detail: appException.Detail,
                    title: appException.Title,
                    type: appException.ExceptionType,
                    instance: appException.Instance,
                    statusCode: appException.StatusCode
                );

            }
            else
            {
                problem = Problem(
                    detail: exception.Message,
                    title: "Internal Server Error",
                    type: "Internal Server Error",
                    instance: "/execute/csharp/error/unexpected",
                    statusCode: 500
                );

            }

            return problem;
        }
    }
}
