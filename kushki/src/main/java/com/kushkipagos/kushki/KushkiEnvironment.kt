package com.kushkipagos.kushki

enum class KushkiEnvironment(override val url: String) : Environment {
    TESTING("https://api-uat.kushkipagos.com/"),
    QA("https://api-qa.kushkipagos.blue/"),
    CI("https://api-ci.kushkipagos.com/"),
    STAGING("https://api-stg.kushkipagos.com/"),
    PRODUCTION("https://api.kushkipagos.com/"),
    UAT_REGIONAL("https://regional-uat.kushkipagos.com/"),
    PRODUCTION_REGIONAL("https://regional.kushkipagos.com/");

}
