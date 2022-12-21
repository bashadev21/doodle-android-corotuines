package com.example.coroutines

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    val TAG="MainActivity";
    private lateinit var textView: TextView
    private lateinit var btn: Button
    private lateinit var list:ArrayList<DataModelItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        suspendFun()
//        runBlocking()
//        coroutineJob()
//        asyncFun()
//        scopes()
        apiCall()
    }
    private fun apiCall(){
        list= ArrayList()


        val retrofit: Retrofit =
            Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build()
        val api:APIInterface=retrofit.create(APIInterface::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            //api call
            val response=api.getData()
            if(response.isSuccessful){
                for(myData in response.body()!!){
                    println(myData.toString()+"sssss")

                }
            }else{
                println( response.message().toString())
            }



        }

///Normal method/////////////
//    var call:Call<DataModel> =api.getData()
//    call.enqueue(object: Callback<DataModel?>{
//        override fun onResponse(call: Call<DataModel?>, response: Response<DataModel?>) {
//            if(response.isSuccessful){
//                list.clear()
//                for(myData in response.body()!!){
//                    list.add(myData)
//                }
//                adaptor.notifyDataSetChanged()
//                recyleView.adapter=adaptor
//            }
//        }
//
//        override fun onFailure(call: Call<DataModel?>, t: Throwable) {
//            println(t.message.toString()+"ssss")
//        }
//
//    })
    }
fun scopes(){
    //Lifecyle scope viewmodelscope
    btn=findViewById(R.id.startbtn)
    btn.setOnClickListener(){
        println("fhgfgf")
        lifecycleScope.launch(Dispatchers.IO) {
            while (true){
                delay(1000)
                Log.d(TAG,"running")
            }


        }
        GlobalScope.launch {
            delay(5000)
            val i= Intent(this@MainActivity,EmptyActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
    fun asyncFun(){
//        textView=findViewById(R.id.text_view)
        GlobalScope.launch {

//            val ans=     doNetworkCall()
//            val ans2=     doNetworkCall2()
         val job1=   async {
              doNetworkCall()
            }
            val job2=   async {
              doNetworkCall2()
            }

//            withContext(Dispatchers.Main){
//                textView.text=ans
//            }

            Log.d(TAG,job1.await())
            Log.d(TAG,job2.await())
        }
    }
    fun coroutineJob(){
       val job= GlobalScope.launch(Dispatchers.IO) {
           for (x in 40..100){
               if(isActive){
                   Log.d(TAG,"Result: $x :${fib(x)}")
               }

           }
//            repeat(5){
//                Log.d(TAG,"running")
//                delay(1000)
//            }
        }
        runBlocking(){
            delay(1000)
            job.cancel()
            Log.d(TAG,"Cancel running")

        }
    }
    fun fib(n : Int):Long{
        return if( n==0 ) 0
        else if(n==1)1
        else fib(n-1)+fib(n-2)
    }
    fun runBlocking(){
        runBlocking(){
            Log.d(TAG,"start")

            launch(Dispatchers.IO) {
                delay(3000)
                Log.d(TAG,"first coroutine")
            }
            launch(Dispatchers.IO) {
                delay(3000)
                Log.d(TAG,"second coroutine")
            }
            delay(3000)

            Log.d(TAG,"end")
        }
        Log.d(TAG,"final end")
    }

    fun suspendFun(){
//        textView=findViewById(R.id.text_view)
        GlobalScope.launch {

       val ans=     doNetworkCall()
       val ans2=     doNetworkCall2()
            withContext(Dispatchers.Main){
                textView.text=ans
            }

            Log.d(TAG,ans)
            Log.d(TAG,ans2)
        }
    }
    suspend fun doNetworkCall():String{
        delay(3000)

        Log.d(TAG,"first")

        return "first call"

    }
    suspend fun doNetworkCall2():String{
        delay(3000)
        Log.d(TAG,"second")

        return "second call"

    }
}
var BASEURL="https://jsonplaceholder.typicode.com/"

data class User(val name: String, var rating: Int)

interface APIInterface {
    @GET("posts")
    suspend fun getData(): Response<DataModel>
}

class DataModel : ArrayList<DataModelItem>()

data class DataModelItem(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
)