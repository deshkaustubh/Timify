package com.streamliners.timify.android.helper

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.streamliners.timify.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType

data class GoogleOAuthTokens(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String
)

class GoogleOAuthTokensFetcher(
    private val client: HttpClient
) {

    suspend fun getOAuthTokens(authCode: String): GoogleOAuthTokens {
        val response: HttpResponse = client.post("https://www.googleapis.com/oauth2/v4/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                FormDataContent(
                    Parameters.build {
                        append("grant_type", "authorization_code")
                        append("client_id", BuildConfig.serverId)
                        append("client_secret", BuildConfig.serverSecret)
                        append("redirect_uri", "")
                        append("code", authCode)
                    }
                )
            )
        }

        val responseBody = response.bodyAsText()

        return Gson().fromJson(responseBody, GoogleOAuthTokens::class.java)
    }

}