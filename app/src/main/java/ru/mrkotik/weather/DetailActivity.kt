package ru.mrkotik.weather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class DetailActivity : AppCompatActivity() {
    private var day_1_00: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        day_1_00 = findViewById(R.id.day_1_00)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart(){
        super.onStart()
        val city = intent.getStringExtra("city")
        val key = intent.getStringExtra("key")
        val url: String = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$key&units=metric&lang=ru"

        doAsync {
            val apiResponse = URL(url).readText()
            val dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss")
            var id = 0

            val list = JSONObject(apiResponse).getJSONArray("list").getJSONObject(id)
            val weather = list.getJSONArray("weather")
            val main = list.getJSONObject("main")
            val wind = list.getJSONObject("wind")

            val dt = list.getString("dt_txt")
            val day: String = LocalDate.parse(dt, dateformatter).dayOfMonth.toString() + " " + LocalDate.parse(dt, dateformatter).month.toString()
            val time = LocalTime.parse(dt, dateformatter).hour.toString()
            val desc = weather.getJSONObject(0).getString("description")
            val temp = main.getString("temp")
            val humidity = main.getString("humidity")
            val speed = wind.getString("speed")
            val gust = wind.getString("gust")








            synchronized(this){
                runOnUiThread({
                    day_1_00?.text = "$day $time \n$desc \n $temp°, $speed м/с "
                })
            }
        }
    }
}