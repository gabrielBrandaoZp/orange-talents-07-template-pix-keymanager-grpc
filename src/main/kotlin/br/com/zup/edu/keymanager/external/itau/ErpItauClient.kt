package br.com.zup.edu.keymanager.external.itau

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client(value = "\${erp.itau.client.host}")
interface ErpItauClient {

    @Get("\${erp.itau.client.endpoint.clientes}/{clienteId}/contas")
    fun searchUserAccountById(
        @PathVariable clienteId: String,
        @QueryValue tipo: String,
    ): HttpResponse<SearchUserAccountResponse>
}