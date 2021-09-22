package br.com.zup.edu.keymanager.register

import br.com.zup.edu.keymanager.external.ErpItauClient
import io.micronaut.http.HttpStatus
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.transaction.Transactional


@Singleton
class NewPixKeyService(
    @Inject
    val erpItauClient: ErpItauClient,

    @Inject
    val pixRepository: PixRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    fun register(newPixKey: NewPixKey): String {
        logger.info("methos=register, msg=creating pix for user: {}, for account type: {}",
            newPixKey.userId,
            newPixKey.accountType)

        val keyValidation = PixType.valueOf(newPixKey.pixType!!.name).validation(newPixKey.pixId)
        if (!keyValidation) {
            throw PixKeyInvalidTypeException("Tipo inválido de chave pix: ${newPixKey.pixType}")
        }

        if (pixRepository.existsByPixId(newPixKey.pixId)) {
            throw PixKeyAlreadyExistsException("Chave pix já cadastrada: ${newPixKey.pixId}")
        }

        val response = erpItauClient.searchUserAccountById(newPixKey.userId!!, newPixKey.accountType!!.name)
        val account = response.body()?.toModel() ?: throw IllegalStateException("Usuário não encontrado")

        val pix = newPixKey.toModel(account)
        pixRepository.save(pix)

        return pix.pixId
    }
}