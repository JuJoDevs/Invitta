package com.jujodevs.invitta.core.presentation.ui

import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.presentation.stringresources.R

fun LoginError.asUiText(): UiText {
    return when (this) {
        LoginError.AuthService.ANONYMOUS_LOGIN_FAILED ->
            UiText.StringResource(
                R.string.anonymous_login_failed_login_error,
            )
        LoginError.AuthService.GOOGLE_LOGIN_FAILED ->
            UiText.StringResource(
                R.string.google_login_failed_login_error,
            )
        LoginError.AuthService.NO_USER_LOGGED ->
            UiText.StringResource(
                R.string.no_user_logged_login_error,
            )
        LoginError.AuthService.LINKING_FAILED ->
            UiText.StringResource(
                R.string.linking_failed_login_error,
            )
        LoginError.AuthService.AUTH_USER_COLLISION ->
            UiText.StringResource(
                R.string.auth_user_collision_login_error,
            )
        LoginError.GoogleAuth.GOOGLE_SIGN_IN_FAILED ->
            UiText.StringResource(
                R.string.google_sign_in_failed_login_error,
            )
        LoginError.GoogleAuth.WEB_CLIENT_ID_NOT_FOUND ->
            UiText.StringResource(
                R.string.web_client_id_not_found_login_error,
            )
        LoginError.GoogleAuth.UNEXPECTED_CREDENTIAL_TYPE ->
            UiText.StringResource(
                R.string.unexpected_credential_type_login_error,
            )
        LoginError.GoogleAuth.NO_ACTIVITY ->
            UiText.StringResource(
                R.string.no_activity_login_error,
            )
    }
}
