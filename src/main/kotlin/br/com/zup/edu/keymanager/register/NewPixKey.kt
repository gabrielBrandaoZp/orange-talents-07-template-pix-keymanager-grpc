package br.com.zup.edu.keymanager.register

import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Introspected
data class NewPixKey(

    @field:NotBlank
    val userId: String?,

    @field:NotNull
    val pixType: PixType?,

    @field:Size(max = 77)
    val pixId: String?,

    @field:NotNull
    val accountType: AccountType?,
) {

    fun toModel(account: Account): Pix {
        return Pix(
            pixId = if (this.pixType == PixType.CHAVE_ALEATORIA) UUID.randomUUID().toString() else this.pixId!!,
            userId = UUID.fromString(this.userId),
            pixType = PixType.valueOf(this.pixType!!.name),
            accountType = AccountType.valueOf(accountType!!.name),
            account = account
        )
    }
}