package com.example.carmatch.view.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carmatch.fragments.ChatFragment
import com.example.carmatch1.R
import com.example.carmatch1.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        includeToolbarApp()
        showChatFragment()
    }
    
    private fun includeToolbarApp() {
        val toolbar = binding.includeMainToolbar.toolbarApp
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Conversas"
            setDisplayHomeAsUpEnabled(true)
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    private fun showChatFragment() {
        val frag = ChatFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_chat, frag)
            .commit()
    }
}
