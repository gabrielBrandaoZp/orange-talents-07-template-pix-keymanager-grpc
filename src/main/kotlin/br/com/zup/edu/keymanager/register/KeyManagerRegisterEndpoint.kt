package br.com.zup.edu.keymanager.register

import br.com.zup.edu.KeyManagerRegisterServiceGrpc
import br.com.zup.edu.NewKeyRequest
import br.com.zup.edu.NewKeyResponse
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.validation.ConstraintViolationException

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

        try {
            val createdKey = newPixKeyService.register(newKey)
            responseObserver.onNext(NewKeyResponse.newBuilder()
                .setPixId(createdKey)
                .build())
            responseObserver.onCompleted()
        } catch (e: PixKeyInvalidTypeException) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                .withDescription(e.message)
                .asRuntimeException())
        } catch (e: PixKeyAlreadyExistsException) {
            responseObserver.onError(Status.ALREADY_EXISTS
                .withDescription(e.message)
                .asRuntimeException())
        } catch (e: IllegalStateException) {
            responseObserver.onError(Status.NOT_FOUND
                .withDescription(e.message)
                .asRuntimeException())
        } catch (e: ConstraintViolationException) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription(e.message)
                .asRuntimeException())
        }
    }
}
