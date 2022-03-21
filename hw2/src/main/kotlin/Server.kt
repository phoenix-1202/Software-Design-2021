import db.ReactiveMongoDriver
import model.Currency
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable
import model.Names

object Server {
    private const val PORT = 8080
    private fun getParameter(key: String, parameters: Map<String, List<String>>): String {
        return parameters[key]!![0]
    }

    private fun handle(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>,
    ): Observable<Void> {
        val query = request.decodedPath.substring(1)
        val parameters = request.queryParameters
        val currency: Currency
        val id: Int
        val message: Observable<String>
        try {
            when (query) {
                "register" -> {
                    id = getParameter(Names.ID_PARAMETER_NAME, parameters).toInt()
                    currency = Names.getCurrencyFromString(getParameter(Names.CURRENCY_PARAMETER_NAME, parameters))
                    message = ReactiveMongoDriver.register(id, currency)
                }
                "add-item" -> {
                    val name = getParameter(Names.NAME_PARAMETER_NAME, parameters)
                    val price = getParameter(Names.PRICE_PARAMETER_NAME, parameters).toDouble()
                    currency = Names.getCurrencyFromString(getParameter(Names.CURRENCY_PARAMETER_NAME, parameters))
                    message = ReactiveMongoDriver.addNewItem(name, price, currency)
                }
                "all-items" -> {
                    id = getParameter(Names.ID_PARAMETER_NAME, parameters).toInt()
                    message = ReactiveMongoDriver.getAllItems(id)
                }
                else -> throw IllegalStateException("Unsupported query '$query'")
            }
        } catch (e: RuntimeException) {
            response.status = HttpResponseStatus.BAD_REQUEST
            return response.writeString(Observable.just(e.message))
        }
        response.status = HttpResponseStatus.OK
        return response.writeString(message)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        HttpServer
            .newServer(PORT)
            .start { request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf> ->
                handle(request,
                    response)
            }
            .awaitShutdown()
    }
}