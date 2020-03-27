const { body, oneOf } = require('express-validator')

class Executable {
    #code = "";
    #unitTests = "";
    #executeTests =  false;

    constructor(code, unitTests, executeTests){
        this.#code = code;
        this.#unitTests = unitTests;
        this.#executeTests = executeTests
    }
    
    get code() {
        return this.#code;
    }

    get unitTests() {
        this.#unitTests;
    }

    get executeTests() {
        this.#executeTests
    }

    static validate = () => {
        console.log("validating")
        return [
            body('code', 'code field is missing')
                .exists()
                .not()
                .isEmpty()
                .isString(),
            body("executeTests", "value must be provided")
                .exists(),
            body("executeTests", "value can't be empty")
                .not()
                .isEmpty(),
            body("executeTests", "value must be a boolean")
                .isBoolean(),
            oneOf(
                [
                    body("executeTests").isIn("false"),
                    body("unitTests")
                        .exists()
                        .not()
                        .isEmpty()
                        .isString()                
                ],
                "Unit tests must be provided"
                )
        ]
    }
}

module.exports = Executable