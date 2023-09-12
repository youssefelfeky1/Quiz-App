package com.example.earningapp.model

class User {
     var name = ""
     var age = ""
     var email = ""
     var password = ""

    constructor()
    constructor(name: String, age: String, email: String, password: String) {
        this.name = name
        this.age = age
        this.email = email
        this.password = password
    }
}