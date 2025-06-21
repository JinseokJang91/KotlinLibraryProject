package com.group.libraryapp.domain.book

import com.group.libraryapp.dto.book.response.BookStatResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookRepository : JpaRepository<Book, Long> {
    fun findByName(bookName: String): Book? // Optional<Book> => Optional 제거

    // !! Querydsl로 변경
    // group by 쿼리를 통해 특정 dto로 바로 변환
//    @Query("SELECT NEW com.group.libraryapp.dto.book.response.BookStatResponse(b.type, COUNT(b.id)) " +
//            "FROM Book b GROUP BY b.type")
//    fun getStats(): List<BookStatResponse>
}