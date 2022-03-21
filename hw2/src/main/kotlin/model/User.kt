package model

import org.bson.Document
import model.Names.CURRENCY_PARAMETER_NAME
import model.Names.ID_PARAMETER_NAME
import model.Names.getCurrencyFromString

class User(private val id: Int, val currency: Currency) : Entity {

    constructor(document: Document) : this(document.getInteger(ID_PARAMETER_NAME),
        getCurrencyFromString(CURRENCY_PARAMETER_NAME)) {
    }

    override fun getDoc(): Document {
        return Document().append("id", id).append("currency", currency.toString())
    }

    override fun toString(): String {
        return "User{" +
                "id=" + id +
                ", currency=" + currency +
                '}'
    }
}