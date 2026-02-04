package ru.sicampus.bootcamp2026.data

import ru.sicampus.bootcamp2026.data.source.AuthNetworkDataSource
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

class UserRepository(
    private val authNetworkDataSource: AuthNetworkDataSource
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
}