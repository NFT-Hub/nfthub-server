package com.nfthub.api.entity

import com.nfthub.api.EMPTY_STRING
import javax.persistence.*


@Table
@Entity
class RefreshToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var email: String = EMPTY_STRING,
    var token: String = EMPTY_STRING
) : BaseTimeEntity()