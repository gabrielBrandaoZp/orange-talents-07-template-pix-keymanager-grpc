package br.com.zup.edu.keymanager.register

import br.com.zup.edu.keymanager.external.bcb.AccountTypeBcb

enum class AccountType {
    CONTA_POUPANCA,
    CONTA_CORRENTE;

    fun converter(): AccountTypeBcb {
        return when(this) {
            CONTA_POUPANCA -> AccountTypeBcb.SVGS
            else -> AccountTypeBcb.CACC
        }
    }
}
