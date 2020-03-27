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
    console.log("error caught")
    console.log(err)
    res.status(422).json({
      message: err
    })
  }
    

});

module.exports = router;
