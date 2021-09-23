package br.com.zup.edu.keymanager.external.bcb

data class DeletedPixKeyResponse(
    val key: String,
    val participant: String,
    val deletedAt: String
)
