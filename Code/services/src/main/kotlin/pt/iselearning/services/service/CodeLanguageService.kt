package pt.iselearning.services.service

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import pt.iselearning.services.models.codeLanguage.CodeLanguageModel
import pt.iselearning.services.repository.CodeLanguageRepository

@Validated
@Service
class CodeLanguageService (
        private val codeLanguageRepository: CodeLanguageRepository,
        private val modelMapper: ModelMapper
) {

    @Transactional
    fun getAllCodeLanguages(): List<CodeLanguageModel> {
        return codeLanguageRepository.findAll().toList().map { cl -> modelMapper.map(cl, CodeLanguageModel::class.java) }
    }
}
