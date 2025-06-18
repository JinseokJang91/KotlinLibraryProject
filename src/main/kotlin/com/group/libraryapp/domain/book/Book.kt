package com.group.libraryapp.domain.book

import javax.persistence.*

@Entity
class Book (
    val name: String,

    //val type: String, // [추가] 책 분야

    @Enumerated(EnumType.STRING) // enum class의 index값 대신 프로퍼티 값으로 value가 변경됨(0 -> COMPUTER)
    val type: BookType, // enum class로 변경 (String 타입은 오타, 유지보수가 어려움)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {
    init {
        if(name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    companion object {
        fun fixture(
            name: String = "책 이름",
            type: BookType = BookType.COMPUTER,
            id: Long? = null
        ): Book {
            return Book(
                name = name,
                type = type,
                id = id,
            )
        }
    }
}