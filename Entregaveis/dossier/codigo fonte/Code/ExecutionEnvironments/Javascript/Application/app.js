var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var router = require('./public/javascripts/routes/router');
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.post('/', router);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  res.setHeader('content-type', 'application/problem+json');
  res.json(new ProblemJson(
    "Resource Not Found",
    "Resource Not Found",
    "Requested resource was not found.",
    "/execute/javascript/error/path").toJson())
});

// error handler
app.use(function(err, req, res, next) {
  console.log(`Error handler error: ${err.message}`, err.stack)

  res.setHeader('content-type', 'application/problem+json');
  res.status(err.status || 500);
  res.status(500).json(new ProblemJson(
      "Internal Server Error",
      "Internal Server Error",
      err.message,
      "/execute/javascript/error").toJson())
});

let port = 3500
app.listen(port,()=> {console.log(`Listening.... on port ${port}`)})

module.exports = app;
