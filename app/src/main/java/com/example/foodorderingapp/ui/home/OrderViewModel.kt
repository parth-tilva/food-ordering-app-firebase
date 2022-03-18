package com.example.foodorderingapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderingapp.data.model.Canteen
import com.example.foodorderingapp.data.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class OrderViewModel: ViewModel() {
    private val TAG = "viewModel"

    private val uid = FirebaseAuth.getInstance().uid!!

    val firestore = FirebaseFirestore.getInstance()


    //val docRef = firestore.collection("users").document(uid)

    private val _order = MutableLiveData<MutableList<Food>>()  // mutableList??
    val order :LiveData<MutableList<Food>> = _order

    private val _foodList = MutableLiveData<List<Food>>()
    val foodList: LiveData<List<Food>>  = _foodList


//    private val _newOrders = mutableListOf<Food>()
//    val newOrders : MutableList<Food> = _newOrders

    private val _currentFood = MutableLiveData<Food>()
    val currentFood : LiveData<Food> = _currentFood


    private val _currentCanteen = MutableLiveData<Canteen>()
    val currentCanteen: LiveData<Canteen> = _currentCanteen

    private var _count = MutableLiveData<Int>(0)
    val count :LiveData<Int> = _count


    fun setFood(food: Food){
        _currentFood.value = food
    }

    fun setCanteen(canteen: Canteen){
        _currentCanteen.value = canteen
    }

    fun removeOrder(foodPar: Food? = _currentFood.value){
        var food = foodPar!!
        _order.value = _order.value?.minus(food) as MutableList<Food>
        val list :MutableList<Food> =  (_foodList.value as MutableList<Food>?)!!
        val pos = list.indexOf(food)
        food = food.apply { isOrdered = false }
        list[pos] = food
        _foodList.postValue(list)
        _count.value = _count.value?.minus(1)
        Log.d(TAG,"${_order.value?.size} and orderinviewmodel vale is ${order.value}")
        Log.d(TAG,"${foodList.value}updated _foodlist")
    }

    fun orderReset(){
        _order.value = mutableListOf()
        _count.value = 0
        if(order.value==null){
            println("null")
        }else{
            print("Not null")
        }
    }


    fun addOrder(){
        var food = _currentFood.value!! //.apply { isOrdered = true }
        val list :MutableList<Food> =  (_foodList.value as MutableList<Food>?)!!
        val pos = list.indexOf(food)
        food = food.apply { isOrdered = true
                            quantity=1}
        list[pos] = food
        _foodList.postValue(list)

        if(_order.value == null){
            _order.value = listOf(food) as MutableList<Food>
        }else{
            _order.value = _order.value!!.plus(food) as MutableList<Food>
        }
        _count.value = _order.value!!.size
        Log.d(TAG,"${_order.value?.size} and orderinviewmodel vale is ${order.value}")
        Log.d(TAG,"${foodList.value}updated _foodlist")
    }

    fun initFoodList(foodList: List<Food>){
        _foodList.value = foodList
    }

    fun increaseQ(food: Food){
        val list = _order.value as MutableList<Food>
        val pos = list.indexOf(food)
        list[pos].apply { ++quantity }
        _order.value = list
        Log.d(TAG,"pos: $pos changed ${list[pos]}")
    }


    fun decreaseQ(food: Food){
        val list = _order.value as MutableList<Food>
        val pos = list.indexOf(food)
            //list[pos] = list[pos].apply { quantity-- }
        list[pos].apply { quantity-- }   /// workssssssss
        //_order.value = list
        _order.postValue(list)
        Log.d(TAG,"pos: $pos changed ${list[pos]}")
    }



}


//
//interface OnGetDataListener{
//    fun onSuccessIn(dataSnapshot: DocumentSnapshot)
//
//}
//
//fun readData(food: Food,listener: OnGetDataListener){
//    var id:String = ""
//    docRef.collection("Orders").whereEqualTo("name" ,food.name)  //quantity
//        .get()
//        .addOnSuccessListener { query ->
//            val documents = query.documents
//            documents.forEach{
//
//                listener.onSuccessIn(it)
//                id = it.id
//                Log.d(TAG,"getid called id is: $id")
//            }
//        }
//        .addOnFailureListener { exception ->
//            Log.w(TAG, "Error getting documents: ", exception)
//        }
//}
//
//fun getId(food: Food) :String {
//    var id:String = ""
//    readData(food, object : OnGetDataListener {
//        override fun onSuccessIn(dataSnapshot: DocumentSnapshot) {
//            id = dataSnapshot.id
//        }
//    })
//    return id
//}


//   docRef.collection("Orders").document(id) // decrement quantity
//            .update("quantity",FieldValue.increment(-1))
//        docRef.collection("Orders").document(id)
//            .get()
//            .addOnSuccessListener {
//                Log.d(TAG,"name is${it.id}:${it.data}")
//                val foodObj = it.toObject<Food>()!!
//                if(foodObj.quantity<=0){
//                    removeDocFood(id)
//                }
//            }
//                .addOnFailureListener {
//                    Log.d(TAG,"fauil in geting updating orders")
//                }




