package br.com.zup.edu.keymanager.external

import br.com.zup.edu.keymanager.Account

data class SearchUserResponse(
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse,
) {

    fun toModel(): Account {
        return Account(
            institution = this.instituicao.nome,
            titularName = this.titular.nome,
            cpf = this.titular.cpf,
            agency = this.agencia,
            accountNumber = this.numero
        )
    }
}

data class InstituicaoResponse(
    val nome: String,
    val ispb: String,
)

data class TitularResponse(
    val id: String,
    val nome: String,
    val cpf: String,
)