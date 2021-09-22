package br.com.zup.edu.shared.handlers

import io.micronaut.aop.Around
import io.micronaut.context.annotation.Type
import kotlin.annotation.AnnotationTarget.*

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(CLASS, FILE, FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Around
@Type(ExceptionHandlerInterceptor::class)
annotation class ErrorHandler()
