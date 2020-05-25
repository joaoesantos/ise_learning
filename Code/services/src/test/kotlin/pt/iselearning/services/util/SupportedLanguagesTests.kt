package pt.iselearning.services.util

import org.junit.jupiter.api.Test
import org.springframework.util.Assert

class ApplicationTests {

    @Test
    fun testGetRegexForSupportedLanguagesSuccess() {
        val regex = SupportedLanguages.getRegexForSupportedLanguages().toRegex()
        Assert.isTrue(regex.matches("java"), "The java language should match")
        Assert.isTrue(regex.matches("kotlin"), "The kotlin language should match")
        Assert.isTrue(regex.matches("csharp"), "The csharp language should match")
        Assert.isTrue(regex.matches("javascript"), "The javascript language should match")
        Assert.isTrue(regex.matches("python"), "The python language should match")
    }

    @Test
    fun testGetRegexForSupportedLanguagesFailure() {
        val regex = SupportedLanguages.getRegexForSupportedLanguages().toRegex()
        Assert.isTrue(!regex.matches("javaaaa"), "The javaaaa is not a supported language")
        Assert.isTrue(!regex.matches("fffkotlin"), "The fffkotlin is not a supported language")
        Assert.isTrue(!regex.matches("c#"), "The c# is not a supported language")
        Assert.isTrue(!regex.matches("javasertertcript"), "The javasertertcript is not a supported language")
        Assert.isTrue(!regex.matches("Python"), "The Python is not a supported language")
    }

    @Test
    fun testRegexConstantAgainstDynamicRegex() {
        val regexString = SupportedLanguages.getRegexForSupportedLanguages()
        Assert.isTrue(regexString == SupportedLanguages.REGEX_STRING_SUPPORTED_LANGUAGES,
                "The hardcoded regex should match with dynamic, please correct harcoded value")
    }
}