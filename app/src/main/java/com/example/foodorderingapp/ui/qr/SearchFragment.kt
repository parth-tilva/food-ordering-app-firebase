package com.example.foodorderingapp.ui.qr

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.foodorderingapp.data.model.Canteen
import com.example.foodorderingapp.databinding.FragmentSearchBinding
import com.example.foodorderingapp.ui.home.CanteenAdapter
import com.example.foodorderingapp.ui.home.HomeFragmentDirections
import com.example.foodorderingapp.ui.home.OrderViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private val CAMERA_REQUEST_CODE = 101
    private val TAG =" LOG"

    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerview:CodeScannerView
    private var _binding: FragmentSearchBinding? = null
    private val viewModel: OrderViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var mFirestore:FirebaseFirestore
    var prevUpiId=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_search, container, false)
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        setUpPermission()
        codeScannerfun()

        return binding.root
    }


    private fun getId(str:String, start:String, end:String):String{  ////upi://pay?pa=7433033464@paytm&pn=PaytmUser&mc=0000&mode=02&purpose=00&orgid=159761
        var startLen = start.length
        var endLen = end.length
        var len = str.length
        var upi=""
        for(i in str.indices){
            println(i)
            var j=i+startLen
            if(j>=len){
                println("error")
                break;
            }
            println(str.subSequence(i,j))
            if(str.subSequence(i,j)==start){
                j++
                while(j<str.length && str.subSequence(j,(j+endLen))!=end){
                    upi += str[j]
                    j++
                }
                if(j==str.length){
                    print("error")
                }else{
                    break;
                }
            }
        }
        print("final found: $upi")
        return upi
    }


    fun navigate(upiId:String){
        Log.d(TAG,"navigate called ")

        mFirestore = FirebaseFirestore.getInstance()
        var canteenId = ""
        lateinit var canteen: Canteen

//        mFirestore.collection("restaurants")
//            .whereEqualTo("Id",upiId)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "Error getting documents: ", exception)
//            }

        mFirestore.collection("restaurants").whereEqualTo("Id", upiId)
            .get()
            .addOnSuccessListener { documents ->
                if(documents.isEmpty){
                    showNotFound()
                }
                Log.d(TAG,"success foound upiid ${documents.documents.size}")
                Log.d(TAG,"success foound upiid ${documents.metadata }}")
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    canteenId = document.id
                    canteen =  document.toObject<Canteen>()
                    Log.d(TAG,"canteen: $canteen, name: ${canteen.name}")
                    Log.d(TAG,"canteenid : $canteenId")
                    viewModel.setCanteen(canteen)
                    val action = SearchFragmentDirections.actionSearchFragmentToFoodsListFragment(canteenId = canteenId)
                    findNavController().navigate(action)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG,"fail listener :")
                Log.w(TAG, "Error getting documents: $exception", exception)
            }
    }

    private fun showNotFound(){
        binding.tvTextView.text = "QR code is not registered"
    }

    private fun codeScannerfun(){

        codeScanner = CodeScanner(requireContext(),binding.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
              //  Toast.makeText(requireContext(),"result of scan is: ${it.text}",Toast.LENGTH_LONG).show()
                Log.d(TAG,"decoded succedfully ")
                getActivity()?.runOnUiThread{
                    var str = it.text     //upi://pay?pa=7433033464@paytm&pn=PaytmUser&mc=0000&mode=02&purpose=00&orgid=159761
                    val startS = "upi://pay?pa"
                    val endS = "&pn="


                    if(it.text!=prevUpiId){
                        prevUpiId = it.text
                        val upiId = getId(str,startS, endS)
                        binding.tvTextView.text = "url: ${ it.text } \nid: $upiId \n redirecting.."
                        //navigate to foodlist and update viewmodel
                        navigate(upiId)
                    }
                }
            }
            errorCallback = ErrorCallback {
                Log.e(TAG,"erorr in inilization codesanner")
            }
        }
        binding.scannerView.setOnClickListener{
            Log.e(TAG,"on Click listner start preview")
            codeScanner.startPreview()
        }

    }

    private fun setUpPermission(){
        val permission = ContextCompat.checkSelfPermission(requireContext(),
            android.Manifest.permission.CAMERA
        )
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
        else{
            //Log.e(TAG,"permission granted ")           // permission granted
        }
    }

    private fun makeRequest(){
        Log.e(TAG,"make Request called ")

        requestPermissions(
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        Log.e(TAG,"request permission result when block in fun")
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(requireContext(),"You need the camera permission to be able to scan QR code",Toast.LENGTH_LONG).show()
                }
                else{
                    codeScanner.startPreview()
                    Log.e(TAG,"request result ofpermission granted ")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG,"on start start preview ")
        codeScanner.startPreview()
    }

    override fun onStop() {
        super.onStop()
      //  Log.e(TAG,"release resources")
        codeScanner.releaseResources()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}


