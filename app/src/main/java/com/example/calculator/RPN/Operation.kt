package com.example.calculator.RPN
class Operation: Token {
    val symbol : Char
    val priority : Int

    constructor(_symbol : Char){
        symbol = _symbol
        priority = getPriorityOperation()
    }

    private fun getPriorityOperation() : Int {
        if (symbol == '+' || symbol == '-'){
            return 1
        }
        return 2
    }

    override fun toString(): String {
        return symbol.toString()
    }
}