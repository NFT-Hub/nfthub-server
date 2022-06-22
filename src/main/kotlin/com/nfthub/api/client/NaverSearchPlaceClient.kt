package com.nfthub.api.client

import com.nfthub.api.EMPTY_STRING
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "searchPlace", url = "https://map.naver.com/v5/api/search")
interface NaverSearchPlaceClient {
    @GetMapping
    fun getPlaces(
        @RequestParam(required = true) caller: String = "pcweb",
        @RequestParam(required = true) query: String = "",
        @RequestParam type: String = "all",
        @RequestParam page: Long = 1,
        @RequestParam displayCount: Long = 100,
        @RequestParam lang: String = "ko"
    ): NaverSearchPlaceResult
}


data class NaverSearchPlaceResult (
    var result: NaverSearchPlace?
)

data class ErrorResult (
    var code: String = EMPTY_STRING
)

data class NaverSearchPlace (
    var place: NaverSearchPlaceDetail?
)

data class NaverSearchPlaceDetail(
    var list: List<PlaceDetail>,
)

data class PlaceDetail(
    var id: String? = EMPTY_STRING,
    var name: String? = EMPTY_STRING, // 물류사 이름
    var address: String? = EMPTY_STRING,
    var roadAddress: String? = EMPTY_STRING,
    var tel: String? = EMPTY_STRING, // 전화번호
    var category: List<String>? = emptyList(), // 카테고리
    var bizhourInfo: String? = EMPTY_STRING,
    var thumUrl: String? = EMPTY_STRING,
    var shortAddress: List<String>? = emptyList(),
    var homePage: String? = EMPTY_STRING,
)


