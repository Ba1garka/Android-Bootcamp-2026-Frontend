package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.EventEntity
import ru.sicampus.bootcamp2026.domain.home.entities.ParticipantEntity
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

class UserRepository(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val userInfoDataSource: UserInfoDataSource
) {
    suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Result<UserEntity>{
        return authNetworkDataSource.register(email, password, fullName).mapCatching { userDto ->
            UserEntity(
                id = userDto.id ?: throw Exception("ID is null"),
                email = userDto.email ?: throw Exception("Email is null"),
                fullName = userDto.fullName ?: throw Exception("FullName is null")
            )
        }
    }

    suspend fun getUsers(page: Int, size: Int): Result<List<UserEntity>>{
        return userInfoDataSource.getUsers(page = page, size = size).mapCatching{ dto ->
            dto.content?.mapNotNull { userDto ->
                UserEntity(
                    id = userDto.id ?: return@mapNotNull null,
                    email = userDto.email?: return@mapNotNull null,
                    fullName = userDto.fullName ?: return@mapNotNull null
                )
            } ?: error("Список пуст")
        }
    }
}