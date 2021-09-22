package br.com.zup.edu.keymanager.integration

import br.com.zup.edu.KeyManagerRemoveServiceGrpc
import br.com.zup.edu.RemoveKeyRequest
import br.com.zup.edu.factory.PixFactory
import br.com.zup.edu.keymanager.register.Pix
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@MicronautTest(transactional = false)
internal class KeyManagerRemoveEndpointTest(
    @Inject
    val grpcClient: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub,

    @Inject
    val pixRepository: PixRepository,
    ) {

    private lateinit var validPix: Pix
    lateinit var validUserId: String

    @BeforeEach
    internal fun setUp() {
        pixRepository.deleteAll()

        validPix = PixFactory.createPix()
        validUserId = "c56dfef4-7901-44fb-84e2-a2cefb157890"
    }

    @Test
    internal fun `should remove a pix key when data is valid`() {
        pixRepository.save(validPix)

        val request = RemoveKeyRequest.newBuilder()
            .setUserId(validUserId)
            .setPixValue("ponte@email.com")
            .build()

        val response = grpcClient.removeKey(request)

        assertTrue(response.result)
    }

    @Test
    internal fun `should not remove a pix key when user not found`() {
        val request = RemoveKeyRequest.newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setPixValue("ponte@email.com")
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.removeKey(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave pix não encontrada ou não pertence ao usuário", status.description)
        }
    }

    @Test
    internal fun `should not remove a pix key when pix value not found`() {
        val request = RemoveKeyRequest.newBuilder()
            .setUserId(validUserId)
            .setPixValue(UUID.randomUUID().toString())
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.removeKey(request)
        }

        with(error) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave pix não encontrada ou não pertence ao usuário", status.description)
        }
    }

    @Factory
    class Clients {

        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub {
            return KeyManagerRemoveServiceGrpc.newBlockingStub(channel)
        }
    }
}