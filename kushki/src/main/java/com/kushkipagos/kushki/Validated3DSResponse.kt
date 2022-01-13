package com.kushkipagos.kushki

data class Validated3DSResponse(
    val validated: Boolean,
    val message: String,
    val sandbox: Boolean=false,
)