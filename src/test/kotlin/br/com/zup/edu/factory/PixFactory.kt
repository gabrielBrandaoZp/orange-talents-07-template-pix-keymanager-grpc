package br.com.zup.edu.factory

import br.com.zup.edu.keymanager.register.Account
import br.com.zup.edu.keymanager.register.AccountType
import br.com.zup.edu.keymanager.register.Pix
import br.com.zup.edu.keymanager.register.PixType
import java.util.*

class PixFactory {

    companion object {
        private const val userId: String = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        private val pixType: PixType = PixType.EMAIL
        private const val pixValue: String = "ponte@email.com"
        private val accountType: AccountType = AccountType.CONTA_CORRENTE

        private val account = Account(
            institution = "Itau",
            agency = "1234",
            accountNumber = "56654",
            titularName = "Rafael Ponte",
            cpf = "12345678901"
        )

        fun createPix(
            pixId: String = this.pixValue,
            userId: String = this.userId,
            accountType: AccountType = this.accountType,
            pixType: PixType = this.pixType,
            account: Account = this.account,
        ): Pix {
            return Pix(
                pixId = pixId,
                userId = UUID.fromString(userId),
                accountType = accountType,
                pixType = pixType,
                account = account
            )
        }
    }
}