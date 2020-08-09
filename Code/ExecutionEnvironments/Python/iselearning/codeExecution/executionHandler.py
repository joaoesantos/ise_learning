import asyncio
import datetime
import uuid

from iselearning.models.Executable import Executable
from iselearning.models.ExecutionResult import ExecutionResult

async def run(req,rsp):
    print("")

def executeCmd(cmd):
    try:
        return exec(cmd)
    except SyntaxError as err:
        return err
    except Exception as err:
        return err


# make a UUID based on the host ID and current time
uuid.uuid1()