package com.nfthub.api.entity

import com.nfthub.api.EMPTY_STRING
import javax.persistence.*

@Entity
@Table
class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @Column(unique = true, nullable = false)
    var name: String = EMPTY_STRING,
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    var categoryGroup: CategoryGroup? = null
)

enum class Category_(val s: String) {
    Id("id"),
    Name("name"),
    CategoryGroup("categoryGroup")
}

@Entity
@Table
class CategoryGroup(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @Column(unique = true, nullable = false)
    var name: String = EMPTY_STRING,
    @OneToMany(mappedBy = "categoryGroup")
    var categories: List<Category> = emptyList()
)

enum class CategoryGroup_(val s: String) {
    Name("name"),
    Categories("categories")
}