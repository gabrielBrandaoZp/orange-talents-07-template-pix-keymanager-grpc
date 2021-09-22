package br.com.zup.edu.keymanager.register

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixRepository : JpaRepository<Pix, Long> {
    fun existsByPixId(id: String?): Boolean

    fun findByPixIdAndUserId(pixId: String, userId: UUID): Optional<Pix>
}