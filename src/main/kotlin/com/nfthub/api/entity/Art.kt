package com.nfthub.api.entity

import com.nfthub.api.EMPTY_STRING
import javax.persistence.*

@Entity
@Table
class Art(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,

    var view: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    var category: Category? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    var artist: Artist = Artist(),

    @OneToMany(mappedBy = "art", orphanRemoval = true, cascade = [CascadeType.ALL])
    var artKeywords: List<ArtKeyword> = emptyList(),

    @OneToMany(mappedBy = "art", orphanRemoval = true, cascade = [CascadeType.ALL])
    var artImages: List<ArtImage> = emptyList(),

    ) : BaseTimeEntity()

@Entity
@Table
class ArtKeyword(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var art: Art = Art(),
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var keyword: Keyword = Keyword(),
)

@Entity
@Table
class ArtImage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @Column(columnDefinition = "text", length = 500)
    var url: String = EMPTY_STRING,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var art: Art = Art(),
    var isMain: Boolean = false,
)