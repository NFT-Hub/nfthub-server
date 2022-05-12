package com.nfthub.api.entity

import com.nfthub.api.EMPTY_STRING
import javax.persistence.*

@Entity
@Table
class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @Column(unique = true, nullable = false)
    var name: String = EMPTY_STRING
)

enum class Tag_(val s: String) {
    Id("id"),
    Name("name")
}