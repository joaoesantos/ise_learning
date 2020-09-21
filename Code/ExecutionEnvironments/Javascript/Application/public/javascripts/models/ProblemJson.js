class ProblemJson {

    #type = "";
    #title = "";
    #detail = "";
    #instance = "";

    constructor(type, title, detail, instance) {
        this.#type = type;
        this.#title = title
        this.#detail = detail
        this.#instance = instance
    }

    get type() {
        return this.#type;
    }

    get title() {
        return this.#title;
    }

    get detail() {
        return this.#detail;
    }

    get instance() {
        return this.#instance;
    }

    toJson = () => {
        return {
            type: this.#type,
            title: this.#title,
            detail: this.#detail,
            instance: this.#instance
        }
    }
}

module.exports = ProblemJson
