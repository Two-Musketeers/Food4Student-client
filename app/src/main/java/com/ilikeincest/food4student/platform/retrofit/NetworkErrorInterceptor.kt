package com.ilikeincest.food4student.platform.retrofit

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import java.net.SocketTimeoutException

class NetworkErrorInterceptor(private val context: Context) : Interceptor {
    private val handler = Handler(Looper.getMainLooper())
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: SocketTimeoutException) {
            Log.e("Network", e.message ?: "")
            val error = "Connection timed out. Please try again."
            val code = 408  // HTTP 408 Request Timeout
            showError(error)
            buildErrorResponse(chain, error, code)
        } catch (e: IOException) {
            val error = "Network error. Please check your connection."
            val code = 503  // HTTP 503 Service Unavailable
            showError(error)
            buildErrorResponse(chain, error, code)
        } catch (e: Exception) {
            val error = "An unexpected error occurred."
            val code = 500 // HTTP 500 Internal Server Error
            showError(error)
            buildErrorResponse(chain, error, code)
        }
    }

    private fun buildErrorResponse(
        chain: Interceptor.Chain,
        message: String,
        code: Int
    ): Response {
        return Response.Builder()
            .request(chain.request())
            .protocol(chain.connection()?.protocol() ?: Protocol.HTTP_1_1)
            .code(code)
            .message(message)
            .body("".toResponseBody(null))
            .build()
    }

    private fun showError(message: String) {
        handler.post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}