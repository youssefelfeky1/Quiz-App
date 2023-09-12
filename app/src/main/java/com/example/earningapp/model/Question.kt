package com.example.earningapp.model

class Question {
    var question = ""
    var option1 = ""
    var option2 = ""
    var option3 = ""
    var option4 = ""
    var ans = ""

    constructor()
    constructor(
        question: String,
        option1: String,
        option2: String,
        option3: String,
        option4: String,
        ans: String
    ) {
        this.question = question
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.option4 = option4
        this.ans = ans
    }
}