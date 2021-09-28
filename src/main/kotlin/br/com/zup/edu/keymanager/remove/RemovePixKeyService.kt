package br.com.zup.edu.keymanager.remove

import br.com.zup.edu.keymanager.external.bcb.BcbClient
import br.com.zup.edu.keymanager.external.bcb.DeletePixKeyRequest
import br.com.zup.edu.keymanager.register.PixRepository
import br.com.zup.edu.shared.validation.ValidUUID
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.*
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank

@Validated
@Singleton
class RemovePixKeyService(
    @Inject
    val bcbClient: BcbClient,

    @Inject
    val pixRepository: PixRepository,
) {

    @Transactional
    fun remove(
        @NotBlank @ValidUUID(message = "Não é um formato válido de id") userId: String,
        @NotBlank pixId: String,
    ) {
        val userUUID = UUID.fromString(userId)

        val key = pixRepository.findByPixIdAndUserId(pixId, userUUID).orElseThrow {
            PixKeyNotFoundException("Chave pix não encontrada ou não pertence ao usuário")
        }

        pixRepository.delete(key)

        val response = bcbClient.removePixBcb(
            key = pixId, request = DeletePixKeyRequest(key = pixId, participant = key.account.ispb)
        )

        if (response.status != HttpStatus.OK) {
            throw IllegalStateException("Error trying to remove pix key in Banco Central do Brasil")
        }
    }
}