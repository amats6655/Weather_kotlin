package ru.mrkotik.weather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList


class DetailActivity : AppCompatActivity() {
    private var day_1_00: TextView? = null
    private var day_2_00: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        day_1_00 = findViewById(R.id.day_1_03)
        day_2_00 = findViewById(R.id.day_2_03)



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart(){
        super.onStart()
        val city = intent.getStringExtra("city")
        val key = intent.getStringExtra("key")
        val url = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$key&units=metric&lang=ru"

        doAsync {
            val apiResponse = URL(url).readText()

            synchronized(this){

                runOnUiThread {
                    val dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss")
                    var id = 0

// Преобразуем JSONArray в ArrayList

                    var list: JSONArray = JSONObject(apiResponse).getJSONArray("list")
                    val formatted_list = arrayListOf<String>()


                    // Запихиваем вытащенные и отформатированные данные в formattedlist
                    for (i in 0 until list.length()) {
                        if (LocalTime.parse(list.getJSONObject(i).getString("dt_txt"), dateformatter).hour.toString() == "0" ||
                            LocalTime.parse(list.getJSONObject(i).getString("dt_txt"), dateformatter).hour.toString() == "6" ||
                            LocalTime.parse(list.getJSONObject(i).getString("dt_txt"), dateformatter).hour.toString() == "12" ||
                            LocalTime.parse(list.getJSONObject(i).getString("dt_txt"), dateformatter).hour.toString() == "18"){
                            continue
                        }
                        val weather = list.getJSONObject(i).getJSONArray("weather")
                        val main = list.getJSONObject(i).getJSONObject("main")
                        val wind = list.getJSONObject(i).getJSONObject("wind")

                        val dt = list.getJSONObject(i).getString("dt_txt")
                        val day = LocalDate.parse(dt, dateformatter).dayOfMonth.toString() + " " + LocalDate.parse(dt, dateformatter).month.toString()
                        val time = LocalTime.parse(dt, dateformatter).hour.toString()
                        val desc = weather.getJSONObject(0).getString("description")
                        val temp = main.getString("temp")
                        val speed = wind.getString("speed")
                        formatted_list.add("$day $time:00 \n$desc \n$temp°, $speed м/с")
                    }




//                    day_1_00?.text = "123123"
                    day_2_00?.text = JSONObject(apiResponse).getJSONArray("list").getJSONObject(1).toString()
//                    day_1_00?.text = "$day $time:00 \n$desc \n $temp°, $speed м/с "
                }
            }
        }
    }
}

