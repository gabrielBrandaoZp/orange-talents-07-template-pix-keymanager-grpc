package br.com.zup.edu.keymanager.integration

import br.com.zup.edu.KeyManagerListUserPixServiceGrpc
import br.com.zup.edu.ListUserPixRequest
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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest(transactional = false)
internal class ListUserPixEndpointTest(
    @Inject
    val pixRepository: PixRepository,

    @Inject
    val grpcClient: KeyManagerListUserPixServiceGrpc.KeyManagerListUserPixServiceBlockingStub
) {

    lateinit var validUserId: String
    lateinit var validPix: Pix

    @BeforeEach
    internal fun setUp() {
        pixRepository.deleteAll()

        validUserId = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        validPix = PixFactory.createPix()
    }

    @Test
    internal fun `should return a list of pix when user id is valid`() {
        pixRepository.save(validPix)

        val request = ListUserPixRequest.newBuilder()
            .setUserId(validUserId)
            .build()

        val response = grpcClient.listUserPix(request)

        with(response) {
            assertNotNull(this)
            assertEquals("c56dfef4-7901-44fb-84e2-a2cefb157890", userId)
            assertTrue(pixDetailsCount == 1)
        }
    }

    @Test
    internal fun `should return an empty list of pix when user id is valid`() {
        val request = ListUserPixRequest.newBuilder()
            .setUserId(validUserId)
            .build()

        val response = grpcClient.listUserPix(request)

        with(response) {
            assertNotNull(this)
            assertEquals("c56dfef4-7901-44fb-84e2-a2cefb157890", userId)
            assertTrue(pixDetailsCount == 0)
        }
    }

    @Test
    internal fun `should return invalid argument when user id is invalid`() {
        val request = ListUserPixRequest.newBuilder()
            .setUserId("")
            .build()

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.listUserPix(request)
        }

        with(error) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("User id cannot be null or empty", status.description)
        }
    }

    @Factory
    class Clients {

        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerListUserPixServiceGrpc.KeyManagerListUserPixServiceBlockingStub {
            return KeyManagerListUserPixServiceGrpc.newBlockingStub(channel)
        }
    }
}