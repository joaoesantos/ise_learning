const ExecutionResult = require('../models/ExecutionResult')
const {exec} = require('child_process')
const util = require('util');
const path = require('path')
const { v4 } = require('uuid');
const strictString = "'use strict';";
const fs = require('fs').promises;
const bufferReplace = require('buffer-replace');

let executableHandler = () => {
    async function run(req, res) {
        let runnable = req.body
        let guid = v4();
        let today = new Date()
        let fileIdentifier = `${guid}_${today.getTime()}`
        let filename =  `${fileIdentifier}.js`;
 
        const filePath = path.join('public','javascripts','templates',filename)

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

            exec(`npx mocha ${testFilePath}`, (error,stdout, stderr) => {
                if (error) {
                    console.error(`exec error: ${error}`);
                    return;
                  }
                  console.log(`stdout test: ${stdout}`);
                  console.error(`stderr test: ${stderr}`);
            }) 
        }

        // fs.writeFile(filePath, strictString + runnable.code,(err) => {
        //     if(err) throw err
            
        //     console.log("code file written")
        //     const fileFunctions = require(`../templates/${fileIdentifier}`)
        
        //     Object.keys(fileFunctions).forEach(function(key,index) {
        //         console.log(`key ${index}:${key}`)
        //     });

        //     //read from test template
        //     if(runnable.executeTests){
        //         fs.readFile(path.join('public','javascripts','templates','testTemplate.js'), (err, data) =>{
        //             const testFilename = `${fileIdentifier}_test.js`
        //             const testFilePath = path.join('public','javascripts','templates',testFilename)
        //             const fileFunctions = require(`../templates/${fileIdentifier}`)
                    
        //             const functionNames = Object.keys(fileFunctions).reduce((fName, key) => {
        //                 if(fName == undefined || fName == ""){
        //                     return key
        //                 }
        //                 return `${fName},${key}`
        //             }, "")
                   
        //             let fileContent = bufferReplace(data,'#function_names',functionNames)
        //             fileContent = bufferReplace(fileContent, '#path',`\'./${fileIdentifier}\'`)
        //             fileContent = bufferReplace(fileContent,'#testCases', runnable.unitTests)
        //             fs.writeFile(testFilePath, strictString + fileContent,(err) => {
        //                 if(err) throw err
                        
        //                 console.log(" test file written")
        //                 exec(`npx mocha ${testFilePath}`, (error,stdout, stderr) => {
        //                     if (error) {
        //                         console.error(`exec error: ${error}`);
        //                         return;
        //                       }
        //                       console.log(`stdout test: ${stdout}`);
        //                       console.error(`stderr test: ${stderr}`);
        //                 }) 
                                                
        //             })
        //         })
        //     }
            
            

            
        // })

        exec(`node ${filePath}`, (error,stdout, stderr) => {
            if (error) {
                console.error(`exec error: ${error}`);
                return;
              }
              console.log(`stdout: ${stdout}`);
              console.error(`stderr: ${stderr}`);
        }) 
        res.json(new ExecutionResult("ok", false).toJson())
    }

    return {
        runCode : run
    }
}


module.exports = executableHandler