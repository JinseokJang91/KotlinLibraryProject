package com.group.libraryapp.dto.book.response

import com.group.libraryapp.domain.book.BookType

data class BookStatResponse(
    val type: BookType,
    //var count: Int,
//    val count: Int, // group by 쿼리로 바꾸는 경우 리턴 타입은 Long
    val count: Long,
) {
    // BookService - getBookStatistics 리팩토링 (count 가변(var) 사용할 필요 없음)
//    fun plusOne() {
//        count++
//    }
}
