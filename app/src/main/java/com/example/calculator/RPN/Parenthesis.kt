package com.example.calculator.RPN

class Parenthesis : Token {
    val symbol : Char
    val isClose : Boolean

    constructor(_symbol : Char){
        symbol = _symbol
        isClose = isClosed()
    }

    private fun isClosed(): Boolean {
        if (symbol == ')') {
            return true
        }
        return false
    }

    override fun toString(): String {
        return symbol.toString()
    }
}