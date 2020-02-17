package br.com.gabriellferreira.carlist.domain.model

class RequestFailure(
    val retryable: Retryable
)

interface Retryable {
    fun retry()
}