class ExecutionResult {

    #rawResult = "";
    #wasError = false;
    #executionTime = 0;

    constructor(rawResult, wasError, executionTime) {
        this.#rawResult = rawResult;
        this.#wasError = wasError;
        this.#executionTime = executionTime;
    }

    get rawResult() {
        return this.#rawResult;
    }

    get wasError() {
        return this.#wasError;
    }

    get executionTime() {
        return this.#executionTime;
    }

    toJson = () => {
        return {
            rawResult: this.#rawResult,
            wasError: this.#wasError,
            executionTime: this.#executionTime
        }
    }
}

module.exports = ExecutionResult
