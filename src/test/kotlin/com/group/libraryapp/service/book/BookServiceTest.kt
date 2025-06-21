package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
        userLoanHistoryRepository.deleteAll()
    }

    @Test
    @DisplayName("책 등록 테스트 결과 정상")
    fun saveBookTest() {
        // given
        val request = BookRequest("이상한 나라의 앨리스", BookType.COMPUTER)

        // when
        bookService.saveBook(request)

        // then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("이상한 나라의 앨리스")
        assertThat(books[0].type).isEqualTo(BookType.COMPUTER)
    }

    @Test
    @DisplayName("책 대출 정상 동작 테스트 결과 정상")
    fun loanBookTest() {
        // given
        bookRepository.save(Book.fixture("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("홍길동", 30))

        val request = BookLoanRequest("홍길동", "이상한 나라의 앨리스")

        // when
        bookService.loanBook(request)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("이상한 나라의 앨리스")
        assertThat(results[0].user.id).isEqualTo(savedUser.id)
        //assertThat(results[0].status).isFalse() // isFalse() 대신 isFalse만 작성해도 정상 동작함
        // enum class 적용
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED) // isFalse() 대신 isFalse만 작성해도 정상 동작함
    }

    @Test
    @DisplayName("책이 진작 대출되어 있다면, 신규 대출이 실패")
    fun loanBookFailTest() {
        // given
        bookRepository.save(Book.fixture("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("홍길동", 30))

        userLoanHistoryRepository.save(
            UserLoanHistory.fixture(
                savedUser,
                "이상한 나라의 앨리스",
//                UserLoanStatus.LOANED
            )
        )
        val request = BookLoanRequest("홍길동", "이상한 나라의 앨리스")

        // when
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message

        // then
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName("책 반납 테스트 결과 정상")
    fun returnBookTest() {
        // given
        bookRepository.save(Book.fixture("이상한 나라의 앨리스"))
        val savedUser = userRepository.save(User("홍길동", 30, Collections.emptyList(), null))

        userLoanHistoryRepository.save(
            UserLoanHistory.fixture(
                savedUser,
                "이상한 나라의 앨리스",
//                UserLoanStatus.LOANED
            )
        )
        val request =
            BookReturnRequest("홍길동", "이상한 나라의 앨리스")

        // when
        bookService.returnBook(request)

        // then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        //assertThat(results[0].status).isTrue
        // enum class 적용
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    @DisplayName("책 대여 권수를 정상 확인한다")
    fun countLoanedBookTest() {
        // given
        val savedUser = userRepository.save(User("홍길동", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUser, "A"),
            UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
            UserLoanHistory.fixture(savedUser, "C", UserLoanStatus.RETURNED),
        ))

        // when
        val result = bookService.countLoanedBook()

        // then
        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("분야별 책 권수를 정상 확인한다")
    fun getBookStatisticsTest() {
        // given
        bookRepository.saveAll(listOf(
            Book.fixture("A", BookType.COMPUTER),
            Book.fixture("B", BookType.COMPUTER),
            Book.fixture("C", BookType.SCIENCE),
        ))

        // when
        val results = bookService.getBookStatistics()

        // then
        assertThat(results).hasSize(2)
        assertCount(results, BookType.COMPUTER, 2L)
        assertCount(results, BookType.SCIENCE, 1L)

//        val computerDto = results.first { result -> result.type == BookType.COMPUTER }
//        assertThat(computerDto.count).isEqualTo(2)
//
//        val scienceDto = results.first { result -> result.type == BookType.SCIENCE }
//        assertThat(scienceDto.count).isEqualTo(1)
    }

    // group by 쿼리로 변경 시 count 타입 변경 Int > Long
    private fun assertCount(results: List<BookStatResponse>, type: BookType, count: Long) {
        assertThat(results.first { result -> result.type == type }.count).isEqualTo(count)
    }
}