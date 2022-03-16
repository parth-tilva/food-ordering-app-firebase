package com.example.foodorderingapp

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodorderingapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.badge.BadgeDrawable
import java.nio.file.Files.find

class MainActivity : AppCompatActivity() {

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private  lateinit var navController: NavController
    private lateinit var notificationsBadge: BadgeDrawable
    private lateinit var dialog: Dialog
    private val TAG = "test"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"onstart oncreacte called")



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
         navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController)   // dont know what is do????? handle action bar botton
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()

        val bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment,null,options)
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment,null,options)
                }
                R.id.orderFragment -> {
                    navController.navigate(R.id.orderFragment,null,options)
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment,null,options)
                }
            }
            true
        }

        bottomNavigationView.setOnNavigationItemReselectedListener { item ->
        return@setOnNavigationItemReselectedListener}



        notificationsBadge =
            bottomNavigationView.getOrCreateBadge(R.id.orderFragment)

        notificationsBadge.apply{
            maxCharacterCount = 3
            isVisible = false
        }

        viewModel.count.observe(this,{
            Log.d("test","mainactivity $it")
            notificationsBadge.number = it
            notificationsBadge.isVisible = it!=0
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

//if(it!=0)
//            notificationsBadge.isVisible = true
//            else
//                notificationsBadge.isVisible = false