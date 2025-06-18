package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User

data class UserResponse( // DTO 는 data class로 주로 사용
    val id: Long,
    val name: String,
    val age: Int?,

) {
    // 1번째 방법 - 정적팩토리 메소드 활용
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                age = user.age
            )
        }
    }

    // 2번째 방법 - 부생성자에서 주생성자 호출 후 세팅
//    constructor(user: User): this(
//        id = user.id!!,
//        name = user.name,
//        age = user.age
//    )
}
