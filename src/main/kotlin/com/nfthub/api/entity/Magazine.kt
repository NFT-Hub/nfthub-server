package com.nfthub.api.entity

import com.nfthub.api.EMPTY_STRING
import javax.persistence.*

@Entity
@Table
class Magazine(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,

    @Column(length = 100)
    var title: String = EMPTY_STRING,

    @Column(length = 200)
    var description: String = EMPTY_STRING,

    var url: String = EMPTY_STRING,

    var author: String = EMPTY_STRING,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    var category: Category? = null,

    @OneToMany(mappedBy = "magazine", orphanRemoval = true, cascade = [CascadeType.ALL])
    var magazineTags: List<MagazineTag> = emptyList(),

    @OneToMany(mappedBy = "magazine", orphanRemoval = true, cascade = [CascadeType.ALL])
    var images: MutableList<MagazineImage> = mutableListOf(),

    ) : BaseTimeEntity()


@Entity
@Table
class MagazineTag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var magazine: Magazine = Magazine(),
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var tag: Tag = Tag()
)

@Entity
@Table
class MagazineImage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @Column(columnDefinition = "text", length = 500)
    var url: String = EMPTY_STRING,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var magazine: Magazine = Magazine(),
    var isMain: Boolean = false,
)