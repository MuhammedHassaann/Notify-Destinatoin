package com.muhammedhassaan.notifydestiniation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.muhammedhassaan.notifydestiniation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DestinationListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = DestinationListAdapter()
        val destinationList = listOf(
            Destination(1,"Arab Juhina",Constants.DONE,Constants.DONE_ICON),
            Destination(2,"Ain Shams",Constants.PENDING,Constants.PENDING_ICON),
            Destination(3,"Cairo",Constants.PENDING,Constants.PENDING_ICON),
            Destination(4,"Tahrir Square",Constants.DONE,Constants.DONE_ICON),
            Destination(5,"Obour",Constants.PENDING,Constants.PENDING_ICON),
            Destination(6,"El Salam",Constants.PENDING,Constants.PENDING_ICON),
            Destination(7,"El Marg",Constants.DONE,Constants.DONE_ICON),
            Destination(8,"6th of October",Constants.PENDING,Constants.PENDING_ICON)
        )
        adapter.submitList(destinationList)
        binding.rvDestinationList.adapter = adapter

        binding.btnAddDestination.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }
    }
}