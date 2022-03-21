package model

enum class Currency(private val rate: Double) {
    RUB(1.0), USD(113.2), EUR(123.2);

    companion object {
        @JvmStatic
        fun convert(amount: Double, from: Currency, to: Currency): Double {
            return amount * from.rate / to.rate
        }
    }
}