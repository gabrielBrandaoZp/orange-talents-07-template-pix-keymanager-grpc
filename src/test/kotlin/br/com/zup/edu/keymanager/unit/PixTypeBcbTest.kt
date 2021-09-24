package br.com.zup.edu.keymanager.unit

import br.com.zup.edu.keymanager.external.bcb.PixTypeBcb
import br.com.zup.edu.keymanager.register.PixType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
internal class PixTypeBcbTest {


    @Nested
    inner class CPF {

        @Test
        internal fun `should return a CPF pix type`() {
            val bcbPixType = PixTypeBcb.CPF

            assertNotNull(bcbPixType.converter())
            assertEquals(PixType.CPF, bcbPixType.converter())
        }
    }

    @Nested
    inner class EMAIL {

        @Test
        internal fun `should return a EMAIL pix type`() {
            val bcbPixType = PixTypeBcb.EMAIL

            assertNotNull(bcbPixType.converter())
            assertEquals(PixType.EMAIL, bcbPixType.converter())
        }
    }

    @Nested
    inner class PHONE {

        @Test
        internal fun `should return a TELEFONE pix type`() {
            val bcbPixType = PixTypeBcb.PHONE

            assertNotNull(bcbPixType.converter())
            assertEquals(PixType.TELEFONE, bcbPixType.converter())
        }
    }

    @Nested
    inner class RANDOM {

        @Test
        internal fun `should return a CHAVE_ALEATORIA pix type`() {
            val bcbPixType = PixTypeBcb.RANDOM

            assertNotNull(bcbPixType.converter())
            assertEquals(PixType.CHAVE_ALEATORIA, bcbPixType.converter())
        }
    }

}