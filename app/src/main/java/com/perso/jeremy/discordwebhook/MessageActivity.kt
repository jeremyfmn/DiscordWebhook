package com.perso.jeremy.discordwebhook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class MessageActivity : AppCompatActivity() {

    private val mMessageViewModel= MessageViewModel()

    private lateinit var mCustomMessageEditText: EditText
    private lateinit var mCustomMessageSendButton: Button
    private lateinit var mCustomSubredditEditText: EditText
    private lateinit var mCustomSubredditSendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        initViews()
    }

    private fun initViews() {
        mCustomMessageEditText = findViewById(R.id.custom_message)
        mCustomMessageSendButton = findViewById(R.id.custom_message_send)
        mCustomSubredditEditText = findViewById(R.id.te_custom_subreddit)
        mCustomSubredditSendButton = findViewById(R.id.send_from_subreddit)

        mCustomMessageSendButton.setOnClickListener {
            val message = mCustomMessageEditText.text.toString()
            if(!TextUtils.isEmpty(message)) {
                mMessageViewModel.sendCustomMessage(this ,message)
            }
        }

        mCustomSubredditSendButton.setOnClickListener {
            val subreddit = mCustomSubredditEditText.text.toString()
            if(TextUtils.isEmpty(subreddit)){
                mMessageViewModel.sendFromSubreddit(subreddit)
            }

        }
    }
}
