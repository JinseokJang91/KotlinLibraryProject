package com.group.libraryapp.domain.user.loanhistory

import org.springframework.data.jpa.repository.JpaRepository

interface UserLoanHistoryRepository : JpaRepository<UserLoanHistory, Long> {
    // isReturn => status 변경 시 JPA 오류나는 부분은 여기!
    //fun findByBookNameAndIsReturn(bookName: String, isReturn: Boolean): UserLoanHistory?

    // => status로 변경
    fun findByBookNameAndStatus(bookName: String, status: UserLoanStatus): UserLoanHistory?
}