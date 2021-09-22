package br.com.zup.edu.keymanager.remove

import br.com.zup.edu.keymanager.register.PixRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.util.*
import javax.transaction.Transactional

@Singleton
class RemovePixKeyService(
    @Inject val pixRepository: PixRepository,
) {

    @Transactional
    fun remove(userId: String, pixId: String) {
        val userUUID = UUID.fromString(userId)

        val key = pixRepository.findByPixIdAndUserId(pixId, userUUID).orElseThrow {
            PixKeyNotFoundException("Chave pix não encontrada ou não pertence ao usuário")
        }

        pixRepository.delete(key)
    }
}