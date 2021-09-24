package br.com.zup.edu.keymanager.search

import br.com.zup.edu.keymanager.external.bcb.BcbClient
import br.com.zup.edu.keymanager.register.Pix
import br.com.zup.edu.keymanager.register.PixRepository
import br.com.zup.edu.keymanager.remove.PixKeyNotFoundException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.util.*
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Validated
@Singleton
class SearchPixService(
    @Inject
    val bcbClient: BcbClient,

    @Inject
    val pixRepository: PixRepository,

    ) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun searchInternal(@Valid newSearchKey: NewSearchKey): Pix {
        logger.info("method=searchInternal, msg=searching pix id: {} for user: {}",
            newSearchKey.pixId,
            newSearchKey.userId)

        val key = pixRepository.findByIdAndUserId(newSearchKey.pixId!!.toLong(), UUID.fromString(newSearchKey.userId))
            .orElseThrow {
                PixKeyNotFoundException("Chave pix não encontrada ou não pertence ao usuário").also {
                    logger.error("method=searchInternal, msg=pix key: {} not found or doest not belong to the current user",
                        newSearchKey.pixId)
                }
            }

        val responseBcb = bcbClient.findByKey(key.pixId)
        if (responseBcb.status != HttpStatus.OK) {
            throw IllegalStateException("Pix key not found in Banco central do Brasil")
        }

        return key
    }

    @Transactional
    fun searchExternal(@NotBlank @Size(max = 77) pixValue: String): Pix {
        logger.info("method=searchInternal, msg=searching pix id: {}", pixValue)

        val key = pixRepository.findByPixId(pixValue).orElseGet {
            val responseBcb = bcbClient.findByKey(pixValue)
            when (responseBcb.status) {
                HttpStatus.OK -> responseBcb.body()!!.toModel()
                else -> throw PixKeyNotFoundException("Chave pix não encontrada ou não pertence ao usuário").also {
                    logger.error("method=searchExternal, msg=pix key: {} not found", pixValue)
                }
            }
        }
        return key
    }
}