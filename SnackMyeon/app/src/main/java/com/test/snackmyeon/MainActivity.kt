package com.test.snackmyeon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity(), Ramyeon, View.OnClickListener {

    var status: String = ""
    var time: Int = 0
    var timerTask: Timer? = null

    var welshOnion: Boolean = false
    var egg: Boolean = false
    var kimchi: Boolean = false

    override var water: Boolean = false
    override var myeon: Boolean = false
    override var soup: Boolean = false

    var orderBt: Button? = null
    var resetBt: Button? = null
    var toastBt: Button? = null
    var welshOnionCb: CheckBox? = null
    var eggCb: CheckBox? = null
    var kimchiCb: CheckBox? = null
    var timerTx: TextView? = null
    var welsh_onion_iv: ImageView? = null
    var egg_iv: ImageView? = null
    var cookImg: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        orderBt = findViewById(R.id.order_bt)
        resetBt = findViewById(R.id.reset_bt)
        toastBt = findViewById(R.id.toast_bt)

        welshOnionCb = findViewById(R.id.welshOnion_bt)
        eggCb = findViewById(R.id.egg_bt)
        kimchiCb = findViewById(R.id.kimchi_bt)
        timerTx = findViewById(R.id.timer_tx)
        welsh_onion_iv = findViewById(R.id.welsh_onion_iv)
        egg_iv = findViewById(R.id.egg_iv)
        cookImg = findViewById(R.id.cook_img)

        orderBt!!.setOnClickListener(this)
        resetBt!!.setOnClickListener(this)
        toastBt!!.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.order_bt -> {
                startTimer()
                cookImg!!.setImageResource(R.drawable.cooking)
                setWater()

                if (welshOnionCb!!.isChecked){
                    welshOnion = Ingredient.setWelshOnion()
                }
                if (eggCb!!.isChecked){
                    egg = Ingredient.setEgg()
                }
                if (kimchiCb!!.isChecked){
                    kimchi = Ingredient.setKimChi()
                }

                controlEnabled(false)

            }
            R.id.reset_bt -> reset()
            R.id.toast_bt -> callToast()
        }
    }

    private fun reset(){
        water = false
        soup = false
        myeon = false

        welshOnion = false
        egg = false
        kimchi = false

        controlEnabled(true)

        stopTimer()
        time = 0

        status = ""

    }

    private fun controlEnabled(flag: Boolean){
        resetBt!!.isEnabled = flag
        orderBt!!.isEnabled = flag
        welshOnionCb!!.isEnabled = flag
        eggCb!!.isEnabled = flag
        kimchiCb!!.isEnabled = flag
    }


    private fun callToast(){
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show()
    }

    private fun startTimer(){
        if (timerTask != null){
            stopTimer()
        }
        time = 0
        timerTask = kotlin.concurrent.timer(period = 500){
            time++
            runOnUiThread{
                timerTx!!.text =time.toString()+"초"
                if (water && time < 60){
                    status = "물 끓이는 중"
                }
                if (time == 60){
                    status = "물 끓이는 중"
                    callToast()
                } else if (time == 120){
                    status = "물 끓기 시작함"
                    setSoup()
                    setMyeon()
                    callToast()
                } else if (time == 180){
                    status = "면이 익는 중"
                    callToast()
                } else if (time == 210){
                    if (egg){
                        status += " / 계란 추가"
                        egg_iv!!.visibility = View.VISIBLE
                    }
                    if (welshOnion){
                        status += " / 대파 추가"
                        welsh_onion_iv!!.visibility = View.VISIBLE
                    }
                } else if (time == 240){
                    stopTimer()
                    status = "요리 완료"
                    if (kimchi){
                        status += " / 김치 곁들임"
                    }
                    welsh_onion_iv!!.visibility = View.INVISIBLE
                    egg_iv!!.visibility = View.INVISIBLE
                    cookImg!!.setImageResource(R.drawable.ramen)
                    callToast()

                    resetBt!!.isEnabled = true
                }
            }
        }
    }


    private fun stopTimer(){
        if (timerTask != null){
            timerTask!!.cancel()
            timerTask!!.purge()
        }
    }

    override fun setWater() {
        water = true
    }

    override fun setMyeon() {
        myeon = true
        status += " / "+"면 추가"
    }

    override fun setSoup() {
        soup = true
        status += " / "+"분말스프 추가"
    }



}