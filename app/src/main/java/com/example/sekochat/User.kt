package com.example.sekochat

class User {

    var name: String? = null
    var email: String? = null
    var image: String? = null
    var uid: String? = null

    constructor(){}

    constructor(name: String?, email: String?,image: String?, uid: String?){
        this.name = name
        this.email = email
        this.image = image
        this.uid = uid
    }
}