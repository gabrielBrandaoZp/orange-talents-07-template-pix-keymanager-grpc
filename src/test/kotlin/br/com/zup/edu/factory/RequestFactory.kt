package br.com.zup.edu.factory

import br.com.zup.edu.NewKeyRequest
import br.com.zup.edu.keymanager.external.bcb.BankAccount
import br.com.zup.edu.keymanager.external.bcb.CreatePixKeyRequest
import br.com.zup.edu.keymanager.external.bcb.Owner
import br.com.zup.edu.keymanager.external.bcb.OwnerType
import br.com.zup.edu.keymanager.register.AccountType
import br.com.zup.edu.keymanager.register.PixType

class RequestFactory {

    companion object {
        private const val userId: String = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        private val pixType: NewKeyRequest.PixType = NewKeyRequest.PixType.EMAIL
        private const val pixValue: String = "ponte@email.com"
        private val accountType: NewKeyRequest.AccountType = NewKeyRequest.AccountType.CONTA_CORRENTE

        fun createNewKeyRequest(
            userId: String = this.userId,
            pixType: NewKeyRequest.PixType = this.pixType,
            pixValue: String = this.pixValue,
            accountType: NewKeyRequest.AccountType = this.accountType,
        ): NewKeyRequest {
            return NewKeyRequest.newBuilder()
                .setUserId(userId)
                .setPixType(pixType)
                .setPixValue(pixValue)
                .setAccountType(accountType)
                .build()
        }

        fun createPixKeyBcbRequest(
            pixType: PixType = PixType.EMAIL,
            pixValue: String = this.pixValue,
            accountType: AccountType = AccountType.CONTA_CORRENTE,
            name: String = "Rafael Ponte",
            taxIdNumber: String = "12345678909",
        ): CreatePixKeyRequest {
            return CreatePixKeyRequest(
                keyType = pixType.converter(),
                key = pixValue,
                bankAccount = BankAccount(
                    participant = "60701190",
                    branch = "0001",
                    accountNumber = "291900",
                    accountType = accountType.converter()
                ),
                owner = Owner(
                    type = OwnerType.NATURAL_PERSON,
                    name = name,
                    taxIdNumber = taxIdNumber
                )
            )
        }

    }

}