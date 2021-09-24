package br.com.zup.edu.keymanager.external.bcb

import br.com.zup.edu.keymanager.register.PixType

enum class PixTypeBcb {
    CPF,
    CNPJ,
    PHONE,
    EMAIL,
    RANDOM;

    fun converter(): PixType {
        return when (this) {
            CPF -> PixType.CPF
            EMAIL -> PixType.EMAIL
            PHONE -> PixType.TELEFONE
            else -> PixType.CHAVE_ALEATORIA
        }
    }
}
