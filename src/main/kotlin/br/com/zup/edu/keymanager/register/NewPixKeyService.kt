package br.com.zup.edu.keymanager.register

import br.com.zup.edu.keymanager.external.ErpItauClient
import io.micronaut.http.HttpStatus
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.transaction.Transactional


@Singleton
class NewPixKeyService(
    @Inject
    val erpItauClient: ErpItauClient,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun userExistsWithAccountType(id: String, tipo: String): Boolean {
        logger.info("methos=userExistsWithAccountType, msg=searching user: {}, for account type: {}", id, tipo)
        val response = erpItauClient.searchUserAccountById(id, tipo)
        return response.status == HttpStatus.OK
    }

    @Transactional
    fun createPix(newPixKey: NewPixKey): Pix {
        logger.info("methos=createPix, msg=creating pix for user: {}, for account type: {}",
            newPixKey.userId,
            newPixKey.accountType)

        val response = erpItauClient.searchUserAccountById(newPixKey.userId!!, newPixKey.accountType!!.name)
        val account = response.body()!!.toModel()

        return newPixKey.toModel(account)
    }

    @Transactional
    fun userExist(id: String): Boolean {
        logger.info("methos=userExist, msg=searching client: {}", id)
        val response = erpItauClient.searchUserById(id)
        return response.status == HttpStatus.OK
    }
}