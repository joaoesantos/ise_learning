const express = require('express');
const {validationResult } = require('express-validator');
var router = express.Router();
var Executable = require('../models/Executable')

/* GET home page. */
router.post('/', Executable.validate(), function(req, res, next) {
  try{
    validationResult(req).throw();
    res.status(200).json({
      message: "all ok"
    })
  }catch(err){
    console.log("error caught")
    console.log(err)
    res.status(422).json({
      message: err
    })
  }
    

});

module.exports = router;
