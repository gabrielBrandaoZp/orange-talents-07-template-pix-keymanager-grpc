package br.com.zup.edu.keymanager.external.bcb

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client(value = "\${bcb.client.host}")
interface BcbClient {

    @Post(value = "\${bcb.client.endpoint.key}",
        produces = [MediaType.APPLICATION_XML],
        consumes = [MediaType.APPLICATION_XML])
    fun registerPixBcb(@Body request: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete(value = "\${bcb.client.endpoint.key}/{key}",
        produces = [MediaType.APPLICATION_XML],
        consumes = [MediaType.APPLICATION_XML])
    fun removePixBcb(
        @PathVariable("key") key: String,
        @Body request: DeletePixKeyRequest,
    ): HttpResponse<DeletedPixKeyResponse>

    @Get(value = "\${bcb.client.endpoint.key}/{key}",
        consumes = [MediaType.APPLICATION_XML])
    fun findByKey(@PathVariable("key") key: String): HttpResponse<PixKeyDetailsResponse>
}