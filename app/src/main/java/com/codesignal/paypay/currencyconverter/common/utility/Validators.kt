package com.codesignal.paypay.currencyconverter.common.utility

class Validators {
    fun validateDecimalInput(s:String):Boolean{
        return if(s.isEmpty()||s.isBlank()){
            false
        }else s != "."
    }
}