package com.example.gyanlike.Model

public class MCQListModel {

    var id: String = ""
    var chId: String = ""
    var question: String = ""
    var a: String = ""
    var b: String = ""
    var c: String = ""
    var d: String = ""
    var ans: String = ""

    constructor()

    constructor(
        id: String,
        chId: String,
        question: String,
        a: String,
        b: String,
        c: String,
        d: String,
        ans: String
    ) {
        this.id = id
        this.chId = chId
        this.question = question
        this.a = a
        this.b = b
        this.c = c
        this.d = d
        this.ans = ans
    }
}