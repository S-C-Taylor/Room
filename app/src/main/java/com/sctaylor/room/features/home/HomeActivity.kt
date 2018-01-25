package com.sctaylor.room.features.home

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.madrapps.pikolo.HSLColorPicker
import com.madrapps.pikolo.listeners.OnColorSelectionListener
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener
import com.sctaylor.room.R
import com.sctaylor.room.application.RoomApplication
import com.sctaylor.room.application.network.RoomService
import com.sctaylor.room.dagger.components.DaggerHomeActivityComponent
import com.sctaylor.room.dagger.components.HomeActivityComponent
import com.sctaylor.room.dagger.modules.HomeActivityModule
import kotlinx.android.synthetic.main.activity_home.*
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.CountDownTimer
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.sctaylor.room.model.DHT
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var roomService: RoomService

    var currentColour: Int = Color.BLACK

    lateinit var compositeDisposible: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val component = DaggerHomeActivityComponent.builder()
                .homeActivityModule(HomeActivityModule(this))
                .roomApplicationComponent(RoomApplication.get(this).getComponent())
                .build() as HomeActivityComponent

        compositeDisposible = CompositeDisposable()

        component.injectHomeActivity(this)

        colourPallete.setBackgroundColor(currentColour)

        textViewHumidity.text = "-- %"
        textViewTemp.text = "-- °C"

        roomService.dht
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dht ->
                    updateDht(dht)
                    Timber.tag("DHT").i("Success DHT!")
                }, {
                    t ->
                    Timber.tag("DHT").e("Failure DHT: " + t.message)
                })

        colourPallete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showColourDialog()
            }
        })

        buttonMessageSend.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (editTextMessage.text != null) {
                    sendMessage(editTextMessage.text.toString())
                    editTextMessage.setText("")
                    editTextMessage.clearFocus()

                    // Then just use the following:
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(homeMainLayout.getWindowToken(), 0)
                }
            }
        })

        buttonReset.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                roomService.doReset()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Timber.tag("Reset").i("Success Reset")
                            Toast.makeText(applicationContext, "Reset Successful", Toast.LENGTH_SHORT).show()
                            reset()
                        }, {
                            t ->
                            Timber.tag("Reset").e("Failure LCD: " + t.message)
                            Toast.makeText(applicationContext, "Reset failed, try again", Toast.LENGTH_SHORT).show()
                        })
            }
        })

    }

    private fun reset(){
        colourPallete.setBackgroundColor(Color.BLACK)
        editTextMessage.setText("")
        textViewHumidity.text = "-- %"
        textViewTemp.text = "-- °C"
    }

    fun sendMessage(message: String) {

        roomService.setMessage(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.tag("LCD").i("Success LCD")
                    Toast.makeText(applicationContext, "Message sent successfully", Toast.LENGTH_SHORT).show()
                }, { t ->
                    Timber.tag("LCD").e("Failure LCD: " + t.message)
                    Toast.makeText(applicationContext, "Message failed to send", Toast.LENGTH_SHORT).show()
                })
    }

    fun showColourDialog() {
        ChromaDialog.Builder()
                .initialColor(currentColour)
                .colorMode(ColorMode.RGB)
                .onColorSelected(object : ColorSelectListener {
                    override fun onColorSelected(color: Int) {

                        val red = Color.red(color)
                        val green = Color.green(color)
                        val blue = Color.blue(color)

                        currentColour = color
                        colourPallete.setBackgroundColor(currentColour)

                        Timber.tag("RGB Values").i("Red: " + red +
                                " Green: " + green +
                                " Blue:" + blue)

                        roomService.setRGBLed(red * 4, green * 4, blue * 4)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    Timber.tag("RGB").i("Success RGB")
                                }, { t ->
                                    Timber.tag("RGB").e("Failure RGB: " + t.message)
                                })
                    }
                })
                .create()
                .show(supportFragmentManager, "ChromaDialog")
    }

    private fun updateDht(dht: DHT) {
        if(dht.temperature.toInt() != -1) {
            textViewTemp.text = dht.temperature.toString().plus(" °C")
        }

        if(dht.humidity.toInt() != -1) {
            textViewHumidity.text = dht.humidity.toString().plus(" %")
        }
    }

    override fun onStart() {
        super.onStart()

        compositeDisposible.add(roomService.dht
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .retry()
                .repeat()
                .subscribe({ v ->
                    updateDht(v)
                    Timber.tag("DHT").i("Success DHT!")
                }, { t ->
                    Timber.tag("DHT").e("Failure DHT: " + t.message)
                }))


    }

    override fun onStop() {
        super.onStop()
        compositeDisposible.clear()
    }
}
