package com.fias.ddrhighspeed.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform