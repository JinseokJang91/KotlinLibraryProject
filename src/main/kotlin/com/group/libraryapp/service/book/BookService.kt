package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.group.libraryapp.repository.book.BookQuerydslRepository
import com.group.libraryapp.repository.user.loanhistory.UserLoanHistoryQuerydslRepository
import com.group.libraryapp.util.fail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService (
    private val bookRepository: BookRepository,
    private val bookQuerydslRepository: BookQuerydslRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
    private val userLoanHistoryQuerydslRepository: UserLoanHistoryQuerydslRepository,
) {
    @Transactional
    fun saveBook(request: BookRequest) {
        val newBook = Book(request.name, request.type)
        bookRepository.save(newBook)
    }

    @Transactional
    fun loanBook(request: BookLoanRequest) {
        //val book = bookRepository.findByName(request.bookName).orElseThrow(::IllegalArgumentException)
        // BookRepository Optional 제거 후
        val book = bookRepository.findByName(request.bookName) ?: fail() // elvis 연산자 사용

        //if(userLoanHistoryRepository.findByBookNameAndIsReturn(request.bookName, false) != null) {
        //if(userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) {
        // !!Querydsl 적용
        if(userLoanHistoryQuerydslRepository.find(request.bookName, UserLoanStatus.LOANED) != null) {
            throw IllegalArgumentException("진작 대출되어 있는 책입니다")
        }

        //val user = userRepository.findByName(request.userName).orElseThrow(::IllegalArgumentException)
        // UserRepository Optional 제거 후
        val user = userRepository.findByName(request.userName) ?: fail() // elvis 연산자 사용

        user.loanBook(book)
    }

    @Transactional
    fun returnBook(request: BookReturnRequest) {
        //val user = userRepository.findByName(request.userName).orElseThrow(::IllegalArgumentException)
        // UserRepository Optional 제거 후
        val user = userRepository.findByName(request.userName) ?: fail() // elvis 연산자 사용

        user.returnBook(request.bookName)
    }

    @Transactional(readOnly = true)
    fun countLoanedBook(): Int {
//        return userLoanHistoryRepository.findAllByStatus(UserLoanStatus.LOANED).size
        // count 쿼리 사용해서 리팩토링
        //return userLoanHistoryRepository.countByStatus(UserLoanStatus.LOANED).toInt()
        // !!Querydsl 적용
        return userLoanHistoryQuerydslRepository.count(UserLoanStatus.LOANED).toInt()
    }

    @Transactional(readOnly = true)
    fun getBookStatistics(): List<BookStatResponse> {
        // 아래 코드 리팩토링 (3) - Querydsl 적용
        return bookQuerydslRepository.getStats()
        // 아래 코드 리팩토링 (2)
        //return bookRepository.getStats()
        // 아래 코드 리팩토링
        /*
        return bookRepository.findAll() // List<Book>
            .groupBy { book -> book.type } // Map<BookType, List<Book>>
            .map { (type, books) -> BookStatResponse(type, books.size) } // List<BookStatResponse>
         */

        /*
        val results = mutableListOf<BookStatResponse>()
        val books = bookRepository.findAll()
        for(book in books) {
//            val targetDto = results.firstOrNull { dto -> book.type == dto.type }
//            if(targetDto == null) {
//                results.add(BookStatResponse(book.type, 1))
//            } else {
//                targetDto.plusOne()
//            }
            // 코드 간소화
            results.firstOrNull { dto -> book.type == dto.type }?.plusOne()
                ?: results.add(BookStatResponse(book.type, 1))
        }

        return results
        */
    }
}