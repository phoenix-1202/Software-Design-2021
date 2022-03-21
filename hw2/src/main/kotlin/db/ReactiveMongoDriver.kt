package db

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import model.Currency
import model.Entity
import model.Item
import model.User
import org.bson.Document
import rx.Observable
import model.Names.DATABASE_NAME
import model.Names.ID_PARAMETER_NAME
import model.Names.ITEM_COLLECTION_NAME
import model.Names.USER_COLLECTION_NAME

object ReactiveMongoDriver {
    private val client = MongoClients.create("mongodb://localhost:27017")

    private fun <T : Entity> insert(collectionName: String, entity: T): Observable<Success> {
        return client.getDatabase(DATABASE_NAME)
            .getCollection(collectionName)
            .insertOne(entity.getDoc())
    }

    fun register(id: Int, currency: Currency): Observable<String> {
        val user = User(id, currency)
        return insert(USER_COLLECTION_NAME, user).map { "Successfully: $user.$it" }
    }

    fun addNewItem(name: String, price: Double, currency: Currency): Observable<String> {
        val item = Item(name, price, currency)
        return insert(ITEM_COLLECTION_NAME, item).map { "Added new item: $item.$it" }
    }

    private fun getItems(): Observable<Item> {
        return client.getDatabase(DATABASE_NAME)
            .getCollection(ITEM_COLLECTION_NAME)
            .find()
            .toObservable()
            .map { document: Document -> Item(document) }
    }

    private fun getUser(id: Int): Observable<User> {
        return client.getDatabase(DATABASE_NAME)
            .getCollection(USER_COLLECTION_NAME)
            .find(Filters.eq(ID_PARAMETER_NAME, id))
            .toObservable()
            .map { document: Document -> User(document) }
    }


    fun getAllItems(id: Int): Observable<String> {
        return getUser(id).map(User::currency).flatMap { currency ->
            getItems().map { item ->
                String.format("%s: %f%n",
                    item.name,
                    Currency.convert(item.price, item.currency, currency))
            }
        }
    }
}