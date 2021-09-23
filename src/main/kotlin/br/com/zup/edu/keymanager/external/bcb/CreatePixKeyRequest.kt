package br.com.zup.edu.keymanager.external.bcb

data class CreatePixKeyRequest(
    val keyType: PixTypeBcb,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreatePixKeyRequest

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}

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
