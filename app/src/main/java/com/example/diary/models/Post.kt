package com.example.diary.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Post(
    var id: String = "",
    var author : String = "",
    var title : String = "",
    var content : String = "",
    var timestamp : Long = 0,
    var background : Int = 0,
    var History : Post? = null)



