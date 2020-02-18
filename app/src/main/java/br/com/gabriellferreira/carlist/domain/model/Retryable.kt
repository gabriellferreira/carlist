package br.com.gabriellferreira.carlist.domain.model

interface Retryable {
    fun retry()
}