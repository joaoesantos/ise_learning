import subprocess
import os, time, datetime, json

from src.modules.codeExecution.execution_handler import ExecutionHandler

# # without unit tests
# request = {
#     "code": "print(\"Hello World!\")",
#     "unitTests": "",
#     "executeTests": False
# }

# infinite cycle
request = {
    "code": "while True:\r\n    print(\"Hello Worlds!\")",
    "unitTests": "",
    "executeTests": False
}

# # with unit tests
# request = {
#     "code": "def isNumberPalindrome(number: int) -> bool:\r\n    if number < 0: \r\n        return  False\r\n   \r\n    original = number\r\n    revertedNumber = 0\r\n\r\n    while number != 0:\r\n        remainder = number % 10\r\n        revertedNumber = revertedNumber * 10 + remainder\r\n        number //= 10\r\n\r\n    return original == revertedNumber",
#     "unitTests": "import unittest\r\n\r\ndef isNumberPalindrome(number: int) -> bool:\r\n    if number < 0: \r\n        return  False\r\n   \r\n    original = number\r\n    revertedNumber = 0\r\n\r\n    while number != 0:\r\n        remainder = number % 10\r\n        revertedNumber = revertedNumber * 10 + remainder\r\n        number //= 10\r\n\r\n    return original == revertedNumber\r\n\r\n\r\nclass TestChallenge(unittest.TestCase):\r\n    def test(self):\r\n        self.assertEqual(isNumberPalindrome(121), True)\r\n        self.assertEqual(isNumberPalindrome(10), False)\r\n        self.assertEqual(isNumberPalindrome(44), True)\r\n        self.assertEqual(isNumberPalindrome(107), False)\r\n\r\nif __name__ == \'__main__\':\r\n    unittest.main()",
#     "executeTests": True
# }

config = {
        "timeout":5
}

execution_handler = ExecutionHandler(config)

r = execution_handler.run(request)
print("RESULT RESULT RESULT ", r.rawResult, r.wasError, r.executionTime )