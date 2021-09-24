package br.com.zup.edu.shared

import br.com.zup.edu.NewKeyRequest
import br.com.zup.edu.SearchKeyRequest
import br.com.zup.edu.keymanager.register.AccountType
import br.com.zup.edu.keymanager.register.NewPixKey
import br.com.zup.edu.keymanager.register.PixType
import br.com.zup.edu.keymanager.search.NewSearchKey

fun NewKeyRequest.toModel(): NewPixKey {
    return NewPixKey(
        pixId = this.pixValue,
        userId = this.userId,
        accountType = when (accountType) {
            NewKeyRequest.AccountType.UNKNOWN_ACCOUNT_TYPE -> null
            else -> AccountType.valueOf(this.accountType.name)
        },
        pixType = when (pixType) {
            NewKeyRequest.PixType.UNKNOWN_KEY_TYPE -> null
            else -> PixType.valueOf(this.pixType.name)
        }
    )
}

fun SearchKeyRequest.toModel(): NewSearchKey {
    return NewSearchKey(
        userId = this.userId,
        pixId = this.pixId
    )
}