package com.example.instabugtask.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.instabugtask.R
import com.example.instabugtask.data.model.ApiEntity
import com.example.instabugtask.databinding.ItemRvBinding

class CachedNetworkAdapter(private val activity: Activity) :
    RecyclerView.Adapter<CachedNetworkAdapter.CachedNetworkViewHolder>() {
    private var apiEntityList = ArrayList<ApiEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CachedNetworkViewHolder {
        val binding: ItemRvBinding = ItemRvBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return CachedNetworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CachedNetworkViewHolder, position: Int) {
        if (apiEntityList[position].responseCode != "200") {
            holder.llError.visibility = View.VISIBLE
            holder.tvResponseCode.setTextColor(
                ContextCompat.getColor(
                    activity.applicationContext,
                    R.color.red
                )
            )
        } else {
            holder.llError.visibility = View.GONE
            holder.tvResponseCode.setTextColor(
                ContextCompat.getColor(
                    activity.applicationContext,
                    R.color.teal_200
                )
            )
        }

        holder.tvResponseBody.text = apiEntityList[position].output
        holder.tvHeaders.text = apiEntityList[position].headers
        holder.tvResponseCode.text = apiEntityList[position].responseCode
        holder.tvQueryAndBodyRequest.text = apiEntityList[position].queryBody
        holder.tvError.text = apiEntityList[position].error
        holder.tvId.text = apiEntityList[position].id.toString()
        holder.tvUrl.text = apiEntityList[position].requestURL

    }


    override fun getItemCount(): Int {
        return apiEntityList.size
    }

    fun setList(apiEntityList: ArrayList<ApiEntity>) {
        this.apiEntityList = apiEntityList
        notifyDataSetChanged()
    }

    class CachedNetworkViewHolder(view: ItemRvBinding) : RecyclerView.ViewHolder(view.root) {
        val tvResponseCode: TextView
        val tvBodyRequest: TextView
        val tvError: TextView
        val tvHeaders: TextView
        val tvQueryAndBodyRequest: TextView
        val tvResponseBody: TextView
        val tvId: TextView
        val tvUrl: TextView
        var llError: LinearLayout


        init {
            // Define click listener for the ViewHolder's View.
            tvResponseCode = view.tvResponseCode
            tvBodyRequest = view.tvBodyRequest
            tvError = view.tvError
            tvHeaders = view.tvHeaders
            tvQueryAndBodyRequest = view.tvQueryAndBodyRequest
            tvResponseBody = view.tvResponseBody
            tvId = view.tvId
            tvUrl = view.tvUrl
            llError = view.llError

        }
    }

}