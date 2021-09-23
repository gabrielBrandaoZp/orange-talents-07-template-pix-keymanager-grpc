package br.com.zup.edu.keymanager.external.bcb

data class CreatePixKeyResponse(
    val keyType: PixTypeBcb,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: String,
)