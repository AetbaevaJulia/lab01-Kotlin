package com.example.calculator.RPN
open class Token { }

class RPN {
    var tokens : MutableList<Token>
    var rpnListTokens : MutableList<Token>
    var answer : Double

    constructor(expression : String){
        tokens = toListTokens(expression)
        rpnListTokens = toRPN()
        answer = calculate()
    }

    private fun toListTokens(expression: String) : MutableList<Token> {
        var tokens = mutableListOf<Token>()
        var buffer = ""

        for (i in 0..(expression.length - 1)){
            if (expression[i].isDigit() || expression[i] == ','){
                buffer += expression[i]
            }
            else {
                if(buffer.isNotEmpty()){
                    tokens.add(Number(buffer.toDoubleOrNull() ?: 0.0))
                    buffer = ""
                }
                when(expression[i]){
                    '(', ')' -> tokens.add(Parenthesis(expression[i]))
                    '+', '-', '/', '*' -> tokens.add(Operation(expression[i]))
                    else -> println("Предупреждение: неизвестный токен ${expression[i]}")
                }
            }

            if (i == expression.length - 1 && buffer != ""){
                tokens.add(Number(buffer.toDoubleOrNull() ?: 0.0))
                buffer = ""
            }
        }

        return tokens
    }

    private fun toRPN() : MutableList<Token> {
        var rpn = mutableListOf<Token>()
        var operation = mutableListOf<Token>()
        for (token in tokens){
            if (token is Number){
                rpn.add(token)
                continue
            }

            if (token is Parenthesis){
                if (!token.isClose){
                    operation.add(token)
                    continue
                }
                else {
                    while(operation.isNotEmpty() && operation.last() !is Parenthesis){
                        rpn.add(operation.last())
                        operation.removeAt(operation.count() - 1)
                    }
                    operation.removeAt(operation.count() - 1)
                }
            }

            else if (token is Operation){
                if (operation.isEmpty()){
                    operation.add(token)
                    continue
                }
                else {
                    if (operation.last() is Operation && token.priority > (operation.last() as Operation).priority){
                        operation.add(token)
                    }
                    else {
                        while(operation.isNotEmpty() &&
                            operation.last() is Operation &&
                            token.priority <= (operation.last() as Operation).priority) {
                            rpn.add(operation.last())
                            operation.removeAt(operation.count() - 1)
                        }
                        operation.add(token)
                    }
                }
            }
        }

        while (operation.isNotEmpty()) {
            rpn.add(operation.removeAt(operation.count() - 1))
        }

        println(rpn.joinToString())

        return rpn
    }

    private fun executeOperaion(num1 : Number, num2 : Number, operation: Operation) : Double{
        when (operation.symbol){
            '+' -> return num1.value + num2.value
            '-' -> return num1.value - num2.value
            '*' -> return num1.value * num2.value
        }
        if (num2.value != 0.0) {
            return num1.value / num2.value
        }
        throw ArithmeticException("Деление на ноль или что-то не так ~(>_<。)＼")
    }

    private fun calculate() : Double {
        var index : Int = 0
        if (rpnListTokens.count() < 2){
            throw IllegalArgumentException("Мало аргументов, переделывай ~(>_<。)＼")
        }
        while (rpnListTokens.count() != 1){
            if (index > rpnListTokens.count()){
                index = 0
            }
            if (rpnListTokens[index] is Operation) {
                val num1 : Number = rpnListTokens[index - 2] as Number
                val num2 : Number = rpnListTokens[index - 1] as Number
                val operation : Operation = rpnListTokens[index] as Operation
                rpnListTokens[index - 2] = Number(executeOperaion(num1, num2, operation))
                rpnListTokens.removeAt(index)
                rpnListTokens.removeAt(index - 1)
                index = 0
            }
            index++
        }
        return (rpnListTokens[0] as Number).value
    }
}