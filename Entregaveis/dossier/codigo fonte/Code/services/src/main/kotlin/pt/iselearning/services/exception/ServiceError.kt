package pt.iselearning.services.exception

import com.fasterxml.jackson.annotation.JsonInclude

class ServiceError(
        val type: String,
        val title: String?,
        val detail: String?,
        val instance: String?,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        val _data: MutableList<Any>? = null
) {
}