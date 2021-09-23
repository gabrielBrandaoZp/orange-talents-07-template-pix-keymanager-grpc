package br.com.zup.edu.keymanager.external.bcb

data class DeletePixKeyRequest(
    val key: String,
    val participant: String
)
