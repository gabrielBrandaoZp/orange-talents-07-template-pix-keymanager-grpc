package br.com.zup.edu.keymanager.remove

import br.com.zup.edu.KeyManagerRemoveServiceGrpc
import br.com.zup.edu.RemoveKeyRequest
import br.com.zup.edu.RemoveKeyResponse
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory


@Singleton
class KeyManagerRemoveEndpoint(
    @Inject
    val removePixKeyService: RemovePixKeyService,

    ) : KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceImplBase() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun removeKey(request: RemoveKeyRequest, responseObserver: StreamObserver<RemoveKeyResponse>) {
        logger.info("method=removeKey, msg=Removing key {}, for user: {}",
            request.pixValue,
            request.userId)

        try {
            removePixKeyService.remove(request.userId, request.pixValue)
            responseObserver.onNext(RemoveKeyResponse.newBuilder()
                .setResult(true)
                .build())
            responseObserver.onCompleted()
            logger.info("method=removeKey, msg=Key removed with sucess")
        } catch (e: PixKeyNotFoundException) {
            logger.error("method=removeKey, msg=Pix not found or does not belong to the user")
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.message).asRuntimeException())
        }

    }
}