package com.nfthub.api.entity

import com.nfthub.api.EMPTY_STRING
import javax.persistence.*

@Entity
@Table
class Artist(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,

    var view: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    var category: Category? = null,

    @OneToMany(mappedBy = "artist", orphanRemoval = true, cascade = [CascadeType.ALL])
    var artistKeywords: List<ArtistKeyword> = emptyList(),

    @OneToMany(mappedBy = "artist", orphanRemoval = true, cascade = [CascadeType.ALL])
    var artistImages: List<ArtistImage> = emptyList(),

    @OneToMany(mappedBy = "artist", orphanRemoval = true, cascade = [CascadeType.ALL])
    var artistArt: List<Art> = emptyList()
) : BaseTimeEntity()

@Entity
@Table
class ArtistKeyword(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var artist: Artist = Artist()
)

@Entity
@Table
class ArtistImage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0,
    @Column(columnDefinition = "text", length = 500)
    var url: String = EMPTY_STRING,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var artist: Artist = Artist(),
    var isMain: Boolean = false,
)