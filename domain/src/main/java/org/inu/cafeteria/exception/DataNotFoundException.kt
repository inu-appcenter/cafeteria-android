package org.inu.cafeteria.exception

/**
 * Thrown when tried to get data of invalid index or key.
 */
class DataNotFoundException(message: String = "") : Exception(message)