package br.com.gabriellferreira.carlist.data.model

@Suppress("UNUSED_PARAMETER", "unused")
open class DataException(message: String = "", cause: Exception?) : Exception(cause)

