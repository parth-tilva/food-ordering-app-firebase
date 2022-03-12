package com.example.foodorderingapp.data.model

data class Food (
    val name:String = "",
    val price:Int = 0,
    val isVeg:Boolean = false,
    val foodDes:String = "",
    val photo:String ="",
    var quantity:Int = 0
)
