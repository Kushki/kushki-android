package com.kushkipagos.android

enum class KushkiEnvironment(override val url: String) : Environment {
    TESTING("https://uat.aurusinc.com/kushki/api/v1"),
    STAGING("https://staging.aurusinc.com/kushki/api/v1"),
    PRODUCTION("https://p1.kushkipagos.com/kushki/api/v1");
}
