package com.jujodevs.invitta.core.presentation.ui

import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.presentation.stringresources.R
import com.jujodevs.invitta.core.presentation.ui.UiText.StringResource

fun LoginError.asUiText(): UiText {
    return loginErrorUiTextMap[this] ?: StringResource(R.string.unknown_error)
}

private val loginErrorUiTextMap =
    mapOf(
        LoginError.AuthService.ANONYMOUS_LOGIN_FAILED to
            StringResource(R.string.anonymous_login_failed_login_error),
        LoginError.AuthService.GOOGLE_LOGIN_FAILED to
            StringResource(R.string.google_login_failed_login_error),
        LoginError.AuthService.NO_USER_LOGGED to
            StringResource(R.string.no_user_logged_login_error),
        LoginError.AuthService.LINKING_FAILED to
            StringResource(R.string.linking_failed_login_error),
        LoginError.AuthService.AUTH_USER_COLLISION to
            StringResource(R.string.auth_user_collision_login_error),
        LoginError.GoogleAuth.GOOGLE_SIGN_IN_FAILED to
            StringResource(R.string.google_sign_in_failed_login_error),
        LoginError.GoogleAuth.WEB_CLIENT_ID_NOT_FOUND to
            StringResource(R.string.web_client_id_not_found_login_error),
        LoginError.GoogleAuth.UNEXPECTED_CREDENTIAL_TYPE to
            StringResource(R.string.unexpected_credential_type_login_error),
        LoginError.GoogleAuth.EMAIL_CLAIM_NOT_FOUND to
            StringResource(R.string.no_activity_login_error),
        LoginError.GoogleAuth.NO_ACTIVITY to
            StringResource(R.string.no_activity_login_error),
        LoginError.AuthService.REST_EXCEPTION to
            StringResource(R.string.rest_exception_login_error),
        LoginError.AuthService.AUTH_REST_EXCEPTION to
            StringResource(R.string.auth_rest_exception_login_error),
        LoginError.AuthService.AUTH_HTTP_REQUEST_EXCEPTION to
            StringResource(R.string.auth_http_request_exception_login_error),
        LoginError.AuthService.AUTH_HTTP_REQUEST_TIMEOUT_EXCEPTION to
            StringResource(R.string.auth_http_request_timeout_exception_login_error),
        LoginError.AuthService.UNKNOWN_ERROR to
            StringResource(R.string.unknown_error),
    )
