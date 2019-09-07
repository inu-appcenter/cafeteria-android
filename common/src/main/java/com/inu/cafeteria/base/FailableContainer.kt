package com.inu.cafeteria.base

/**
 * A component that has Failable inside.
 */
interface FailableContainer {
    /**
     * List of failables to handle.
     */
    val failables: MutableList<Failable>
}