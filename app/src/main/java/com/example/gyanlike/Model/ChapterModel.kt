package com.example.gyanlike.Model

class ChapterModel {
    var chapterName : String = ""
    var id : String = ""

    constructor()

    constructor(chapterName : String , id : String)
    {
        this.chapterName = chapterName
        this.id = id
    }
}