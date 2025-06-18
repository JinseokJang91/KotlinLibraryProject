package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import com.group.libraryapp.util.findByIdOrThrow
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// ✅ 플러그인을 사용해 open을 생략할 수 있다.
// >> 플러그인 : id 'org.jetbrains.kotlin.plugin.spring' version '1.6.21')
@Service
class UserService (
    private val userRepository: UserRepository,
) {
    @Transactional
    fun saveUser(request: UserCreateRequest) {
        // User에 기본값 설정해놓은 userLoanHistories, id는 제외해도 됨
        val newUser = User(request.name, request.age)
        userRepository.save(newUser)
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        return userRepository.findAll()
          //.map(::UserResponse)
          //.map { UserResponse(it) }
          //.map { user -> UserResponse(user) }
            .map { user -> UserResponse.of(user) } // 정적팩토리 메소드 활용
    }

    @Transactional
    fun updateUserName(request: UserUpdateRequest) {
        //val user = userRepository.findById(request.id).orElseThrow(::IllegalArgumentException)

        // JPA의 findById도 Optional을 반환하지만 컨트롤 불가(JPA 내장 함수)
        // => kotlin의 경우, JPA에서 kotlin을 위한 확장함수인 findByIdOrNull을 사용해 Optional 처리 가능
        //val user = userRepository.findByIdOrNull(request.id) ?: fail()

        // ExceptionUtils에 구현한 확장함수로 한 번 더 변환 가능
        val user = userRepository.findByIdOrThrow(request.id)

        user.updateName(request.name)
    }

    @Transactional
    fun deleteUser(name: String) {
        //val user = userRepository.findByName(name).orElseThrow(::IllegalArgumentException)

        // UserRepository Optional 제거 후
        val user = userRepository.findByName(name) ?: fail() // elvis 연산자 사용

        userRepository.delete(user)
    }
}