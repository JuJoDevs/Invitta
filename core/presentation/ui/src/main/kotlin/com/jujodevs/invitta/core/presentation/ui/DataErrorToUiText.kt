package com.jujodevs.invitta.core.presentation.ui

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.presentation.stringresources.R

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.RemoteDatabase.DATABASE ->
            UiText.StringResource(
                R.string.database_data_error,
            )
        DataError.RemoteDatabase.UNAUTHORIZED ->
            UiText.StringResource(
                R.string.unauthorized_data_error,
            )
        DataError.RemoteDatabase.NO_INTERNET ->
            UiText.StringResource(
                R.string.no_internet_data_error,
            )
        DataError.RemoteDatabase.NO_SIGNED_IN_USER ->
            UiText.StringResource(
                R.string.no_signed_in_user_data_error,
            )
        DataError.RemoteDatabase.API_NOT_AVAILABLE ->
            UiText.StringResource(
                R.string.api_not_available_data_error,
            )
        DataError.RemoteDatabase.TOO_MANY_REQUESTS ->
            UiText.StringResource(
                R.string.too_many_requests_data_error,
            )
        DataError.RemoteDatabase.EVENT_NOT_FOUND ->
            UiText.StringResource(
                R.string.event_not_found_data_error,
            )
        DataError.RemoteDatabase.EMPTY_UID ->
            UiText.StringResource(
                R.string.empty_uid_data_error,
            )
        DataError.RemoteDatabase.UNKNOWN ->
            UiText.StringResource(
                R.string.unknown_data_error,
            )
    }
}
