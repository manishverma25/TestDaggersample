package com.africa.airtel.testsample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }


        btn.setOnClickListener {
            textview_second.text = ""
            textview_third.text = ""
            fetchIccId()
        }

        val subscriptionInfoList = getSimSerialNumberWithOrWithoutFilter(this.requireContext())
        subscriptionInfoList?.forEach { item ->
            Log.d("mvv", "subscriptionInfoList  ${item}")
            Log.d("mvv",
                "subscriptionInfoList  item.iccId  ${item.iccId}   item.number ${item.number}"
            )
        }
        textview_third.text = "Sim Serial : " + getSImSerial(this.requireContext())
        fetchIccId()
    }

    fun fetchIccId(){
        val subscriptionInfoList =  getSimSerialNumberWithOrWithoutFilter(this.requireContext())
        subscriptionInfoList?.forEach { item ->

            textview_second.text =   textview_second.text.toString() + "\n"+"IccId  : "+item.iccId  +" msisdn : "+item.number
            Log.d("mvv","subscriptionInfoList  ${item}")
            Log.d("mvv","subscriptionInfoList  item.iccId  ${item.iccId}   item.number ${item.number}")
        }
        textview_third.text = "Sim Serial : "+getSImSerial(this.requireContext())

    }

    fun getSImSerial(context: Context): String? {
        var desc = ""
        val applicationContext: Context =
            context.applicationContext
        try {
            val tm =
                applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ){
                desc = tm.simSerialNumber
            }
        } catch (e: Exception) {
            e.printStackTrace()
            desc = e.toString()
        }
        return desc
    }


    fun getSimSerialNumberWithOrWithoutFilter(
        context: Context
    ): List<SubscriptionInfo> ?{
        var simSerialNumber: String? = null
        val applicationContext: Context =
            context.getApplicationContext()
        var  infoList : List<SubscriptionInfo>? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) try {
            val manager = SubscriptionManager.from(applicationContext)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return infoList
            }
            infoList =
                manager.activeSubscriptionInfoList
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return infoList
    }

}
