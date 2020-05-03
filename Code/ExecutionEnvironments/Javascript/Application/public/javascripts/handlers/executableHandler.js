const ExecutionResult = require('../models/ExecutionResult')
const util = require('util');
const exec = util.promisify(require('child_process').exec)
const path = require('path')
const { v4 } = require('uuid');
const fs = require('fs').promises;


let executableHandler = () => {
    async function run(req, res) {
        const strictString = "'use strict';";
        const runnable = req.body
        const guid = v4();
        const today = new Date()
        const fileIdentifier = `${guid}_${today.getTime()}`
        const filename =  `${fileIdentifier}.js`;
        const filePath = path.join('public','javascripts','templates',filename)
        let rawResult = "";
        let wasError = false;
 
        try {
            await fs.writeFile(filePath, strictString + runnable.code)
        
        if(runnable.executeTests){
            const templateTestContent = await fs.readFile(
                path.join('public','javascripts','templates','testTemplate.js'),
                {encoding : 'utf8'})
            
            const fileFunctions = require(`../templates/${fileIdentifier}`)
            const testFilename = `${fileIdentifier}_test.js`
            const testFilePath = path.join('public','javascripts','templates',testFilename)

            const functionNames = Object.keys(fileFunctions).reduce((fName, key) => {
                if(fName == undefined || fName == ""){
                    return key
                }
                return `${fName},${key}`
            }, "")

            const fileContent = templateTestContent
                                .replace('#path',`\'./${fileIdentifier}\'`)
                                .replace('#function_names',functionNames)
                                .replace('#testCases', runnable.unitTests);
            await fs.writeFile(testFilePath, strictString + fileContent) 
            const obj = await execChildProcess(`npx mocha ${testFilePath}`)
            rawResult = obj.rawResult
            wasError = obj.error
        }else{
            const obj = await execChildProcess(`node ${filePath}`)
            rawResult = obj.rawResult
            wasError = obj.error
        }
        res.json(new ExecutionResult(rawResult, wasError).toJson())
        } catch (error) {
            console.log(error)
            res.json(new ExecutionResult("Error running:" + error, true).toJson())
        }
        
    }

    async function execChildProcess(command){
        const {stdout, stderr, error} = await exec(command)
        let res = ""
        let err = false;
            if(error){
                res = error;
                err = true;
            }
            else if(stderr || stderr !== ""){
                res = stderr;
                err = true;
            }
            else{
                res = stdout;
            }
        return {
            rawResult: res,
            wasError: err
        }
    }

    return {
        runCode : run
    }
}


module.exports = executableHandler