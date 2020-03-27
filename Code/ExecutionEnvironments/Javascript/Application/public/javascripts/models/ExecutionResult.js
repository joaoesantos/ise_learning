class ExecutionResult {

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
}

export default ExecutionResult