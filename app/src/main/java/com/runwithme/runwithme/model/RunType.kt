package com.runwithme.runwithme.model

enum class RunType {
    PERSONAL,
    GROUP;

    companion object{
        fun  describe(runType: RunType) = when (runType) {
            RunType.PERSONAL -> "Personal"
            RunType.GROUP  -> "Group"
        }
    }


}