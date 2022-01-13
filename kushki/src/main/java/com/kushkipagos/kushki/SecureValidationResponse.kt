package com.kushkipagos.kushki

enum class SecureValidationResponse(val code: String) {
    CODE_OTP("OTP000"),
    CODE_3DS("ok"),
    MESSAGE_3DS("3DS000")
}