package org.inu.cafeteria.repository

/**
 * You have to create your own implementation of this repository.
 * It is forbidden to upload the server url to VCS.
 */
abstract class PrivateRepository : Repository() {

    abstract fun getServerBaseUrl(): String
}