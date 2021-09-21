package br.com.zup.edu.keymanager.unit

import br.com.zup.edu.keymanager.PixType
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
internal class PixTypeTest {

    @Nested
    inner class CPF {

        @Test
        internal fun `should be valid when pix key CPF has pix value is valid`() {
            with(PixType.CPF) {
                assertTrue(validation("12345678909"))
            }
        }

        @Test
        internal fun `should not be valid when pix key CPF has pix value invalid`() {
            with(PixType.CPF) {
                assertFalse(validation("1234567890"))
            }
        }

        @Test
        internal fun `should not be valid when pix key CPF has pix value empty or null`() {
            with(PixType.CPF) {
                assertFalse(validation(""))
                assertFalse(validation(null))
            }
        }
    }

    @Nested
    inner class EMAIL {

        @Test
        internal fun `should be valid when pix key EMAIL has pix value is valid`() {
            with(PixType.EMAIL) {
                assertTrue(validation("ponte@email.com"))
            }
        }

        @Test
        internal fun `should not be valid when pix key EMAIL has pix value invalid`() {
            with(PixType.EMAIL) {
                assertFalse(validation("ponteemail.com"))
            }
        }

        @Test
        internal fun `should not be valid when pix key CPF has pix value empty or null`() {
            with(PixType.EMAIL) {
                assertFalse(validation(""))
                assertFalse(validation(null))
            }
        }
    }

    @Nested
    inner class TELEFONE {

        @Test
        internal fun `should be valid when pix key TELEFONE has pix value is valid`() {
            with(PixType.TELEFONE) {
                assertTrue(validation("+558340028922"))
            }
        }

        @Test
        internal fun `should not be valid when pix key TELEFONE has pix value invalid`() {
            with(PixType.TELEFONE) {
                assertFalse(validation("32330202"))
            }
        }

        @Test
        internal fun `should not be valid when pix key TELEFONE has pix value empty or null`() {
            with(PixType.TELEFONE) {
                assertFalse(validation(""))
                assertFalse(validation(null))
            }
        }
    }

    @Nested
    inner class CHAVEALEATORIA {

        @Test
        internal fun `should be valid when pix key CHAVE_ALEATORIA has pix value is valid`() {
            with(PixType.CHAVE_ALEATORIA) {
                assertTrue(validation(""))
                assertTrue(validation(null))
            }
        }

        @Test
        internal fun `should not be valid when pix key CHAVE_ALEATORIA has pix value invalid`() {
            with(PixType.CHAVE_ALEATORIA) {
                assertFalse(validation("some text here"))
            }
        }
    }
}