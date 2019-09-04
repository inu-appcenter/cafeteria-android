package org.inu.cafeteria.exception

/**
 * Thrown when response body is null.
 */
class NullBodyException(message: String = "") : Exception(message)