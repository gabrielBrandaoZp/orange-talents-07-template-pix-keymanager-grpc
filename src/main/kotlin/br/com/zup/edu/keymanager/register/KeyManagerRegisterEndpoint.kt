package br.com.zup.edu.keymanager.register

import br.com.zup.edu.KeyManagerRegisterServiceGrpc
import br.com.zup.edu.NewKeyRequest
import br.com.zup.edu.NewKeyResponse
import br.com.zup.edu.shared.handlers.ErrorHandler
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.validation.ConstraintViolationException

@ErrorHandler
@Singleton
class KeyManagerRegisterEndpoint(
    @Inject
    val newPixKeyService: NewPixKeyService,

    ) : KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun registerKey(request: NewKeyRequest, responseObserver: StreamObserver<NewKeyResponse>) {
        logger.info("method=registerKey, msg=Registring key {}, for user: {} of account type: {}",
            request.pixValue,
            request.userId,
            request.accountType)

        val newKey = request.toModel()
        val createdKey = newPixKeyService.register(newKey)

        with(responseObserver) {
            onNext(NewKeyResponse.newBuilder()
                .setPixId(createdKey)
                .build())
            onCompleted()
        }
    }
}
