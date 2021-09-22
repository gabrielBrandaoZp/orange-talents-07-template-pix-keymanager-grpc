package br.com.zup.edu.shared.handlers

import br.com.zup.edu.keymanager.register.PixKeyAlreadyExistsException
import io.grpc.Status
import jakarta.inject.Singleton

@Singleton
class PixKeyAlreadyExistsExceptionHandler : ExceptionHandler<PixKeyAlreadyExistsException> {

    override fun handle(e: PixKeyAlreadyExistsException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.ALREADY_EXISTS
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is PixKeyAlreadyExistsException
    }
}