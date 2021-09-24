package br.com.zup.edu.keymanager.integration

import br.com.zup.edu.KeyManagerSearchPixServiceGrpc
import br.com.zup.edu.SearchKeyExternalRequest
import br.com.zup.edu.SearchKeyRequest
import br.com.zup.edu.factory.PixFactory
import br.com.zup.edu.keymanager.external.bcb.BcbClient
import br.com.zup.edu.keymanager.register.Pix
import br.com.zup.edu.keymanager.register.PixRepository
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

@MicronautTest(transactional = false)
internal class SearchPixEndpointTest(

    @Inject
    val grpcClient: KeyManagerSearchPixServiceGrpc.KeyManagerSearchPixServiceBlockingStub,

    @Inject
    val bcbClient: BcbClient,

    @Inject
    val pixRepository: PixRepository,

    ) {

    lateinit var validPix: Pix
    lateinit var validKey: String
    lateinit var validUserId: String

    @BeforeEach
    internal fun setUp() {
        pixRepository.deleteAll()

        validPix = PixFactory.createPix()
        validKey = "ponte@email.com"
        validUserId = "c56dfef4-7901-44fb-84e2-a2cefb157890"
    }

    @Test
    internal fun `should return a pix info when valid user id and pix id`() {
        pixRepository.save(validPix)

        `when`(bcbClient.findByKey(validKey)).thenReturn(HttpResponse.ok())

        val response = grpcClient.searchKeyInternal(SearchKeyRequest.newBuilder()
            .setUserId(validUserId)
            .setPixId(1)
            .build())

        with(response) {
            assertNotNull(response)
            assertEquals(1, pixIdInternal)
            assertEquals(validKey, pixInfo.pixValue)
        }
    }

    @Test
    internal fun `should not return a pix info when user not found`() {
        val request = SearchKeyRequest.newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setPixId(1)
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.searchKeyInternal(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave pix não encontrada ou não pertence ao usuário", status.description)
        }
    }

    @Test
    internal fun `should not return a pix info when pix id not found`() {
        val request = SearchKeyRequest.newBuilder()
            .setUserId(validUserId)
            .setPixId(1)
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.searchKeyInternal(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave pix não encontrada ou não pertence ao usuário", status.description)
        }
    }

    @Test
    internal fun `should not return a pix info when pix has no registry in Banco Central do Brasil`() {
        pixRepository.save(validPix)
        `when`(bcbClient.findByKey(validKey)).thenReturn(HttpResponse.notFound())

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.searchKeyInternal(SearchKeyRequest.newBuilder()
                .setUserId(validUserId)
                .setPixId(2)
                .build())
        }

        with(error) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Pix key not found in Banco central do Brasil", status.description)
        }
    }

    @Test
    internal fun `should return a pix info when pix value is valid`() {
        pixRepository.save(validPix)
        `when`(bcbClient.findByKey(validKey)).thenReturn(HttpResponse.ok())

        val response = grpcClient.searchKeyExternal(SearchKeyExternalRequest.newBuilder()
            .setPixValue(validKey)
            .build())

        with(response) {
            assertNotNull(response)
            assertEquals(validKey, pixInfo.pixValue)
        }
    }

    @Test
    internal fun `should not return pix info when pix value does not exists`() {
        val invalidKey = UUID.randomUUID().toString()

        `when`(bcbClient.findByKey(invalidKey)).thenReturn(HttpResponse.notFound())

        val request = SearchKeyExternalRequest.newBuilder()
            .setPixValue(invalidKey)
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.searchKeyExternal(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave pix não encontrada ou não pertence ao usuário", status.description)
        }
    }

    @MockBean(BcbClient::class)
    fun bcbClientMock(): BcbClient {
        return Mockito.mock(BcbClient::class.java)
    }

    @Factory
    class Clients {

        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerSearchPixServiceGrpc.KeyManagerSearchPixServiceBlockingStub {
            return KeyManagerSearchPixServiceGrpc.newBlockingStub(channel)
        }
    }
}