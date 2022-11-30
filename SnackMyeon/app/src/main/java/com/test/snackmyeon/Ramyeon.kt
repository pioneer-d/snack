package com.test.snackmyeon


// interface의 특징인 강제 오버라이딩을 통해
// 필수적으로 필요한 라면 재료를 강제 구현하도록 유도.
interface Ramyeon {

    var water: Boolean
    var myeon: Boolean
    var soup: Boolean

    fun setWater()
    fun setMyeon()
    fun setSoup()

}