package edu.temple.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    val myList = MutableList(10){"Test"}

    val flag = MutableLiveData<Int>().apply{
        value = 0
    }

    fun addElement(element: String){
        myList.add(element)
        flag.value = myList.size - 1
    }

    fun changeElement(index: Int, element: String){
        myList[index] = element
        flag.value = index
    }
}