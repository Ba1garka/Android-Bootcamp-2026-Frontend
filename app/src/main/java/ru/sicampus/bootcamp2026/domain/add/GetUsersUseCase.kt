package ru.sicampus.bootcamp2026.domain.add

import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.domain.home.entities.UserEntity

class GetUsersUseCase( private val userRepository: UserRepository) {
    suspend operator fun invoke(
        page: Int,
    ): Result<List<UserEntity>>{
        return userRepository.getUsers(
            page = page,
            size = COUNT
        )
    }

    private companion object{
        const val COUNT = 7 //TODO количество элементов помещающихся в экран * 2
    }

}
