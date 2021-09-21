package br.com.zup.edu.factory

import br.com.zup.edu.NewKeyRequest

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

    }

}