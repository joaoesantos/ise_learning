package pt.iselearning.services.domain

import java.io.Serializable

data class ChallengeTagId (
    var challengeTagId : Int?,
    var challengeId : Int?
) : Serializable {
    constructor(): this(null, null)
}