package com.example.recipeapp.data.mapper

import com.example.recipeapp.data.remote.UserDto
import com.example.recipeapp.domain.model.User
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class UserMapperTest {
    private lateinit var user: User
    private lateinit var userDto: UserDto

    @Before
    fun setUp() {
        user = User(
            userUID = "userUID",
            name = "John Smith"
        )

        userDto = UserDto(
            userUID = "userUID",
            name = "John Smith"
        )
    }

    @Test
    fun `User can be mapped to UserDto`() {
        val mappedUserDto = user.toUserDto()

        assertThat(mappedUserDto).isEqualTo(userDto)
    }

    @Test
    fun `UserDto can be mapped to User`() {
        val mappedUser = userDto.toUser()

        assertThat(mappedUser).isEqualTo(user)
    }
}