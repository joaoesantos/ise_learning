// client side configurations
import { runCodeModel } from '../models/runCodeModel.js';

export async function runCodeCtrl(codeLanguage, code, unitTests, executeTests) {
     return runCodeModel(codeLanguage, code, unitTests, executeTests);
}

export default { runCodeCtrl }