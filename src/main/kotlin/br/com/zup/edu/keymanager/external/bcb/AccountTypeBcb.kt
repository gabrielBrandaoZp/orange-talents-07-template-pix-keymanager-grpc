package br.com.zup.edu.keymanager.external.bcb

import br.com.zup.edu.keymanager.register.AccountType

enum class AccountTypeBcb {
    CACC, //CACC -> Conta Corrente
    SVGS; //SVGS -> Conta Poupança

    fun converter(): AccountType {
        return when (this) {
            CACC -> AccountType.CONTA_CORRENTE
            else -> AccountType.CONTA_POUPANCA
        }
    }
}