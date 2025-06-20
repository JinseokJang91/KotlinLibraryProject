package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory

data class UserLoanHistoryResponse (
    val name: String, // 유저 이름
    val books: List<BookHistoryResponse>
) {
    companion object {
        fun of(user: User): UserLoanHistoryResponse {
            return UserLoanHistoryResponse(
                name = user.name,
                books = user.userLoanHistories.map(BookHistoryResponse::of)
            )
        }
    }
}

data class BookHistoryResponse (
    val name: String, // 책 이름
    val isReturn: Boolean,
) {
    companion object {
        fun of(history: UserLoanHistory): BookHistoryResponse {
            return BookHistoryResponse(
                name = history.bookName,
//                isReturn = history.status == UserLoanStatus.RETURNED,
                // 간소화 => UserLoanHistory 내 isReturn 프로퍼티 생성 후 커스텀 getter 생성
                isReturn = history.isReturn
            )
        }
    }
}