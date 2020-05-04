// client side configurations
import { runCodeModel } from '../models/runCodeModel.js';

export async function runCodeCtrl(codeLanguage, code) {
    // return await runCodeModel(codeLanguage, code);
    async function sleep() {
        await new Promise(resolve => setTimeout(resolve, 2000));
        return runCodeModel(codeLanguage, code);
    }
    return sleep();
}

export default { runCodeCtrl }