//fun addToArray(food: Food){
//        //_count.value!!.plus(1)
//        docRef.update("orders",FieldValue.arrayUnion(food))
//            .addOnSuccessListener {
//                Log.d(TAG,"order added")
//            }
//            .addOnFailureListener {
//                Log.d(TAG,"fail to order user $it")
//            }
//    }
//
//    fun removeToArray(food:Food){
//
//        docRef.update("orders",FieldValue.arrayRemove(food))
//            .addOnSuccessListener {
//                Log.d(TAG,"order added")
//            }
//            .addOnFailureListener {
//                Log.d(TAG,"fail to order user $it")
//            }
//    }
//
//    fun addOrder(){
//        var alreadyOrdered = false
//        try{
//            docRef.get().addOnCompleteListener { task ->
//                if (task.isSuccessful()) {
//                    val document: DocumentSnapshot = task.result
//                    if (document.exists()) {
//                        val user = document.toObject<User>()
//                        for(user in user!!.orders){
//                            if( user.name == _currentFood.value!!.name){
//                                alreadyOrdered = true
//                                increaseQ(user)
//                            }
//                        }
//                        if(!alreadyOrdered){
//                            _count.value = _count.value!! + 1
//                            _currentFood.value!!.quantity = 1
//                            addToArray(_currentFood.value!!)
//                        }
//                        Log.d(TAG,"geting order list ${user}")
//                    }
//                }
//            }
//        }catch (e:Exception){
//            Log.d(TAG,"error in toObject $e")
//        }
//    }
//    fun increaseQ(food: Food){
//        removeToArray(food)
//        food.quantity += 1
//        val newOrder = food
//        addToArray(newOrder)
//    }
//
//    fun decreaseQ(food: Food){
//        val num = food.quantity
//        if(num>1){
//            removeToArray(food)
//            food.quantity -= 1
//            val newOrder = food
//            addToArray(newOrder)
//        }
//        else{
//            _count.value = _count.value !! - 1
//            removeToArray(food)
//        }
//    }










// docRef.update("orders",FieldValue.arrayUnion(_currentFood.value))
//                    .addOnSuccessListener {
//                Log.d(TAG,"order added")
//            }
//            .addOnFailureListener {
//                Log.d(TAG,"fail to order user $it")
//            }



//try{
//    docRef.get().addOnCompleteListener { task ->
//        if (task.isSuccessful()) {
//            val document: DocumentSnapshot = task.getResult()
//            if (document.exists()) {
//                val users =
//                    document["users"] as List<HashMap<String,Food>>
//
//                Log.d(TAG,"cating $users")
//            }
//        }
//    }
//}catch (e:Exception){
//    Log.d(TAG,"casting")
//}











//                    List<HashMap<String,Food>>

//fun updateDatabase(){
//        try{
//            val firestore = FirebaseFirestore.getInstance()
//            val order = order.value!!
//            val data = hashMapOf("orders" to order)
//            firestore.collection("users").document("user")
//                .set(data, SetOptions.merge())//, SetOptions.merge()
//                .addOnSuccessListener {
//                    Log.d(TAG,"order added")
//                }
//                .addOnFailureListener {
//                    Log.d(TAG,"fail to order user")
//                }
//        }catch (e:Exception){
//            Log.d(TAG,"erroe in adding order is $e")
//        }
//    }
//    fun increaseQ(food: Food){
//        _currentFood.value = food
//        _order.value!!.remove(_currentFood.value)
//        _currentFood.value!!.quantity += 1
//        _currentFood.value?.let { _order.value!!.add(it) }
//        updateDatabase()
//    }
//    fun decreaseQ(food: Food){
//        _currentFood.value = food
//        if(_currentFood.value!!.quantity>1){
//            _order.value!!.remove(_currentFood.value)
//            _currentFood.value!!.quantity -= 1
//            _currentFood.value?.let { _order.value!!.add(it) }
//        }
//        else{
//            try{
//                order.value!!.remove(_currentFood.value)
//                order.value!!.forEach {
//                    if(it.name==_currentFood.value!!.name){
//                        _order.value!!.remove(it)
//                    }
//                }
//            }catch(e:Exception){
//                Log.d(TAG,"error in quantity $e")
//            }
//        }
//        updateDatabase()
//    }










//        if(_currentFood.value!!.quantity==0){
//            _currentFood.value!!.quantity = 1
//            val food = _currentFood.value!!
//            _order.value!!.add(food)
//
//            _count.value = _order.value!!.size
//            updateDatabase()
//        }
//        else{
//            currentFood.value?.let { increaseQ(it) }
//        }






//        currentFood
//            .addOnSuccessListener { DocumentSS ->
//             // Log.d(TAG,"name is${DocumentSS.id}:${DocumentSS.data}")
//                foodObj = DocumentSS.toObject<Food>()!!
//                //Log.d(TAG,"food name          is            ${foodObj.name}")
//                loadData(foodObj)
//            }.addOnFailureListener {
//                Log.d(TAG,"loggggggl")
//            }




//        pplicationIdRef.get().addOnCompleteListener(task -> { //javacode
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    List<Map<String, Object>> users = (List<Map<String, Object>>) document.get("users");
//                }
//            }
//        });