package br.com.zup.edu.keymanager

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PixRepository : JpaRepository<Pix, Long> {
    fun existsByPixId(id: String?): Boolean
}