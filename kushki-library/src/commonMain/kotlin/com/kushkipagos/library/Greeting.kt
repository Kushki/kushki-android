package com.kushkipagos.library

import com.kushkipagos.library.Platform


class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
