from flask import Flask, request, make_response, json
from werkzeug.exceptions import HTTPException

app = Flask(__name__)

@app.route('/', methods = ['POST'])
def execute_code():
    return make_response(request.get_json(), 200)

@app.errorhandler(HTTPException)
def handle_exception(e):
    """Return JSON instead of HTML for HTTP errors."""
    # start with the correct headers and status code from the error
    response = e.get_response()
    # replace the body with JSON
    response.data = json.dumps({
        "code": e.code,
        "name": e.name,
        "description": e.description,
    })
    response.content_type = "application/json"
    return response


if __name__ == '__main__':
    app.run(host = 'localhost', port = 8085, debug = None)