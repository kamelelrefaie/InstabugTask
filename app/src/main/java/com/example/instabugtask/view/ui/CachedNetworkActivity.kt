package com.example.instabugtask.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instabugtask.R
import com.example.instabugtask.data.local.DBHelper
import com.example.instabugtask.databinding.ActivityCachedNetworkBinding
import com.example.instabugtask.view.adapter.CachedNetworkAdapter

class CachedNetworkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCachedNetworkBinding
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var dbHelper:DBHelper
    private lateinit var rvAdapter:CachedNetworkAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCachedNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initialize spinner adapter
        initSpinAdapter()
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = spinnerItemClickedListener()

        dbHelper = DBHelper(this, factory = null)
        binding.rv.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAdapter = CachedNetworkAdapter(this)
        val list = dbHelper.getRequests("GET")
        rvAdapter.setList(list)
        binding.rv.adapter=rvAdapter



    }

    private fun spinnerItemClickedListener (): AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
         override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
             val list = dbHelper.getRequests(parent?.getItemAtPosition(position).toString())
             rvAdapter.setList(list)

         }

         override fun onNothingSelected(p0: AdapterView<*>?) {
             TODO("Not yet implemented")
         }

     }
    private fun initSpinAdapter() {
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.method,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }
}