package com.example.userapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class UserData(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    var name: String = "",
    var age: String = "",
    var city: String = ""
) : RealmObject()
