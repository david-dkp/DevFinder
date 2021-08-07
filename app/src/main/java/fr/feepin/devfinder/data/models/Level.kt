package fr.feepin.devfinder.data.models

import androidx.annotation.StringRes
import fr.feepin.devfinder.R

enum class Level(val level: Int, @StringRes val textResId: Int) {
    BEGINNER(1, R.string.beginner_level_text),
    INTERMEDIATE(2, R.string.intermediate_level_text),
    ADVANCED(3, R.string.advanced_level_text);

    companion object {
        fun getLevelByNumber(number: Int): Level? {
            return when(number) {
                1 -> BEGINNER
                2 -> INTERMEDIATE
                3 -> ADVANCED
                else -> null
            }
        }
    }

}