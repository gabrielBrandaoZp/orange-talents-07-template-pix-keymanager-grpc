package br.com.zup.edu.shared.validation

import br.com.zup.edu.keymanager.register.NewPixKey
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import jakarta.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
annotation class ValidPixKey(
    val message: String = "Invalid pix type",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

/**
 * Using Bean Validation API because we wanted to use Custom property paths
 * https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-custom-property-paths
 */
@Singleton
class ValidPixKeyValidator: ConstraintValidator<ValidPixKey, NewPixKey> {

    override fun isValid(
        value: NewPixKey?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext,
    ): Boolean {

        // must be validated with @NotNull
        if (value?.pixType == null) {
            return true
        }

        val valid = value.pixType.validation(value.pixId)
        if (!valid) {
            context.messageTemplate("Tipo inválido de chave pix: ${value.pixType}")
        }

        return valid
    }
}