package pt.iselearning.services.util

import org.junit.jupiter.api.Test
import org.springframework.util.Assert

class CustomValidatorsTests {

    @Test
    fun privacyRegexTest() {
        val regex = CustomValidators.PRIVACY_REGEX_STRING.toRegex()
        Assert.isTrue(regex.matches("private"), "The private should match")
        Assert.isTrue(regex.matches("public"), "The public should match")
        Assert.isTrue(!regex.matches(""), "Empty string should not match")
    }

    @Test
    fun tagsRegexTest() {
        val regex = CustomValidators.TAGS_REGEX_STRING.toRegex()
        Assert.isTrue(regex.matches("a"), "A single value with no commas should match")
        Assert.isTrue(regex.matches("b,b,b,b,b"), "Comma separated values should match")
        Assert.isTrue(!regex.matches(",c,c,c,c"), "Starting with a comma should not match")
        Assert.isTrue(!regex.matches("d,d,d,d,"), "Ending with a comma should not match")
        Assert.isTrue(!regex.matches("e,e,e,,e,e,e"), "2 or mores commas with no value in between should not match")
    }
}