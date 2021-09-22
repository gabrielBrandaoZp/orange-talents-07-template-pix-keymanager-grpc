package br.com.zup.edu.shared.handlers

import br.com.zup.edu.keymanager.remove.PixKeyNotFoundException
import io.grpc.Status
import jakarta.inject.Singleton

@Singleton
class PixKeyNotFoundExceptionHandler : ExceptionHandler<PixKeyNotFoundException> {

    override fun handle(e: PixKeyNotFoundException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.NOT_FOUND
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is PixKeyNotFoundException
    }
}