package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.remote.UserDto
import com.example.recipeapp.domain.model.User

fun User.toUserDto(): UserDto {
    return UserDto(
        userUID = userUID,
        name = name
    )
}

fun UserDto.toUser(): User {
    return User(
        userUID = userUID,
        name = name
    )
}