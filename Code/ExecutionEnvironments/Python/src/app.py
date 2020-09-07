from flask import Flask, request
from werkzeug.exceptions import HTTPException
import json

from modules.codeExecution.execution_handler import ExecutionHandler

app = Flask(__name__)

@app.route('/', methods = ['POST'])
def execute_code():
    executionHandler = ExecutionHandler(config["execution_parameters"])
    execution_result = executionHandler.run(request.get_json())
    return app.response_class(
        response = json.dumps({
            "rawResult": str(execution_result.rawResult),
            "wasError": execution_result.wasError,
            "executionTime": execution_result.executionTime,
        }),
        status = 200,
        mimetype = "application/json"
    )

@app.errorhandler(Exception)
def handle_exception(e):
    # handles response caused by exception of TypeError type
    if e.__class__.__name__ ==  "TypeError":
        return app.response_class(
            response = json.dumps({
                "type": "TypeError",
                "title": "TypeError",
                "detail": str(e),
                "instance": "/iselearning/executionEnvironments/python/typeError"
            }),
            status = 400,
            mimetype = "application/problem+json"
        )
    # handles response caused by exception of TimeoutExpired type
    if e.__class__.__name__ == "TimeoutExpired":
        return app.response_class(
            response = json.dumps({
                "type": "TimeoutExpire",
                "title": "TimeoutExpired",
                "detail": "Code execution was cancelled due to exceeding process time.",
                "instance": "/iselearning/executionEnvironments/python/timeout"

            }),
            status = 408,
            mimetype = "application/problem+json"
        )
    # handles all unexpected exceptions
    return app.response_class(
        response = json.dumps({
            "type": "Internal Server Error",
            "title": "Internal Server Error",
            "detail": "The server encountered an internal error and was unable to complete your request. Either the server is overloaded or there is an error in the application.",
            "instance": "/iselearning/executionEnvironments/python/unexpected"
        }),
        status = 500,
        mimetype = "application/problem+json"
    )

if __name__ == '__main__':
    with open("config.json") as json_config_file:
        config = json.load(json_config_file)
    app.run(host = config["server"]["host"], port = config["server"]["port"])