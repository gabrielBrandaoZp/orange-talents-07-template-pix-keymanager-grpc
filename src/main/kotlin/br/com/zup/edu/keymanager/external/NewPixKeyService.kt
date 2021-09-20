package br.com.zup.edu.keymanager.external

import br.com.zup.edu.keymanager.NewPixKey
import br.com.zup.edu.keymanager.Pix
import io.micronaut.http.HttpStatus
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import javax.transaction.Transactional


@Singleton
open class NewPixKeyService(
    @Inject
    val erpItauClient: ErpItauClient,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    open fun userExist(id: String, tipo: String): Boolean {
        logger.info("methos=searchUser, msg=searching user: {}, for acoount type: {}", id, tipo)
        val response = erpItauClient.searchUserById(id, tipo)
        return response.status == HttpStatus.OK
    }

    @Transactional
    open fun createPix(newPixKey: NewPixKey): Pix {
        logger.info("methos=createPix, msg=creating pix for user: {}, for account type: {}",
            newPixKey.userId,
            newPixKey.accountType)

        val response = erpItauClient.searchUserById(newPixKey.userId!!, newPixKey.accountType!!.name)
        val account = response.body()!!.toModel()

        return newPixKey.toModel(account)
    }
}