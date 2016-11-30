package com.kushkipagos.android

internal enum class TestEnvironment(override val url: String) : Environment {
    LOCAL("http://localhost:8888/kushki/api/v1"),
    INVALID("This causes a MalformedURLException");
}
