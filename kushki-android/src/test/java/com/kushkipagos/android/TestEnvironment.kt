package com.kushkipagos.android

internal enum class TestEnvironment(override val url: String) : Environment {
    LOCAL("https://api-uat.kushkipagos.com/v1/"),
    INVALID("This causes a MalformedURLException");
}
