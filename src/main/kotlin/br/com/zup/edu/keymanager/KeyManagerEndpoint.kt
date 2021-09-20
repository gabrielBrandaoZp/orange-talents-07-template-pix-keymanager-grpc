package br.com.zup.edu.keymanager

import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.NewKeyRequest
import br.com.zup.edu.NewKeyResponse
import br.com.zup.edu.keymanager.external.NewPixKeyService
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.validation.ConstraintViolationException

@Singleton
class KeyManagerEndpoint(
    @Inject
    val pixRepository: PixRepository,

    @Inject
    val newPixKeyService: NewPixKeyService,

    ) : KeyManagerServiceGrpc.KeyManagerServiceImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun registerKey(request: NewKeyRequest, responseObserver: StreamObserver<NewKeyResponse>) {
        logger.info("method=registerKey, msg=Registring key {}, for user: {} of account type: {}",
            request.pixValue,
            request.userId,
            request.accountType)

        val userExists = newPixKeyService.userExist(request.userId, request.accountType.toString())
        if (!userExists) {
            responseObserver.onError(Status.NOT_FOUND
                .withDescription("Usuário não encontrado")
                .asRuntimeException())
            return
        }

        val newKey = request.toModel()
        val createdKey = newPixKeyService.createPix(newKey)

        val pixType = request.pixType.name
        val pixValue = request.pixValue

        val keyValidation = PixType.valueOf(pixType).validation(pixValue)
        if (!keyValidation) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                .withDescription("Tipo inválido de chave pix: $pixType")
                .asRuntimeException())
            return
        }

        if (pixRepository.existsByPixId(pixValue)) {
            responseObserver.onError(Status.ALREADY_EXISTS
                .withDescription("Chave pix já cadastrada: $pixValue")
                .asRuntimeException())
            return
        }

        try {
            pixRepository.save(createdKey)
        } catch (e: ConstraintViolationException) {
            logger.error("method=registerKey, msg=Error trying to save pix entity")

            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription(e.message)
                .asRuntimeException())
            return
        }

        with(responseObserver) {
            onNext(NewKeyResponse.newBuilder()
                .setPixId(createdKey.pixId)
                .build())
            onCompleted()
        }
    }
}
