package br.com.zup.edu.keymanager.external.bcb

data class Problem(
    val type: String,
    val status: Int,
    val title: String,
    val detail: String,
    val violations: List<Violation>,
)

data class Violation(
    val field: String,
    val message: String,
)
