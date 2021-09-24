package br.com.zup.edu.keymanager.list

import br.com.zup.edu.KeyManagerListUserPixServiceGrpc
import br.com.zup.edu.ListUserPixRequest
import br.com.zup.edu.ListUserPixResponse
import br.com.zup.edu.PixInfo
import br.com.zup.edu.keymanager.register.PixRepository
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.*


@Singleton
class ListUserPixEndpoint(
    @Inject
    val pixRepository: PixRepository,

    ) : KeyManagerListUserPixServiceGrpc.KeyManagerListUserPixServiceImplBase() {

    override fun listUserPix(request: ListUserPixRequest, responseObserver: StreamObserver<ListUserPixResponse>) {

        if (request.userId.isNullOrBlank()) {
            throw IllegalArgumentException("User id cannot be null or empty")
        }

        val userUUID = UUID.fromString(request.userId)
        val keys = pixRepository.findAllByUserId(userUUID).map {
            ListUserPixResponse.PixDetails.newBuilder()
                .setPixId(it.id!!)
                .setPixInfo(
                    PixInfo.newBuilder()
                        .setPixType(it.pixType.name)
                        .setPixValue(it.pixId)
                        .setCreatedAt(it.createdIn.toString())
                        .build())
                .setAccountType(it.accountType.name)
                .build()
        }

        with(responseObserver) {
            onNext(ListUserPixResponse.newBuilder()
                .setUserId(request.userId)
                .addAllPixDetails(keys)
                .build())
            onCompleted()
        }
    }
}