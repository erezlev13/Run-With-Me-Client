package com.runwithme.runwithme.model.steps

interface StepListener {
    fun step(timeNs: Long)
}