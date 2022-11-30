package com.test.snackmyeon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity(), Ramyeon, View.OnClickListener {

    // 매분, 사용자가 원할때마다 상태를 알려주기 위한 값
    var status: String = ""

    // Timer를 위한 변수들
    var time: Int = 0   // 사용자에게 보여지는 시간
    var timerTask: Timer? = null    // Timer 객체

    // 대파를 선택했는지에 대한 여부
    var welshOnion: Boolean = false
    // 계란을 선택했는지에 대한 여부
    var egg: Boolean = false
    // 김치를 선택했는지에 대한 여부
    var kimchi: Boolean = false

    // Ramyeon interface를 통한 변수 선언
    // 물 넣었는지에 대한 여부
    override var water: Boolean = false
    // 면 넣었는지에 대한 여부
    override var myeon: Boolean = false
    // 스프 넣었는지에 대한 여부
    override var soup: Boolean = false

    // Nullable하게 만들어 값 입력을 onCreate에서 한다.
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

    // Activity의 생명주기중 하나로 view 관련된 코드를 입력.
    // Activity가 생성될때 제일 먼저 실행된다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // xml에서 위에 선언한 변수에 값을 할당한다 (==binding 이라고도 함)
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

        // View.OnClickListener를 활용하기 위해 listener에 등록한다.
        orderBt!!.setOnClickListener(this)
        resetBt!!.setOnClickListener(this)
        toastBt!!.setOnClickListener(this)


//        View.OnClickListener를 사용하지 않으면
//        onCreate()내 코드가 길어지고 가독성이 떨어진다.
//        orderBt!!.setOnClickListener { View.OnClickListener {
//            이런식으로 하나하나 listener를 써줘야한다;
//            어후 귀찮아라
//        } }


    }

    // View.OnClickListener Interface를 구현하면 강제 오버라이딩 해야한다.
    // 여기에서 활용하기위해 setOnClickListener를 통해 view를 등록한다.
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

    // 라면 조리가 끝난 후 재 조리를 위해 초기화 하는 메소드
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

    // 특정 버튼들을 선택가능 or 선택불가 상태로 만들기 위한 메소드.
    private fun controlEnabled(flag: Boolean){
        resetBt!!.isEnabled = flag
        orderBt!!.isEnabled = flag
        welshOnionCb!!.isEnabled = flag
        eggCb!!.isEnabled = flag
        kimchiCb!!.isEnabled = flag
    }

    // 실시간 상태를 알게해주는 메소드
    private fun callToast(){
        Toast.makeText(this,status,Toast.LENGTH_SHORT).show()
    }

    // Timer를 시작하는 메소드
    // 이미 진행중인 Timer가 있다면 중복 실행 방지를 위해
    // 기존 Timer를 삭제해준다.
    private fun startTimer(){
        if (timerTask != null){
            stopTimer()
        }
        // 시간 값 초기화
        time = 0
        // period를 통해 시간 단위를 선택할 수 있다.
        // 빠른 결과를 보기 위해 1000 밀리 세컨에서 500으로 조정.
        timerTask = kotlin.concurrent.timer(period = 500){
            time++

            // 안드로이드는 싱글 쓰레드 체제. 오직 메인 쓰레드(UI 쓰레드)만이 뷰의 값을 바꿀 수 있는 권한을 갖고 있어
            // 지금 작업을 수행하는 쓰레드가 메인 쓰레드라면 즉시 작업을 시작하고
            // 메인 쓰레드가 아니라면 쓰레드 이벤트 큐에 쌓아두는 기능을 하는 게 runOnUiThread야!!

            // 아래 내용처럼 타이머를 화면에 보여준다거나 텍스트가 바뀐다거나 하는게 다 UI가 바뀌는 거야
            runOnUiThread{
                timerTx!!.text =time.toString()+"초"
                if (water && time < 60){
                    status = "물 끓이는 중"
                }
                if (time == 60){            // 매 분마다 상태를 알려주는 부분
                    status = "물 끓이는 중"
                    callToast()
                } else if (time == 120){    // 매 분마다 상태를 알려주는 부분
                    status = "물 끓기 시작함"
                    setSoup()
                    setMyeon()
                    callToast()
                } else if (time == 180){    // 매 분마다 상태를 알려주는 부분
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
                } else if (time == 240){    // 매 분마다 상태를 알려주는 부분
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

    // 타이머가 null인지 먼저 확인하고
    // null이 아니라면 타이머를 취소하고
    // 취소한 timer task들을 모두 삭제한다
    private fun stopTimer(){
        if (timerTask != null){
            timerTask!!.cancel()
            timerTask!!.purge()
        }
    }

    // Ramyeon interface를 통한 강제 오버라이딩
    override fun setWater() {
        water = true
    }

    // Ramyeon interface를 통한 강제 오버라이딩
    override fun setMyeon() {
        myeon = true
        status += " / "+"면 추가"
    }

    // Ramyeon interface를 통한 강제 오버라이딩
    override fun setSoup() {
        soup = true
        status += " / "+"분말스프 추가"
    }



}