package br.com.zup.edu.keymanager.search

import br.com.zup.edu.shared.validation.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class NewSearchKey(

    @field:ValidUUID
    @field:NotBlank
    val userId: String?,

    @field:NotNull
    val pixId: Long?,
)
