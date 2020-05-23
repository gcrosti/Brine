package com.gdc.recipebook

import com.google.firebase.iid.FirebaseInstanceId

class Meal (
    var name: String,
    val instanceId: String = FirebaseInstanceId.getInstance().id,
    var function: String = "",
    var notes: String = "",
    var imageURI: String = "")