package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

// (참고) Live Template 저장함
// ctrl + space => commentForm 검색

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
) {

    @AfterEach
    fun clean() {
        // 테스트 전체 실행 시 SpringContext는 하나만 뜨게 됨 => DB 서버도 하나만 뜨게 됨
        // getUsersTest()에서 size는 2가 아닌 3이므로 테스트 실패
        // 각 테스트 종료 시 마다 delete 처리
        userRepository.deleteAll()

        // (자체 테스트 결과 참고)
        // saveUserTest에 @Transactional 어노테이션 사용해도 처리됨 (단, open 적용 해야 함)
    }

    @Test
    @DisplayName("사용자 저장 테스트 결과 정상")
    fun saveUserTest() {
        // given
        val request = UserCreateRequest("홍길동", null)

        // when
        userService.saveUser(request)

        // then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("홍길동")

        // [ERROR] NPE : results[0].age must not be null
        // => User.java 의 Integer 타입인 age를 kotlin으로 변환 시 플랫폼 타입으로 간주하여 not null로 인식
        // => 해당 필드(age)에 @Nullable 어노테이션 추가
        assertThat(results[0].age).isNull()
    }

    @Test
    @DisplayName("사용자 전체 조회 테스트 결과 정상")
    fun getUsersTest() {
        // given
        userRepository.saveAll(listOf(
            User("A", 20),
            User("B", null),
        ))

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactly("A", "B")
        assertThat(results).extracting("name").containsExactlyInAnyOrder("B", "A")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)
    }

    @Test
    @DisplayName("사용자 변경 테스트 결과 정상")
    fun updateUserNameTest() {
        // given
        val savedUser = userRepository.save(User("A", null))
        var request = UserUpdateRequest(savedUser.id, "B")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("사용자 삭제 테스트 결과 정상")
    fun deleteUserTest() {
        // given
        userRepository.save(User("A", null))

        // when
        userService.deleteUser("A")

        // then
        assertThat(userRepository.findAll()).hasSize(0)
        assertThat(userRepository.findAll()).isEmpty()
    }
}