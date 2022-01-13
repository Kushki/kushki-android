package com.kushkipagos.tests

import com.kushkipagos.kushki.Environment

internal enum class TestEnvironment(override val url: String) : Environment {
    LOCAL("https://api-uat.kushkipagos.com/"),
    LOCAL_QA("https://api-qa.kushkipagos.com/"),
    LOCAL_CI("https://api-ci.kushkipagos.com/"),
    INVALID("This causes a MalformedURLException");
}
