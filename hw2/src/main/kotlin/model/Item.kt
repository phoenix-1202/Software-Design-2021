package model

import org.bson.Document
import model.Names.CURRENCY_PARAMETER_NAME
import model.Names.NAME_PARAMETER_NAME
import model.Names.PRICE_PARAMETER_NAME
import model.Names.getCurrencyFromString

class Item(val name: String, val price: Double, val currency: Currency) : Entity {

    constructor(document: Document) : this(
        document.getString(NAME_PARAMETER_NAME),
        document.getDouble(PRICE_PARAMETER_NAME),
        getCurrencyFromString(document.getString(CURRENCY_PARAMETER_NAME))
    )

    override fun getDoc(): Document {
        return Document().append("name", name).append("price", price).append("currency", currency.toString())
    }

    override fun toString(): String {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", currency=" + currency.toString() +
                '}'
    }
}