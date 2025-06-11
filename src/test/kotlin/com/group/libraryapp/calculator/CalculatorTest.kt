package com.group.libraryapp.calculator

fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest1()
    calculatorTest.divideTest2()
}

class CalculatorTest {
    fun addTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.add(3)

        // then
        // number 검증을 위한 처리 (Calculator.kt)
        // 1. data class 사용
        /*
        val expectedCalculator = Calculator(8)
        if(calculator != expectedCalculator) {
            throw IllegalStateException()
        }
        */
        // 2. number 필드를 private => public 으로 변경
        //  - public 변경으로 인한 setter 방지를 위해 backing property 사용 (public getter 구현)
        if(calculator.number != 8) {
            throw IllegalStateException()
        }
    }

    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        if(calculator.number != 2) {
            throw IllegalStateException()
        }
    }

    fun multiplyTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiply(3)

        // then
        if(calculator.number != 15) {
            throw IllegalStateException()
        }
    }

    fun divideTest1() {
        // given
        val calculator = Calculator(9)

        // when
        // 1. 분모가 0인 case
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            if(e.message != "0으로 나눌 수 없습니다.") {
                throw IllegalStateException("메시지가 다릅니다.")
            }
            // 테스트 성공
            return
        } catch (e: Exception) {
            throw IllegalStateException()
        }

        // then
        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
    }

    fun divideTest2() {
        // given
        val calculator = Calculator(9)

        // when
        // 2. 분모가 0이 아닌 case
        calculator.divide(3)

        // then
        if(calculator.number != 3) {
            throw IllegalStateException()
        }
    }
}