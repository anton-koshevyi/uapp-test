package com.uapp_llc.exception

abstract class LocalizedException constructor(

    message: String

) : Exception(message) {

  abstract fun getStatusCode(): Int

}
