package br.com.zup.edu.keymanager.register

import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Account(

    @field:NotBlank
    val institution: String,

    @field:NotBlank
    val ispb: String,

    @field:NotBlank
    val agency: String,

    @field:NotBlank
    val accountNumber: String,

    @field:NotBlank
    val titularName: String,

    @field:NotBlank
    val cpf: String
)
