package br.com.zup.edu.keymanager.unit

import br.com.zup.edu.keymanager.external.bcb.AccountTypeBcb
import br.com.zup.edu.keymanager.register.AccountType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
internal class AccountTypeBcbTest {


    @Nested
    inner class CACC {

        @Test
        internal fun `should return a CONTA_CORRENT type`() {
            val bcbType = AccountTypeBcb.CACC

            assertNotNull(bcbType.converter())
            assertEquals(AccountType.CONTA_CORRENTE, bcbType.converter())
        }
    }

    @Nested
    inner class SVGS {

        @Test
        internal fun `should return a CONTA_POUPANCA type`() {
            val bcbType = AccountTypeBcb.SVGS

            assertNotNull(bcbType.converter())
            assertEquals(AccountType.CONTA_POUPANCA, bcbType.converter())
        }
    }

}