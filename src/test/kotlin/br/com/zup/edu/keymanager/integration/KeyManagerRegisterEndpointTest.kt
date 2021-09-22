package br.com.zup.edu.keymanager.integration

import br.com.zup.edu.KeyManagerRegisterServiceGrpc
import br.com.zup.edu.NewKeyRequest
import br.com.zup.edu.factory.PixFactory
import br.com.zup.edu.factory.RequestFactory
import br.com.zup.edu.keymanager.register.PixRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@MicronautTest(transactional = false)
internal class KeyManagerRegisterEndpointTest(
    @Inject
    val grpcClient: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub,

    @Inject
    val pixRepository: PixRepository,
) {

    private lateinit var validPixRequest: NewKeyRequest

    @BeforeEach
    internal fun setUp() {
        pixRepository.deleteAll()

        validPixRequest = RequestFactory.createNewKeyRequest()
    }

    @Test
    internal fun `should create a pix key EMAIL for CONTE_CORRENTE when data is valid`() {
        val response = grpcClient.registerKey(validPixRequest)

        with(response) {
            assertNotNull(pixId)
            assertTrue(pixRepository.existsByPixId(pixId))
        }
    }

    @Test
    internal fun `should create a pix key TELEFONE for CONTE_CORRENTE when data is valid`() {
        val request = RequestFactory.createNewKeyRequest(
            pixType = NewKeyRequest.PixType.TELEFONE,
            pixValue = "+5583999411430"
        )

        val response = grpcClient.registerKey(request)

        with(response) {
            assertNotNull(pixId)
            assertTrue(pixRepository.existsByPixId(pixId))
        }
    }

    @Test
    internal fun `should create a pix key CPF for CONTE_CORRENTE when data is valid`() {
        val request = RequestFactory.createNewKeyRequest(
            pixType = NewKeyRequest.PixType.CPF,
            pixValue = "12345678909"
        )

        val response = grpcClient.registerKey(request)

        with(response) {
            assertNotNull(pixId)
            assertTrue(pixRepository.existsByPixId(pixId))
        }
    }

    @Test
    internal fun `should create a pix key CHAVE_ALEATORIA for CONTA_CORRENTE when data is valid`() {
        val request = RequestFactory.createNewKeyRequest(
            pixType = NewKeyRequest.PixType.CHAVE_ALEATORIA,
            pixValue = ""
        )

        val response = grpcClient.registerKey(request)

        with(response) {
            assertNotNull(pixId)
            assertTrue(pixRepository.existsByPixId(pixId))
        }
    }

    @Test
    internal fun `should not create a pix key EMAIL when user not found`() {
        val request = RequestFactory.createNewKeyRequest(
            userId = UUID.randomUUID().toString()
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Usuário não encontrado", status.description)
        }
    }

    @Test
    internal fun `should not create a pix key EMAIL when pix value is empty`() {
        val invalidRequest = RequestFactory.createNewKeyRequest(
            pixValue = ""
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(invalidRequest)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Tipo inválido de chave pix: ${invalidRequest.pixType}", status.description)
        }
    }

    @Test
    internal fun `should not create a pix key EMAIL when pix value is invalid`() {
        val invalidRequest = RequestFactory.createNewKeyRequest(
            pixValue = "ponteemail.com"
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(invalidRequest)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Tipo inválido de chave pix: ${invalidRequest.pixType}", status.description)
        }
    }

    @Test
    internal fun `should not create a pix key TELEFONE when pix value is invalid`() {
        val invalidRequest = RequestFactory.createNewKeyRequest(
            pixType = NewKeyRequest.PixType.TELEFONE,
            pixValue = "40028922"
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(invalidRequest)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Tipo inválido de chave pix: ${invalidRequest.pixType}", status.description)
        }
    }

    @Test
    internal fun `should not create a pix key TELEFONE when pix value is empty`() {
        val invalidRequest = RequestFactory.createNewKeyRequest(
            pixType = NewKeyRequest.PixType.TELEFONE,
            pixValue = ""
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(invalidRequest)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Tipo inválido de chave pix: ${invalidRequest.pixType}", status.description)
        }
    }

    @Test
    internal fun `shoud not create a pix key CPF when pix value is invalid`() {
        val invalidRequest = RequestFactory.createNewKeyRequest(
            pixType = NewKeyRequest.PixType.CPF,
            pixValue = "123456789"
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(invalidRequest)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Tipo inválido de chave pix: ${invalidRequest.pixType}", status.description)
        }
    }

    @Test
    internal fun `shoud not create a pix key CPF when pix value is empty`() {
        val invalidRequest = RequestFactory.createNewKeyRequest(
            pixType = NewKeyRequest.PixType.CPF,
            pixValue = ""
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(invalidRequest)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Tipo inválido de chave pix: ${invalidRequest.pixType}", status.description)
        }
    }

    @Test
    internal fun `should not create a pix key EMAIL when key already exists`() {
        val existingPix = PixFactory.createPix()

        pixRepository.save(existingPix)

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(validPixRequest)
        }

        with(error) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave pix já cadastrada: ${existingPix.pixId}", status.description)
        }
    }

    @Test
    internal fun `should not create a pix key EMAIL when pix value size is bigger than 77`() {
        val request = RequestFactory.createNewKeyRequest(
            pixValue = "ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte." +
                    "ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte." +
                    "ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.ponte.@email.com"
        )

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(request)
        }

        assertEquals(Status.INVALID_ARGUMENT.code, error.status.code)
    }

    @Factory
    class Clients {

        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub {
            return KeyManagerRegisterServiceGrpc.newBlockingStub(channel)
        }
    }
}