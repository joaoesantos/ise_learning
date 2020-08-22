# Model responded by the controller
class ExecutionResult:
    def __init__(self, rawResult, wasError, executionTime):
        self.rawResult = rawResult
        self.wasError = wasError
        self.executionTime = executionTime

    @property
    def rawResult(self):
        return self.__rawResult

    @rawResult.setter
    def rawResult(self, rawResult):
        if rawResult is None:
            raise TypeError("Value code must be provided.")
        self.__rawResult = rawResult

    @property
    def wasError(self):
        return self.__wasError

    @wasError.setter
    def wasError(self, wasError):
        if not isinstance(wasError, bool):
            raise TypeError("Value wasError must be a bool.")
        self.__wasError = wasError

    @property
    def executionTime(self):
        return self.__executionTime

    @executionTime.setter
    def executionTime(self, executionTime):
        if not isinstance(executionTime, float):
            raise TypeError("Value executionTime must be a float.")
        self.__executionTime = executionTime
