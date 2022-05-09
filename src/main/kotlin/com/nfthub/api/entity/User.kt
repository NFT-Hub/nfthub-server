package com.nfthub.api.entity

import com.nfthub.api.EMPTY_STRING
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*


enum class Role(role: String) {
    NORMAL("ROLE_NORMAL"),
    ADMIN("ROLE_ADMIN")
}

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var name: String = EMPTY_STRING,

    @Column(nullable = false, unique = true)
    var email: String = EMPTY_STRING,

    @Column(nullable = false)
    var passwords: String = EMPTY_STRING,

    var verifiedEmail: Boolean = false,

    var role: Role = Role.NORMAL,

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
    var bookmarkMagazines: List<BookmarkMagazine> = emptyList(),

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
    var bookmarkArt: List<BookmarkArt> = emptyList(),

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = [CascadeType.ALL])
    var bookmarkArtist: List<BookmarkArtist> = emptyList()
) : BaseTimeEntity(), UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = arrayListOf(
        GrantedAuthority { role.toString() }
    )

    override fun getPassword(): String = this.passwords

    override fun getUsername(): String = this.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

@Entity
@Table
class BookmarkMagazine(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var magazine: Magazine = Magazine(),
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user: User = User()
)

@Entity
@Table
class BookmarkArt(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var magazine: Art = Art(),
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user: User = User()
)

@Entity
@Table
class BookmarkArtist(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var magazine: Artist = Artist(),
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user: User = User()
)