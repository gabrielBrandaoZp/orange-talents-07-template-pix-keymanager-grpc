package br.com.zup.edu.keymanager.register

import br.com.zup.edu.keymanager.external.bcb.*
import br.com.zup.edu.keymanager.external.itau.ErpItauClient
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.transaction.Transactional
import javax.validation.Valid


@Validated
@Singleton
class NewPixKeyService(
    @Inject
    val erpItauClient: ErpItauClient,

    @Inject
    val bcbClient: BcbClient,

    @Inject
    val pixRepository: PixRepository,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    fun register(@Valid newPixKey: NewPixKey): String {
        logger.info("methos=register, msg=creating pix for user: {}, for account type: {}",
            newPixKey.userId,
            newPixKey.accountType)

        if (pixRepository.existsByPixId(newPixKey.pixId)) {
            throw PixKeyAlreadyExistsException("Chave pix já cadastrada: ${newPixKey.pixId}")
        }

        val responseItauClient = erpItauClient.searchUserAccountById(newPixKey.userId!!, newPixKey.accountType!!.name)
        val account = responseItauClient.body()?.toModel() ?: throw IllegalStateException("Usuário não encontrado")

        val pix = newPixKey.toModel(account)

        val bcbRequest = createPixKeyRequestForBcb(pix = pix, account = account)
        val responseBcb = bcbClient.registerPixBcb(bcbRequest)

        if (responseBcb.status != HttpStatus.CREATED) {
            throw IllegalStateException("Error trying to registry pix key in Banco Central do Brasil")
        }

        pix.update(responseBcb.body()!!.key)
        pixRepository.save(pix)

        logger.info("methos=register, msg=pix key: {}, created with sucess", pix.pixId)
        return pix.pixId
    }

    private fun createPixKeyRequestForBcb(pix: Pix, account: Account): CreatePixKeyRequest {
        return CreatePixKeyRequest(
            keyType = pix.pixType.converter(),
            key = pix.pixId,
            bankAccount = BankAccount(
                participant = account.ispb,
                branch = account.agency,
                accountNumber = account.accountNumber,
                accountType = pix.accountType.converter()
            ),
            owner = Owner(
                type = OwnerType.NATURAL_PERSON,
                name = account.titularName,
                taxIdNumber = account.cpf
            )
        )
    }
}