const ExecutionResult = require('../models/ExecutionResult')
const ProblemJson = require('../models/ProblemJson')
const util = require('util');
const exec = util.promisify(require('child_process').exec)
const path = require('path')
const { v4 } = require('uuid');
const fs = require('fs').promises;
const fsOriginal = require('fs');

let executableHandler = () => {
    async function run(req, res) {
        const strictString = "'use strict';";
        const runnable = req.body
        const guid = v4();
        const today = new Date()
        const fileIdentifier = `${guid}_${today.getTime()}`
        const filename =  `${fileIdentifier}.js`;
        const tempDir = path.join('public','javascripts','tempFiles')
        const filePath = path.join(tempDir, filename)
        const testFilename = `${fileIdentifier}_test.js`
        const testFilePath = path.join(tempDir, testFilename)
        let rawResult = "";
        let wasError = false;
        let executionTime = 0;
 
        try {
            if(!fsOriginal.existsSync(tempDir)) {
                fsOriginal.mkdirSync(tempDir);
            }
            await fs.writeFile(filePath, strictString + runnable.code)
        
            if(runnable.executeTests) {
                const templateTestContent = await fs.readFile(
                    path.join('public','javascripts','templates','testTemplate.js'),
                    {encoding : 'utf8'})
                
                const fileFunctions = require(`../tempFiles/${fileIdentifier}`)

                const functionNames = Object.keys(fileFunctions).reduce((fName, key) => {
                    if(fName == undefined || fName == "") {
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
                executionTime = obj.executionTime
            } else {
                const obj = await execChildProcess(`node ${filePath}`)
                rawResult = obj.rawResult
                wasError = obj.error
                executionTime = obj.executionTime
            }
            res.json(new ExecutionResult(rawResult, wasError, executionTime).toJson())
        } catch (error) {
            console.log(`Executable Handler error: ${error.message}`, error.stack)
            res.setHeader('content-type', 'application/problem+json');
            if(error.wasTimeout) {
                res.json(new ProblemJson(
                    "TimeoutExpired",
                    "TimeoutExpired",
                    error.message,
                    "/execute/javascript/timeout").toJson())
            }
            res.json(new ProblemJson(
                "Internal Server Error",
                "Internal Server Error",
                error.message,
                "/execute/javascript/error").toJson())
        } finally {
            if(fsOriginal.existsSync(filePath)) {
                fs.unlink(filePath).catch(p => console.log(`Error deleting file ${filePath} : ${error}`))
            }
            if(fsOriginal.existsSync(testFilePath)) {
                fs.unlink(testFilePath).catch(p => console.log(`Error deleting file ${testFilePath} : ${error}`))
            }
        }
        
    }

    async function execChildProcess(command) {
        let timeout = 60*1000; //milliseconds
        try{
            let hrstart = process.hrtime()
            const {stdout, stderr, error} = await exec(command, {timeout: timeout})
            let hrend = process.hrtime(hrstart)
            let execTimeMili = Math.round(hrend[1] / 1000000)
            let res = stdout
            let err = false;
                if(error) {
                    console.log(`Error executing command, error field: ${error.message}`)
                    res = error;
                    err = true;
                }
            return {
                rawResult: res,
                error: err,
                executionTime: execTimeMili
            }
        } catch (error) {
            console.log(`Error executing command: ${error.message}`)
            if(error.killed && error.signal === 'SIGTERM') {
                let timeoutError = new Error(`Execution exceeded timeout of ${timeout / 1000} seconds.`);
                timeoutError.wasTimeout = true;
                throw timeoutError;
            } else {
                return {
                    rawResult: error.stdout ? error.stdout : error.message,
                    error: true,
                    executionTime: 0
                }
            }
        }
    }

    return {
        runCode : run
    }
}

module.exports = executableHandler