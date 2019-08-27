package com.kushkipagos.android

import org.json.JSONObject

class Amount(private val subtotalIva: Double, private val subtotalIva0: Double, private val iva: Double) {

    fun toJsonObject(): JSONObject {
        return JSONObject()
                .put("subtotalIva", subtotalIva)
                .put("subtotalIva0", subtotalIva0)
                .put("iva", iva)
    }
}
