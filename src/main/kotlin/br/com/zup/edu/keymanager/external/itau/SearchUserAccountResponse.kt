package br.com.zup.edu.keymanager.external.itau

import br.com.zup.edu.keymanager.register.Account

data class SearchUserAccountResponse(
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse,
) {

    fun toModel(): Account {
        return Account(
            institution = this.instituicao.nome,
            ispb = this.instituicao.ispb,
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