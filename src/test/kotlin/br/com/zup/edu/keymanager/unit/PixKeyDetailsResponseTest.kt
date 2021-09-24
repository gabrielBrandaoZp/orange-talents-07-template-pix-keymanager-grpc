package br.com.zup.edu.keymanager.unit

import br.com.zup.edu.keymanager.external.bcb.*
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

@MicronautTest(transactional = false)
internal class PixKeyDetailsResponseTest {


    @Test
    internal fun `should return true when the objects has the pix keytype and pix value`() {
        val pixDetails = createPixDetailsResponse(keyType = PixTypeBcb.EMAIL, key = "ponte@email.com")
        val pixDetailsClone = pixDetails.copy()

        assertTrue(pixDetails.equals(pixDetailsClone))
    }

    @Test
    internal fun `should return false when the objects has different pix keytype and pix value`() {
        val pixDetails = createPixDetailsResponse(keyType = PixTypeBcb.EMAIL, key = "ponte@email.com")
        val pixDetailsClone = pixDetails.copy(key = UUID.randomUUID().toString())

        assertFalse(pixDetails.equals(pixDetailsClone))
    }

    private fun createPixDetailsResponse(keyType: PixTypeBcb, key: String): PixKeyDetailsResponse {
        return PixKeyDetailsResponse(
            keyType = keyType,
            key = key,
            bankAccount = BankAccount(
                participant = "Itau",
                branch = "0001",
                accountNumber = "12345",
                accountType = AccountTypeBcb.CACC,
            ),
            owner = Owner(
                type = OwnerType.NATURAL_PERSON,
                name = "Rafael Ponte",
                taxIdNumber = "12345678909"
            ),
            createdAt = LocalDateTime.now().toString()
        )
    }
}