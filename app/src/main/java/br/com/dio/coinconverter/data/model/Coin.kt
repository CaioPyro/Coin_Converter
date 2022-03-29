package br.com.dio.coinconverter.data.model

import java.util.*

enum class Coin(val locale: Locale) {
    USD(Locale.US),
    EUR(Locale.UK),
    CNY(Locale.CHINA),
    GBP(Locale.UK),
    CAD(Locale.CANADA),
    BRL(Locale("pt", "BR")),
    ARS(Locale("es", "AR")),
    JPY(Locale.JAPAN)
    ;

    companion object {
        fun getByName(name: String) = values().find { it.name == name } ?: BRL
    }
}