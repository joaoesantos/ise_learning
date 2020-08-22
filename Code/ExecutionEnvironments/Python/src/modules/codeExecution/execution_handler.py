import os, pathlib, glob
from time import time
import uuid, json
from subprocess import run, PIPE, CompletedProcess

from ..models.executable import Executable
from ..models.execution_result import ExecutionResult

class ExecutionHandler:
    def __init__(self, config):
        self.filedir = pathlib.Path(__file__).parent.absolute()
        self.timeout = config["timeout"]

    # runs a script in another thread and returns a CompletedProcess object and process execution time
    # if script is not completed during a pre-determinated time, an timeout exception is raise and the process is killed
    def executeScript(self, script: str, timeout: float) -> (CompletedProcess, float):
        try:
            start_time = time()
            completedProcess = run(['python', script],stdout=PIPE,stderr=PIPE,timeout=timeout)
            end_time = time()
            return completedProcess, end_time - start_time
        except Exception as e:
            raise    

    # delete temporary files
    def __deleteTempFiles(self,pattern: str):
        # iterate over the list of filepaths & remove each file.
        for filepath in glob.glob("{0}\\temp\\{1}*.py".format(self.filedir,pattern)):
            try:
                os.remove(filepath)
            except Exception as e:
                raise

    # method that receives an json request of code to be executed
    def run(self,request: json) -> ExecutionResult:
        # create random UUID
        uui4 = uuid.uuid4()
        try:
            executable = Executable(
                request["code"],
                request["unitTests"],
                request["executeTests"]
            )
            if executable.executeTests:
                unittest_filename = os.path.join(self.filedir, 'temp\\{0}_unittests.py'.format(uui4))
                with open(unittest_filename, 'w+') as f:
                    f.write(executable.unitTests)
                completedProcess, executionTime = self.executeScript(unittest_filename,self.timeout)
                return ExecutionResult(
                    completedProcess.stderr,
                    completedProcess.returncode != 0,
                    executionTime
                )  
            else:
                code_filename = os.path.join(self.filedir, 'temp\\{0}.py'.format(uui4))
                with open(code_filename, 'w+') as f:
                    f.write(executable.code)
                completedProcess, executionTime = self.executeScript(code_filename,self.timeout)
                return ExecutionResult(
                    completedProcess.stdout if completedProcess.returncode == 0 else completedProcess.stderr,
                    completedProcess.returncode != 0,
                    executionTime
                )  
        except Exception as e:
            raise
        finally:
            self.__deleteTempFiles(uui4)
