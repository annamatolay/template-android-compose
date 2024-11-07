package dev.anmatolay.template.compose.data.repository

import dev.anmatolay.template.compose.data.source.local.UserIdDataSource
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

// Keep it simple if need more type of datasources in the future for user
@Factory
class UserCacheRepository(private val dataSource: UserIdDataSource) {

    fun retrieveUserId() = dataSource.getUserId()

    fun cacheUserId(id: String): Flow<Unit> = dataSource.putUserId(id)
}
