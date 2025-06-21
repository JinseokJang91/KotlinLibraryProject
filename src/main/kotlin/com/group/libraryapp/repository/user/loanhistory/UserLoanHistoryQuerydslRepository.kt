package com.group.libraryapp.repository.user.loanhistory

import com.group.libraryapp.domain.user.loanhistory.QUserLoanHistory.userLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class UserLoanHistoryQuerydslRepository (
    private val queryFactory: JPAQueryFactory,
) {
    // 동적 쿼리 생성을 통해 UserLoanHistoryRepository 메소드 통합 가능
    // (1) UserLoanHistoryRepository.findByBookName
    // (2) UserLoanHistoryRepository.findByBookNameAndStatus
    fun find(bookName: String, status: UserLoanStatus? = null): UserLoanHistory? {
        // => default parameter인 status 값에 null을 넣어 동적 쿼리화
        return queryFactory
            .select(userLoanHistory)
            .from(userLoanHistory)
            .where(
                userLoanHistory.bookName.eq(bookName),
                status?.let { userLoanHistory.status.eq(status) }
            )
            .limit(1)
            .fetchOne()
    }

    fun count(status: UserLoanStatus): Long {
        return queryFactory
            .select(userLoanHistory.id.count()) // userLoanHistory.count()도 가능
            .from(userLoanHistory)
            .where(
                userLoanHistory.status.eq(status)
            )
            .fetchOne() ?: 0L
    }
}