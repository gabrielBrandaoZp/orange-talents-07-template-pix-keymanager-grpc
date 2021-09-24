package br.com.zup.edu.keymanager.search

import br.com.zup.edu.*
import br.com.zup.edu.keymanager.register.Pix
import br.com.zup.edu.shared.handlers.ErrorHandler
import br.com.zup.edu.shared.toModel
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton

@ErrorHandler
@Singleton
class SearchPixEndpoint(
    @Inject
    val searchPixService: SearchPixService,

    ) : KeyManagerSearchPixServiceGrpc.KeyManagerSearchPixServiceImplBase() {

    override fun searchKeyInternal(request: SearchKeyRequest, responseObserver: StreamObserver<SearchKeyResponse>) {

        val searchRequest = request.toModel()
        val pix = searchPixService.searchInternal(searchRequest)

        with(responseObserver) {
            onNext(createSearchKeyInternalResponse(pix))
            onCompleted()
        }
    }

    override fun searchKeyExternal(
        request: SearchKeyExternalRequest, responseObserver: StreamObserver<SearchKeyExternalResponse>,
    ) {

        val pix = searchPixService.searchExternal(request.pixValue);
        with(responseObserver) {
            onNext(createSearchKeyExternalResponse(pix))
            onCompleted()
        }
    }

    private fun createSearchKeyExternalResponse(pix: Pix): SearchKeyExternalResponse {
        return SearchKeyExternalResponse.newBuilder()
            .setPixInfo(
                PixInfo.newBuilder()
                    .setPixType(pix.pixType.name)
                    .setPixValue(pix.pixId)
                    .setCreatedAt(pix.createdIn.toString())
                    .build())
            .setUserInfo(
                UserInfo.newBuilder()
                    .setName(pix.account.titularName)
                    .setCpf(pix.account.cpf)
                    .build())
            .setAccountInfo(
                AccountInfo.newBuilder()
                    .setInstitution(pix.account.institution)
                    .setAgency(pix.account.agency)
                    .setNumber(pix.account.accountNumber)
                    .setAccountType(pix.accountType.name)
                    .build())
            .build()
    }

    private fun createSearchKeyInternalResponse(pix: Pix): SearchKeyResponse {
        return SearchKeyResponse.newBuilder()
            .setUserId(pix.userId.toString())
            .setPixIdInternal(pix.id!!)
            .setPixInfo(
                PixInfo.newBuilder()
                    .setPixType(pix.pixType.name)
                    .setPixValue(pix.pixId)
                    .setCreatedAt(pix.createdIn.toString())
                    .build())
            .setUserInfo(
                UserInfo.newBuilder()
                    .setName(pix.account.titularName)
                    .setCpf(pix.account.cpf)
                    .build())
            .setAccountInfo(
                AccountInfo.newBuilder()
                    .setInstitution(pix.account.institution)
                    .setAgency(pix.account.agency)
                    .setNumber(pix.account.accountNumber)
                    .setAccountType(pix.accountType.name)
                    .build())
            .build()
    }
}