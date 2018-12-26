package com.perso.jeremy.discordwebhook

import android.content.Context
import okhttp3.*
import okhttp3.RequestBody
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MessageViewModel {

    companion object {
        val client = OkHttpClient()
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }

    fun sendToDiscord(context: Context, message: String) {
        val body = RequestBody.create(JSON, message)
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

    fun sendFromSubreddit(context: Context, subreddit: String) {
        getPostFromSubreddit(context, subreddit)
    }

    private fun getPostFromSubreddit(context: Context, subreddit: String) {
        val url = "https://" + context.getString(R.string.reddit_base_url) + subreddit + "/hot.json"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("WEBHOOK", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("WEBHOOK", "Unexpected code $response")
                } else {
                    val jsonToSend = treatRedditResponse(response)
                    sendToDiscord(context, jsonToSend.toString())
                }
            }
        })

    }

    private fun treatRedditResponse(response: Response): JSONObject {
        val jsonData = response.body()?.string()
        val childrenArray = JSONObject(jsonData).getJSONObject("data").getJSONArray("children")
        val randomChild = getRandomElementFromJsonArray(childrenArray)
        return formatBody(randomChild)

    }

    private fun getRandomElementFromJsonArray(jsonArray: JSONArray): JSONObject {
        val length = jsonArray.length()
        val randomInteger = (0 until length).shuffled().first()
        return jsonArray.getJSONObject(randomInteger).get("data") as JSONObject
    }

    private fun formatBody(jsonObject: JSONObject): JSONObject {
        val body= JSONObject()
        val singleEmbed= JSONObject()
        singleEmbed.put("title", jsonObject.get("title"))
        val imageJson = JSONObject()
        imageJson.put("width", 1080)
        imageJson.put("height", 1920)
        imageJson.put("url", jsonObject.get("url"))
        singleEmbed.put("image", imageJson)
        body.put("embeds", JSONArray().put(singleEmbed))
        return body
    }
}
