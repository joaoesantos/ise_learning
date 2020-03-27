class ExecutionResult {

    #rawResult = "";
    #wasError = false;

    constructor(rawResult, wasError) {
        this.#rawResult = rawResult;
        this.#wasError = wasError
    }

    get rawResult() {
        return this.#rawResult;
    }

    get wasError() {
        return this.#wasError;
    }

    toJson = () => {
        return {
            rawResult: this.#rawResult,
            wasError: this.#wasError
        }
    }
}

module.exports = ExecutionResult