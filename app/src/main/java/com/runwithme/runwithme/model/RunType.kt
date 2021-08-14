package com.runwithme.runwithme.model

import java.io.Serializable

enum class RunType() : Serializable {
    PERSONAL,
    GROUP;

    companion object{
        fun  describe(runType: RunType) = when (runType) {
            RunType.PERSONAL -> "Personal"
            RunType.GROUP  -> "Group"
        }
    }

}