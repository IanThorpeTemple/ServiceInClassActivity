package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivity : AppCompatActivity() {

    lateinit var timerTextView : TextView

    lateinit var timerBinder : TimerService.TimerBinder
    var isConnected = false

    val timerHandler = Handler(Looper.getMainLooper()){
        timerTextView.text = it.what.toString()
        true
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(timerHandler)
            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.textView)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            if (startButton.text == "Start" && isConnected) {
                startButton.text = "Pause"
                timerBinder.start(100)
            } else if (startButton.text == "Pause" && isConnected) {
                startButton.text = "Start"
                timerBinder.pause()
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isConnected){
                timerBinder.stop()
            }
        }
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}

//class MyViewModel : ViewModel() {
//
//    val myList = MutableList(10){"Test"}
//
//    val flag = MutableLiveData<Int>().apply{
//        value = 0
//    }
//
//    fun addElement(element: String){
//        myList.add(element)
//        flag.value = myList.size - 1
//    }
//
//    fun changeElement(index: Int, element: String){
//        myList[index] = element
//        flag.value = index
//    }
//}