let ExecutionResult = require('../models/ExecutionResult')
const { v4 } = require('uuid');
const strictString = "'use strict';";
const fs = require('fs');

let executableHandler = () => {
    async function run(req, res) {
        let runnable = req.body
        let guid = v4();
        let today = new Date()
        let filename =  `${guid}_${today.getTime()}.txt`;

        let codeResult = new Function( strictString + runnable.code)();

        fs.writeFile(filename, codeResult,(err) => {
            if(err) throw err

            console.log("file written")
        })
        res.json(new ExecutionResult("ok", false).toJson())
    }

    return {
        runCode : run
    }
}


module.exports = executableHandler