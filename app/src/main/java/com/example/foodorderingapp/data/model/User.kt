package com.example.foodorderingapp.data.model

data class User (
    val displayName:String = "",
    val userUrl:String = "",
    val uid: String = "",
    val orders: List<Food> = listOf(Food()),  //MutableList<Food> = mutableListOf<Food>() ,
    val orderCount: Int = 0
)

//currentUser.displayName
//            currentUser.email
//            currentUser.photoUrl
//            currentUser.uid
//            order arry
//