package com.example.data.remote

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.data.local.ContactDatabase
import com.example.data.local.ContactEntity
import com.example.data.mappers.toContactEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ContactRemoteMediator(
    private val contactDatabase: ContactDatabase,
    private val contactApi: ContactApi,
) : RemoteMediator<Int, ContactEntity>() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ContactEntity>,
    ): MediatorResult {
        return try {
            val loadKey =
                when (loadType) {
                    LoadType.REFRESH -> 1
                    LoadType.PREPEND -> return MediatorResult.Success(
                        endOfPaginationReached = true,
                    )
                    LoadType.APPEND -> {
                        val lastItem = state.lastItemOrNull()
                        if (lastItem == null) {
                            1
                        } else {
                            (lastItem.index / state.config.pageSize) + 1
                        }
                    }
                }

            // Check if the current list size is less than the page size
            val currentSize = state.pages.flatMap { it.data }.size
            if (currentSize < state.config.pageSize && loadType == LoadType.REFRESH) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val contacts =
                contactApi
                    .getContacts(
                        page = loadKey,
                        results = state.config.pageSize,
                    ).results

            contactDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    contactDatabase.dao.clearAll()
                }
                val contactEntities = contacts.map { it.toContactEntity() }
                contactDatabase.dao.upsert(contactEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = contacts.isEmpty(),
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
