package br.com.zup.edu.keymanager.integration

import br.com.zup.edu.KeyManagerRegisterServiceGrpc
import br.com.zup.edu.NewKeyRequest
import br.com.zup.edu.factory.PixFactory
import br.com.zup.edu.factory.RequestFactory
import br.com.zup.edu.keymanager.external.bcb.*
import br.com.zup.edu.keymanager.register.PixRepository
import br.com.zup.edu.keymanager.register.PixType
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

@MicronautTest(transactional = false)
internal class KeyManagerRegisterEndpointTest(
    @Inject
    val grpcClient: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub,

    @Inject
    val bcbClient: BcbClient,

    @Inject
    val pixRepository: PixRepository,
) {

    lateinit var validPixRequest: NewKeyRequest
    lateinit var validCreatePixKeyBcbRequest: CreatePixKeyRequest
    lateinit var responseBcb: CreatePixKeyResponse

    @BeforeEach
    internal fun setUp() {
        pixRepository.deleteAll()

        validPixRequest = RequestFactory.createNewKeyRequest()
        validCreatePixKeyBcbRequest = RequestFactory.createPixKeyBcbRequest()
        responseBcb = createPixKeyResponse()
    }

    @Test
    internal fun `should create a pix key EMAIL for CONTE_CORRENTE when data is valid`() {
        `when`(bcbClient.registerPixBcb(validCreatePixKeyBcbRequest)).thenReturn(HttpResponse.created(responseBcb))

        val response = grpcClient.registerKey(validPixRequest)

        with(response) {
            assertNotNull(pixId)
            assertTrue(pixRepository.existsByPixId(pixId))
        }
    }

    @Test
    internal fun `should create a pix key TELEFONE for CONTE_CORRENTE when data is valid`() {
        val mockRequest = RequestFactory.createPixKeyBcbRequest(
            pixType = PixType.TELEFONE,
            pixValue = "+5583999411430"
        )
        `when`(bcbClient.registerPixBcb(mockRequest)).thenReturn(HttpResponse.created(responseBcb))


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
        val mockRequest = RequestFactory.createPixKeyBcbRequest(
            pixType = PixType.CPF,
            pixValue = "12345678909"
        )
        `when`(bcbClient.registerPixBcb(mockRequest)).thenReturn(HttpResponse.created(responseBcb))

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
        val mockRequest = RequestFactory.createPixKeyBcbRequest(
            pixType = PixType.CHAVE_ALEATORIA,
            pixValue = ""
        )
        `when`(bcbClient.registerPixBcb(mockRequest)).thenReturn(HttpResponse.created(responseBcb))

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
    internal fun `should not register a pix when already exists in Banco Central do Brasil`() {
        `when`(bcbClient.registerPixBcb(validCreatePixKeyBcbRequest)).thenReturn(HttpResponse.unprocessableEntity())

        val error: StatusRuntimeException = assertThrows {
            grpcClient.registerKey(validPixRequest)
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Error trying to registry pix key in Banco Central do Brasil", status.description)
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
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
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
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertNotNull(status.description)
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
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertNotNull(status.description)
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
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertNotNull(status.description)
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
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertNotNull(status.description)
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
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertNotNull(status.description)
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
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertNotNull(status.description)
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

    private fun createPixKeyResponse(): CreatePixKeyResponse {
        return CreatePixKeyResponse(
            keyType = PixTypeBcb.EMAIL,
            key = "ponte@email.com",
            bankAccount = BankAccount(
                participant = "ITAU",
                branch = "0001",
                accountNumber = "299021",
                accountType = AccountTypeBcb.CACC
            ),
            owner = Owner(
                type = OwnerType.NATURAL_PERSON,
                name = "Rafael Ponte",
                taxIdNumber = "12345678909"
            ),
            createdAt = "2021-09-23T13:35:21.786071"
        )
    }

    @MockBean(BcbClient::class)
    fun bcbClientMock(): BcbClient {
        return Mockito.mock(BcbClient::class.java)
    }

    @Factory
    class Clients {

        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub {
            return KeyManagerRegisterServiceGrpc.newBlockingStub(channel)
        }
    }
}