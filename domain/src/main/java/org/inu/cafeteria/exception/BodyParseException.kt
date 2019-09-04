package org.inu.cafeteria.exception

/**
 * Thrown when failed to parse json reponse body.
 */
class BodyParseException(message: String = "") : Exception(message)