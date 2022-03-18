package com.example.foodorderingapp.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.foodorderingapp.R
import com.example.foodorderingapp.data.model.User
import com.example.foodorderingapp.databinding.ActivitySignInBinding
import com.example.foodorderingapp.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase


class SignInActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 123
    private val TAG = "SignInActivity Tag"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    private lateinit var mFirebase:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {// ffffffffooooooooooooddddddddddd
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Configure Google Sign In
        Log.d(TAG,"onstart oncreacte called")


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.signInButton.setOnClickListener{
            signIn()
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onstart called")
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }



    private fun signIn() {
        val signIntent = googleSignInClient.signInIntent
        startActivityForResult(signIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        binding.signInButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    mFirebase = FirebaseFirestore.getInstance()
                    val name = user?.displayName ?:""
                    val uid = user?.uid ?:""
                    val pUrl = user?.photoUrl.toString()
                    try{
                        mFirebase.collection("users").document(uid)
                            .set(User(name,pUrl,uid), SetOptions.merge())
                            .addOnSuccessListener {
                                Log.d(TAG,"use added")
                            }
                            .addOnFailureListener {
                                Log.d(TAG,"fail to add user")
                            }
                    }catch (e:Exception){
                        Log.d(TAG,"erroe in adding is $e")
                    }
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null){



            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
        else{
            binding.signInButton.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            Log.d("result","signing fail ")
        }
    }

}

//currentUser.displayName
//            currentUser.email
//            currentUser.photoUrl
//            currentUser.uid
//            order arry
//
