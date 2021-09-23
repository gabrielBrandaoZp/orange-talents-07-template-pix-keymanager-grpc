package br.com.zup.edu.keymanager.external.bcb

data class CreatePixKeyRequest(
    val keyType: PixTypeBcb,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
)

data class BankAccount(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountTypeBcb,
)

data class Owner(
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
)
