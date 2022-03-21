package model

object Names {
    const val ID_PARAMETER_NAME = "id"
    const val CURRENCY_PARAMETER_NAME = "currency"
    const val NAME_PARAMETER_NAME = "name"
    const val PRICE_PARAMETER_NAME = "price"
    const val DATABASE_NAME = "Reactive"
    const val USER_COLLECTION_NAME = "User"
    const val ITEM_COLLECTION_NAME = "Item"
    @JvmStatic
    fun getCurrencyFromString(currencyString: String): Currency {
        return Currency.valueOf(currencyString.uppercase())
    }
}