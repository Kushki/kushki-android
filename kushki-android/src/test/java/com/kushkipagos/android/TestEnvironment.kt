package com.kushkipagos.android

internal enum class TestEnvironment(override val url: String) : Environment {
    LOCAL("https://api-uat.kushkipagos.com/"),
    LOCAL_QA("https://api-qa.kushkipagos.com/"),
    INVALID("This causes a MalformedURLException");
}
