package com.example.foodorderingapp.ui.payment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent

import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Log

import java.util.*
import kotlin.collections.ArrayList
import com.example.foodorderingapp.databinding.FragmentPaymentBinding
import android.R

import android.app.Activity
import android.widget.*

import androidx.activity.result.ActivityResultCallback

import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.foodorderingapp.ui.home.OrderViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PaymentFragment : Fragment() {

    lateinit var binding: FragmentPaymentBinding
    val navigationArgs: PaymentFragmentArgs by navArgs()
    private val viewModel: OrderViewModel by activityViewModels()
    lateinit var progressBar: ProgressBar
//
//    var amount: EditText? = null
//    var note:EditText? = null
//    var name:EditText? = null
//    var upivirtualid:EditText? = null
//    var send: Button? = null
//    var TAG = "main"
    val UPI_PAYMENT = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val canteen = viewModel.currentCanteen.value!!
        val send = binding.send
       val  amount = binding.amountEt
        val note = binding.note
        val name = binding.name
        val upivirtualid = binding.upiId
        progressBar = binding.progressBar


        //amount.setText(navigationArgs.amount)
        name.setText(canteen.ownerName)
        upivirtualid.setText(canteen.Id)
        try {
            amount.setText(navigationArgs.amount.toString())
        }catch (e:Exception){
            Toast.makeText(requireContext(), "An error occurred $e", Toast.LENGTH_LONG)
                .show()
        }

        send.setOnClickListener { //Getting the values from the EditTexts
            progressBar.visibility = View.VISIBLE
            if (TextUtils.isEmpty(name.text.toString().trim { it <= ' ' })) {
                Toast.makeText(requireContext(), " Name is invalid", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(upivirtualid.text.toString().trim { it <= ' ' })) {
                Toast.makeText(requireContext(), " UPI ID is invalid", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(note.text.toString().trim { it <= ' ' })) {
                Toast.makeText(requireContext(), " Note is invalid", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(amount.text.toString().trim { it <= ' ' })) {
                Toast.makeText(requireContext(), " Amount is invalid", Toast.LENGTH_SHORT).show()
            } else {
                pay(
                    name.text.toString(), upivirtualid.text.toString(),
                    note.text.toString(), amount.text.toString()
                )
            }
        }


    }


    fun payUsingUpi(name: String, upiId: String, note: String, amount: String) {
        Log.e("main ", "name $name--up--$upiId--$note--$amount")
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()
        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri
        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
        // check if intent resolves
        if (null != chooser.resolveActivity(requireContext().packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(
                requireContext(),
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    fun pay(name: String, upiId: String, note: String, amount: String){
        val uri: Uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", upiId) // virtual ID
            .appendQueryParameter("pn", name) // name
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount) // amount
            .appendQueryParameter("cu", "INR") // currency
            .build()


        val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
        val GOOGLE_PAY_REQUEST_CODE = 123
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
        if(activity!=null){
            activity!!.startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE)
           lifecycleScope.launch {
               delay(4000)
               onPaymentSuccess()
           }
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("main ", "response $resultCode")

        Log.e("main ", "response $resultCode")
        when (requestCode) {
            UPI_PAYMENT -> if (RESULT_OK === resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.e("UPI", "onActivityResult: $trxt")
                    val dataList: ArrayList<String?> = ArrayList()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null")
                    val dataList: ArrayList<String?> = ArrayList()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null")
                val dataList: ArrayList<String?> = ArrayList()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String?>) {
        if (isConnectionAvailable(requireContext())) {
            var str = data[0]
            Log.e("UPIPAY", "upiPaymentDataOperation: $str")
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&").toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=").toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].lowercase(Locale.getDefault()) == "Status".lowercase(Locale.getDefault())) {
                        status = equalStr[1].lowercase(Locale.getDefault())
                    } else if (equalStr[0].lowercase(Locale.getDefault()) == "ApprovalRefNo".lowercase(
                            Locale.getDefault()
                        ) || equalStr[0].lowercase(Locale.getDefault()) == "txnRef".lowercase(Locale.getDefault())
                    ) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") {
                //Code to handle successful transaction here.
                Toast.makeText(requireContext(), "Transaction successful.", Toast.LENGTH_SHORT)
                    .show()
                Log.e("UPI", "payment successfull: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(requireContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
                Log.e("UPI", "Cancelled by user: $approvalRefNo")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "failed payment: $approvalRefNo")
            }
        } else {
            Log.e("UPI", "Internet issue: ")
            Toast.makeText(
                requireContext(),
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected
                && netInfo.isConnectedOrConnecting
                && netInfo.isAvailable
            ) {
                return true
            }
        }
        return false
    }


    fun onPaymentSuccess(){
        progressBar.visibility = View.GONE
        val action = PaymentFragmentDirections.actionPaymentFragmentToComformationFragment()
        findNavController().navigate(action)
    }


}