package com.group.libraryapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibraryAppApplication

fun main(args: Array<String>) {
    // runApplication : 스프링에서 kotlin 개발 시 지원하는 확장 함수
    // => Java의 SpringApplication.run(LibraryAppApplication.class, args)와 같음
    // 가변 인자 배열 삽입 시 스프레드 연산자(*) 사용
    runApplication<LibraryAppApplication>(*args)
}