package fr.feepin.devfinder.utils

import android.content.Context
import android.text.format.DateFormat
import com.google.firebase.Timestamp

object TimeFormatUtils {

    fun getFormattedTimestamp(context: Context, timestamp: Timestamp): String {
        val timeFormat = DateFormat.getTimeFormat(context)

        return timeFormat.format(timestamp.toDate())
    }

}