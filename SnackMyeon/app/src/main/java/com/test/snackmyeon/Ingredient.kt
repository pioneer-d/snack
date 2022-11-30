package com.test.snackmyeon

// 여기에 object로 선언하면 싱글톤 패턴으로 사용할 수 있어
// 객체를 여러개 생성해서 사용해야하는 상황에서 많이 사용해요!
// 이거를 싱글톤 패턴으로 사용한 이유는
// 파, 김치, 계란등은 공용 재료라는 생각에 여러곳에서 사용해야 할 수 있다!
// 그러므로 객체생성을 줄이고 자원 낭비를 방지하기 위해서 싱글톤으로 구현.
object Ingredient{

    fun setKimChi(): Boolean{
        return true
    }
    fun setWelshOnion(): Boolean{
        return true
    }
    fun setEgg(): Boolean{
        return true
    }
}