package com.perso.jeremy.discordwebhook

import android.content.Context
import okhttp3.*
import okhttp3.RequestBody
import android.util.Log
import java.io.IOException


class MessageViewModel {

    companion object {
        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }

    fun sendCustomMessage(context: Context, message: String) {
        val body = RequestBody.create(JSON, "{\"content\": \"$message\"}")
        val webhook = context.getSharedPreferences(MainActivity.SHAREDPREFS_NAME, Context.MODE_PRIVATE).getString(MainActivity.WEBHOOK_KEY, "")
        val request = Request.Builder()
                .url(webhook)
                .post(body)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("WEBHOOK", e.message)
            }
            override fun onResponse(call: Call, response: Response) {
            }
        })

        }

    fun sendFromSubreddit(subreddit: String) {
    }

}
