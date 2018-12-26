package com.perso.jeremy.discordwebhook

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    companion object {
        const val SHAREDPREFS_NAME = "SHAREDPREFS"
        const val WEBHOOK_KEY = "WEBHOOK"
    }

    private lateinit var webhookEditText: EditText
    private lateinit var validateWebhookButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews(){
        webhookEditText = findViewById(R.id.webhook_entry)
        webhookEditText.setText(getWebhookFromSharedPrefs())
        validateWebhookButton = findViewById(R.id.validate_webhook)
        validateWebhookButton.setOnClickListener {
            val webhook = webhookEditText.text.toString()
            if (!TextUtils.isEmpty(webhook)) {
                saveWebhookToSharedPrefs(webhook)
                goToNextActivity()
            }
        }
    }

    private fun goToNextActivity() {
        val intent = Intent(this, MessageActivity::class.java)
        startActivity(intent)
    }

    private fun saveWebhookToSharedPrefs(webhook: String) {
        val sharedPrefs = getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(WEBHOOK_KEY, webhook).apply()
    }

    private fun getWebhookFromSharedPrefs():String {
        val sharedPrefs = getSharedPreferences(SHAREDPREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getString(WEBHOOK_KEY, "")
    }
}
