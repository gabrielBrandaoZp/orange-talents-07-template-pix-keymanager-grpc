package br.com.zup.edu.keymanager.external.bcb

import br.com.zup.edu.keymanager.register.Account
import br.com.zup.edu.keymanager.register.Pix
import java.util.*

data class PixKeyDetailsResponse(
    val keyType: PixTypeBcb,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: String,
) {

    fun toModel(): Pix {
        return Pix(
            pixId = key,
            userId = UUID.fromString(""),
            accountType = bankAccount.accountType.converter(),
            pixType = keyType.converter(),
            account = Account(
                institution = bankAccount.participant,
                ispb = "",
                agency = bankAccount.branch,
                accountNumber = bankAccount.accountNumber,
                titularName = owner.name,
                cpf = owner.taxIdNumber
            )
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PixKeyDetailsResponse

        if (keyType != other.keyType) return false
        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keyType.hashCode()
        result = 31 * result + key.hashCode()
        return result
    }
}
