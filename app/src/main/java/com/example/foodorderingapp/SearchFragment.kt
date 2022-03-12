package com.example.foodorderingapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.foodorderingapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private val CAMERA_REQUEST_CODE = 101
    private val TAG =" LOG"

    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerview:CodeScannerView
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


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
                   // Log.d(TAG,"decoded succedfully on  ui main thred ")
                    // Toast.makeText(requireContext(),"result of scan is: ${it.text}",Toast.LENGTH_LONG).show()
                    binding.tvTextView.text = it.text
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


