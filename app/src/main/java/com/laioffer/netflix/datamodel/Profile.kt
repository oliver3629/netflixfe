package com.laioffer.netflix.datamodel

// User profile shown at the top of My Netflix.
data class Profile(
    val id: String,
    val name: String,
    val avatarUrl: String
)