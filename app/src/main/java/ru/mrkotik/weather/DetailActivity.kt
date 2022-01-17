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
    private var day_1_03: TextView? = null
    private var day_1_09: TextView? = null
    private var day_1_15: TextView? = null
    private var day_1_21: TextView? = null
    private var day_2_03: TextView? = null
    private var day_2_09: TextView? = null
    private var day_2_15: TextView? = null
    private var day_2_21: TextView? = null
    private var day_3_03: TextView? = null
    private var day_3_09: TextView? = null
    private var day_3_15: TextView? = null
    private var day_3_21: TextView? = null
    private var day_4_03: TextView? = null
    private var day_4_09: TextView? = null
    private var day_4_15: TextView? = null
    private var day_4_21: TextView? = null
    private var day_5_03: TextView? = null
    private var day_5_09: TextView? = null
    private var day_5_15: TextView? = null
    private var day_5_21: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        day_1_03 = findViewById(R.id.day_1_03)
        day_1_09 = findViewById(R.id.day_1_09)
        day_1_15 = findViewById(R.id.day_1_15)
        day_1_21 = findViewById(R.id.day_1_21)
        day_2_03 = findViewById(R.id.day_2_03)
        day_2_09 = findViewById(R.id.day_2_09)
        day_2_15 = findViewById(R.id.day_2_15)
        day_2_21 = findViewById(R.id.day_2_21)
        day_3_03 = findViewById(R.id.day_3_03)
        day_3_09 = findViewById(R.id.day_3_09)
        day_3_15 = findViewById(R.id.day_3_15)
        day_3_21 = findViewById(R.id.day_3_21)
        day_4_03 = findViewById(R.id.day_4_03)
        day_4_09 = findViewById(R.id.day_4_09)
        day_4_15 = findViewById(R.id.day_4_15)
        day_4_21 = findViewById(R.id.day_4_21)
        day_5_03 = findViewById(R.id.day_5_03)
        day_5_09 = findViewById(R.id.day_5_09)
        day_5_15 = findViewById(R.id.day_5_15)
        day_5_21 = findViewById(R.id.day_5_21)




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




                    day_1_03?.text = formatted_list.get(0)
                    day_1_09?.text = formatted_list.get(1)
                    day_1_15?.text = formatted_list.get(2)
                    day_1_21?.text = formatted_list.get(3)
                    day_2_03?.text = formatted_list.get(4)
                    day_2_09?.text = formatted_list.get(5)
                    day_2_15?.text = formatted_list.get(6)
                    day_2_21?.text = formatted_list.get(7)
                    day_3_03?.text = formatted_list.get(8)
                    day_3_09?.text = formatted_list.get(9)
                    day_3_15?.text = formatted_list.get(10)
                    day_3_21?.text = formatted_list.get(11)
                    day_4_03?.text = formatted_list.get(12)
                    day_4_09?.text = formatted_list.get(13)
                    day_4_15?.text = formatted_list.get(14)
                    day_4_21?.text = formatted_list.get(15)
                    day_5_03?.text = formatted_list.get(16)
                    day_5_09?.text = formatted_list.get(17)
                    day_5_15?.text = formatted_list.get(18)
                    day_5_21?.text = formatted_list.get(19)

                }
            }
        }
    }
}

