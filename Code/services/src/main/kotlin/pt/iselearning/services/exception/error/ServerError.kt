package pt.iselearning.services.exception.error

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Class to support problem/+json data structure for RFC7807.
 */
class ServerError(
        val type: String,
        val title: String,
        val detail: String,
        val instance: String?,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val _data: MutableList<Any>? = null
)