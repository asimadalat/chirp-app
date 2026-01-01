package com.asimorphic.core.presentation.mapper

import chirp.core.presentation.generated.resources.Res
import chirp.core.presentation.generated.resources.error_bad_request
import chirp.core.presentation.generated.resources.error_conflict
import chirp.core.presentation.generated.resources.error_disk_full
import chirp.core.presentation.generated.resources.error_forbidden
import chirp.core.presentation.generated.resources.error_internal_server
import chirp.core.presentation.generated.resources.error_no_internet
import chirp.core.presentation.generated.resources.error_not_found
import chirp.core.presentation.generated.resources.error_payload_too_large
import chirp.core.presentation.generated.resources.error_request_timed_out
import chirp.core.presentation.generated.resources.error_serialisation
import chirp.core.presentation.generated.resources.error_service_unavailable
import chirp.core.presentation.generated.resources.error_too_many_requests
import chirp.core.presentation.generated.resources.error_unauthorised
import chirp.core.presentation.generated.resources.error_unknown
import com.asimorphic.core.domain.util.DataError
import com.asimorphic.core.presentation.util.UiText

fun DataError.toUiText(): UiText {
    val resource = when (this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.NOT_FOUND -> Res.string.error_not_found
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.BAD_REQUEST -> Res.string.error_bad_request
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timed_out
        DataError.Remote.UNAUTHORIZED -> Res.string.error_unauthorised
        DataError.Remote.FORBIDDEN -> Res.string.error_forbidden
        DataError.Remote.NOT_FOUND -> Res.string.error_not_found
        DataError.Remote.CONFLICT -> Res.string.error_conflict
        DataError.Remote.INTERNAL_SERVER_ERROR -> Res.string.error_internal_server
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.PAYLOAD_TOO_LARGE -> Res.string.error_payload_too_large
        DataError.Remote.SERVICE_UNAVAILABLE -> Res.string.error_service_unavailable
        DataError.Remote.SERIALIZATION -> Res.string.error_serialisation
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }

    return UiText.Resource(id = resource)
}