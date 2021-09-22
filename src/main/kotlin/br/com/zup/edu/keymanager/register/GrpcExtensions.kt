package br.com.zup.edu.keymanager.register

import br.com.zup.edu.NewKeyRequest

fun NewKeyRequest.toModel(): NewPixKey {
    return NewPixKey(
        pixId = this.pixValue,
        userId = this.userId,
        accountType = when (accountType) {
            NewKeyRequest.AccountType.UNKNOWN_ACCOUNT_TYPE -> null
            else -> AccountType.valueOf(this.accountType.name)
        },
        pixType = when(pixType) {
            NewKeyRequest.PixType.UNKNOWN_KEY_TYPE -> null
            else -> PixType.valueOf(this.pixType.name)
        }
    )
}