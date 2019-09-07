package com.inu.cafeteria.base

interface FailableHandler {

    val observedFailables: MutableList<Failable>

    /**
     * What to do when some failure occur
     */
    fun onFail(failure: Failable.Failure)

    /**
     * Add failables to manage.
     */
    fun startObservingFailables(failables: List<Failable>)

    /**
     * Stop managing failables.
     * This clears observedFailables.
     */
    fun stopObservingFailables()
}