package ru.mrkotik.weather

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL
import java.util.*
import android.content.Intent


class MainActivity : AppCompatActivity() {
    private var city_field: EditText? = null
    private var main_btn: Button? = null
    private var detail: Button? = null
    private var result_info: TextView? = null
    private var videoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        city_field = findViewById(R.id.city_field)
        main_btn = findViewById(R.id.main_btn)
        detail = findViewById(R.id.detail)
        result_info = findViewById(R.id.result_info)
        detail?.setVisibility(View.GONE) // Скрыть кнопку детального прогноза погоды


    }
    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        videoView = findViewById<View>(R.id.videoView) as VideoView
        videoView!!.setOnPreparedListener { mp: MediaPlayer -> mp.isLooping = true }
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.cloud)
        videoView!!.setVideoURI(uri)
        videoView!!.start()
        main_btn?.setOnClickListener {
            result_info?.alpha = 1f
            if (city_field?.text?.toString()?.trim()?.equals("")!!)
                Toast.makeText(this, "Введите город", Toast.LENGTH_SHORT).show()
            else {
                val city: String = city_field?.text.toString()
                val key: String = "de1f3667886b5164d0616ae406eadda3"
                val url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"

                doAsync {
                    val apiResponse = URL(url).readText()

                    val weather = JSONObject(apiResponse).getJSONArray("weather")
                    val desc = weather.getJSONObject(0).getString("description")
                    val id = weather.getJSONObject(0).getString("id")

                    val main = JSONObject(apiResponse).getJSONObject("main")
                    val temp = main.getString("temp")
                    val feels_like = main.getString("feels_like")
                    val humidity = main.getString("humidity")

                    val wind = JSONObject(apiResponse).getJSONObject("wind")
                    val speed = wind.getString("speed")
                    val gust = wind.getString("gust")
                    val name = JSONObject(apiResponse).getString("name")

                    synchronized(this) {
                        runOnUiThread(Runnable {
                            result_info?.text = "В городе $name сейчас $desc" +
                                    "\n\nТемпература воздуха $temp°,\nОщущается как: $feels_like°" +
                                    "\n\nВлажность: $humidity%\n\nСкорость ветра: $speed м/с, порывами до $gust м/с."

                            detail?.setVisibility(View.VISIBLE)

                            videoView!!.setOnPreparedListener { mp: MediaPlayer ->
                                mp.isLooping = true
                                mp.setVolume(0f, 0f)
                            }
                            val uri = when (id) {
                                "200", "201", "202", "210", "211", "212", "221", "230", "231", "232" -> Uri.parse(
                                    "android.resource://" + packageName + "/" + R.raw.thunderstorm
                                )
                                "300", "301", "302", "310", "311", "312", "313", "314", "321" -> Uri.parse(
                                    "android.resource://" + packageName + "/" + R.raw.drizzle
                                )
                                "500", "501", "502", "503", "504", "511", "520", "521", "522", "531" -> Uri.parse(
                                    "android.resource://" + packageName + "/" + R.raw.rain
                                )
                                "600", "601", "602", "611", "612", "613", "615", "616", "620", "621", "622" -> Uri.parse(
                                    "android.resource://" + packageName + "/" + R.raw.snow_loght
                                )
                                "701", "711", "721", "731", "741", "751", "761", "762", "771", "781" -> Uri.parse(
                                    "android.resource://" + packageName + "/" + R.raw.fog
                                )
                                "800" -> Uri.parse("android.resource://" + packageName + "/" + R.raw.sunny)
                                "801", "802" -> Uri.parse("android.resource://" + packageName + "/" + R.raw.cloudy)
                                "803", "804" -> Uri.parse("android.resource://" + packageName + "/" + R.raw.cloud)
                                else -> {
                                    Uri.parse("android.resource://" + packageName + "/" + R.raw.cloud)
                                }
                            }
                            videoView!!.setVideoURI(uri)
                            videoView!!.start()

                        })

                    }


                }

            }

        }
//        var detail = Button(this)
//        detail.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        detail.text = "Прогноз на 5 дней"
        detail?.setOnClickListener{
            videoView!!.stopPlayback()
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("city", city_field?.text.toString()) // Передаем данные о городе в следующий activity
            intent.putExtra("key", "de1f3667886b5164d0616ae406eadda3" )
            startActivity(intent)
        }
    }

}
