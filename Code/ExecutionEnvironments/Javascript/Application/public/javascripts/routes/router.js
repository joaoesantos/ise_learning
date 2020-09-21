const express = require('express');
const {validationResult } = require('express-validator');
const router = express.Router();
const executableHandler = require('../handlers/executableHandler')()

let Executable = require('../models/Executable')

/* GET home page. */
router.post('/', Executable.validate(), function(req, res, next) {
  try{
    validationResult(req).throw();
    executableHandler.runCode(req, res)
    res.status(200)
  }catch(err){
    res.setHeader('content-type', 'application/problem+json');
    res.status(422).json(new ProblemJson(
      "Unprocessable Entity",
      "Unprocessable Entity",
      err.message,
      "/execute/javascript/modelValidation/error").toJson())
    console.log(`Validation error: ${err.message}`, err.stack)
  }
});

module.exports = router;
