package br.com.zup.edu.keymanager

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class Pix(

    @field:NotBlank
    @field:Size(max = 77)
    @Column(unique = true, nullable = false)
    val pixId: String,

    @field:NotNull
    @Column(nullable = false)
    val userId: UUID,

    @field:NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val accountType: AccountType,

    @field:NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val pixType: PixType,

    @field:Valid
    @Embedded
    val account: Account,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    val createdIn: LocalDateTime = LocalDateTime.now()
}