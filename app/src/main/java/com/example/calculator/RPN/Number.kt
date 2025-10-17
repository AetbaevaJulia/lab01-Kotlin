package com.example.calculator.RPN
class Number : Token {
    val value : Double

    constructor(_value : Double){
        value = _value
    }

    override fun toString(): String {
        return value.toString()
    }
}