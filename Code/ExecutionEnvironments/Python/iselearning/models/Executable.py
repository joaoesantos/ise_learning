# Model received by the controller
class Executable:
    def __init__(self, code, unitTests, executeTests):
        self.code = code
        self.unitTests = unitTests
        self.executeTests = executeTests

    @property
    def code(self):
        return self.__code

    @code.setter
    def code(self, code):
        if code is None:
            raise TypeError("Value code must be provided.")
        self.__code = code

    @property
    def unitTests(self):
        return self.__unitTests

    @unitTests.setter
    def unitTests(self, unitTests):
        self.__unitTests = unitTests

    @property
    def executeTests(self):
        return self.__executeTests

    @executeTests.setter
    def executeTests(self, executeTests):
        if not isinstance(executeTests, bool):
            raise TypeError("Value executeTests must be a bool.")
        self.__executeTests = executeTests
