package fr.feepin.devfinder.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.Timestamp
import fr.feepin.devfinder.data.models.Message

class MessagesPagingSource(val chatId: String, val chatRepository: ChatRepository) :
    PagingSource<Timestamp, Message>() {

    override suspend fun load(params: LoadParams<Timestamp>): LoadResult<Timestamp, Message> {
        val lastTimestamp = params.key ?: Timestamp.now()
        val messages = chatRepository.fetchRecentMessages(chatId, lastTimestamp)

        return try {
            LoadResult.Page(
                messages,
                messages.firstOrNull()?.sendTimeTs,
                messages.getOrNull(19)?.sendTimeTs,

                )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Timestamp, Message>): Timestamp? {
        return null
    }
}