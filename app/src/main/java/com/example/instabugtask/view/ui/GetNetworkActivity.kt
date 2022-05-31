package com.example.instabugtask.view.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.instabugtask.R
import com.example.instabugtask.databinding.ActivityNetworkBinding
import com.example.instabugtask.model.HeaderRequest
import com.example.instabugtask.utils.HTTPCallback
import com.example.instabugtask.utils.HTTPReqTask
import com.example.instabugtask.utils.HTTPRequest


class GetNetworkActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNetworkBinding
    private var headerRequestList = ArrayList<HeaderRequest>()
    private lateinit var mError: String
    private lateinit var mOutput: String
    private lateinit var mResponseCode: String
    private lateinit var mHeaders: String
    private lateinit var mQueryBody: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHeaders = ""
        mQueryBody = ""
        mResponseCode = ""
        mOutput = ""
        mError = ""
        binding = ActivityNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvRequestName.text = "GET Request"
        binding.buttonAdd.text = "Add Request Query"
        binding.tvBodyRequest.text = "Query Request:"
        binding.btnGoToRequest.text = "Go to Post Request"
        if (isOnline(this)) {
            drawView(isOnline(this))


            // This Submit Button is used to store all the
            // data entered by user in arraylist
            binding.buttonSendRequest.setOnClickListener {
                try {
                    if (binding.url.text.isEmpty()) {
                        Toast.makeText(this, "please, enter a URL", Toast.LENGTH_SHORT).show()
                    } else {
                        drawView(isOnline(this))
                        val requestHttp =
                            HTTPRequest(
                                binding.url.text.toString(),
                                postDataParams = headerRequestList,
                                asyncResponse = object :
                                    HTTPCallback {
                                    override fun processFinish(
                                        output: String,
                                        responseCode: Int,
                                        queryBody: String,
                                        headers: String
                                    ) {
                                        binding.llError.visibility = View.GONE
                                        binding.tvResponseCode.setTextColor(getColor(R.color.teal_200))

                                        mOutput = output
                                        mResponseCode = responseCode.toString()
                                        mHeaders = headers
                                        mQueryBody = queryBody

                                        binding.tvResponseBody.text = mOutput
                                        binding.tvHeaders.text = mHeaders
                                        binding.tvResponseCode.text = mResponseCode
                                        binding.tvQueryAndBodyRequest.text = mQueryBody
                                        binding.tvError.text = mError
                                    }

                                    override fun processFailed(
                                        output: String,
                                        responseCode: Int,
                                        queryBody: String,
                                        headers: String,
                                        error: String
                                    ) {
                                        binding.llError.visibility = View.VISIBLE
                                        binding.tvResponseCode.setTextColor(getColor(R.color.red))
                                        mOutput = output
                                        mResponseCode = responseCode.toString()
                                        mHeaders = headers
                                        mQueryBody = queryBody
                                        mError = error

                                        binding.tvResponseBody.text = mOutput
                                        binding.tvHeaders.text = mHeaders
                                        binding.tvResponseCode.text = mResponseCode
                                        binding.tvQueryAndBodyRequest.text = mQueryBody
                                        binding.tvError.text = mError

                                    }
                                },
                                requestType = "GET"
                            )
                        requestHttp.execute()
                    }
                } catch (e: Exception) {
                    drawView(isOnline(this))
                }
                saveData()
            }


        } else {
            drawView(isOnline(this))
        }

        binding.btnTryAgain.setOnClickListener {
            Toast.makeText(this, "${isOnline(this)}", Toast.LENGTH_SHORT).show()
            if (isOnline(this)) {
                binding.llInternet.visibility = View.VISIBLE
                binding.llNoInternet.visibility = View.GONE
            }
        }


        binding.btnGoToRequest.setOnClickListener {
            startActivity(Intent(this, NetworkActivity().javaClass))
        }
        // This addButton is used to add a new view
        // in the parent linear layout
        binding.buttonAdd.setOnClickListener {
            addNewView()
        }

    }

    // This function is called after
    // user clicks on addButton
    private fun addNewView() {
        // this method inflates the single item layout
        // inside the parent linear layout
        val inflater = LayoutInflater.from(this).inflate(R.layout.item_add, null)
        binding.parentLinearLayout.addView(inflater, binding.parentLinearLayout.childCount)

    }

    // This function is called after user
    // clicks on Submit Button
    private fun saveData() {
        headerRequestList.clear()
        // this counts the no of child layout
        // inside the parent Linear layout
        val count = binding.parentLinearLayout.childCount
        var v: View?

        for (i in 0 until count) {
            v = binding.parentLinearLayout.getChildAt(i)

            val key: EditText = v.findViewById(R.id.etKey)
            val value: EditText = v.findViewById(R.id.etValue)

            // create an object of Language class
            val headerRequest = HeaderRequest()
            headerRequest.key = key.text.toString()
            headerRequest.value = value.text.toString()

            // add the data to arraylist
            headerRequestList.add(headerRequest)
        }
    }


    /*
    get or  post
    get via url
    post via both

     */


    private fun isOnline(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun drawView(isOnline: Boolean) {
        if (isOnline) {
            binding.llInternet.visibility = View.VISIBLE
            binding.llNoInternet.visibility = View.GONE
        } else {
            binding.llInternet.visibility = View.GONE
            binding.llNoInternet.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("mOutput", mOutput)
        outState.putString("error", mError)
        outState.putString("mHeaders", mHeaders)
        outState.putString("mQueryBody", mQueryBody)
        outState.putString("mResponseCode", mResponseCode)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mOutput = savedInstanceState.getString("mOutput", "")
        mError = savedInstanceState.getString("mError", "")
        mHeaders = savedInstanceState.getString("mHeaders", "")
        mQueryBody = savedInstanceState.getString("mQueryBody", "")
        mResponseCode = savedInstanceState.getString("mResponseCode", "")


        binding.tvResponseBody.text = mOutput
        binding.tvHeaders.text = mHeaders
        binding.tvResponseCode.text = mResponseCode
        binding.tvQueryAndBodyRequest.text = mQueryBody
        binding.tvError.text = mError

    }

}
