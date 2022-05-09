package com.nfthub.api.controller

import com.nfthub.api.dto.MagazineCreateRequest
import com.nfthub.api.dto.MagazineResponse
import com.nfthub.api.dto.MagazineUpdateRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@Tag(name = "magazine", description = "매거진 api")
@RequestMapping("/api/v1/magazines")
@RestController
class MagazineController {
    @Operation(summary = "매거진 리스트 조회 [미구현]")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @Parameters(
        value = [
            Parameter(
                name = "page", description = "0 부터 시작되는 페이지 (0..N)",
                `in` = ParameterIn.QUERY,
                schema = Schema(type = "integer", defaultValue = "0")
            ),
            Parameter(
                name = "size", description = "페이지의 사이즈",
                `in` = ParameterIn.QUERY,
                schema = Schema(type = "integer", defaultValue = "5")
            ),
            Parameter(
                `in` = ParameterIn.QUERY,
                description = "정렬할 대상과 정렬 방식, 데이터 형식: property(,asc|desc). "
                        + "디폴트 정렬순서는 오름차순, 다중 정렬 가능",
                name = "sort",
                array = ArraySchema(schema = Schema(type = "integer", defaultValue = "view,desc"))
            ),
            Parameter(
                `in` = ParameterIn.QUERY,
                description = "키워드 입력, 여러개 사용 가능",
                name = "keywordId",
                array = ArraySchema(schema = Schema(type = "integer"))
            ),
            Parameter(
                `in` = ParameterIn.QUERY,
                description = "카테고리 입력, 여러개 사용 가능",
                name = "categoryId",
                array = ArraySchema(schema = Schema(type = "integer"))
            ),
            Parameter(
                `in` = ParameterIn.QUERY,
                description = "검색어 입력",
                name = "searchKeyword",
                schema = Schema(type = "String")
            ),
        ]
    )
    @GetMapping
    fun getMagazineResponses(
        @PageableDefault
        @Parameter(hidden = true) pageable: Pageable,
        @RequestParam(required = false) keywordId: List<Long>?,
        @RequestParam(required = false) categoryId: List<Long>?,
        @RequestParam(required = false) searchKeyword: String?
    ): Page<MagazineResponse> = PageImpl(emptyList())

    @Operation(summary = "[어드민] 매거진 조회")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @GetMapping("/{magazineId}")
    fun getMagazineResponse(
        @PathVariable magazineId: Long
    ) = MagazineResponse()

    @Operation(summary = "[어드민] 매거진 생성", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
    )
    @PostMapping
    fun createMagazine(
        @RequestBody magazineCreateRequest: MagazineCreateRequest
    ) = MagazineResponse()

    @Operation(summary = "[어드민] 매거진 수정", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
        ApiResponse(responseCode = "409", description = "이미 존재"),
    )
    @PatchMapping("/{magazineId}")
    fun updateMagazine(
        @PathVariable magazineId: Long,
        @RequestBody @Valid magazineUpdateRequest: MagazineUpdateRequest
    ) = MagazineResponse()

    @Operation(summary = "[미구현] 매거진 카테고리 수정", description = "어드민 전용")
    @PatchMapping("/{magazineId}/category")
    fun updateMagazineCategory(
        @PathVariable magazineId: String,
        @RequestBody @Valid categoryId: Long
    ) {
    }

    @Operation(summary = "[미구현 매거진 키워드 수정]", description = "어드민 전용")
    @PatchMapping("/{magazineId}/keywords")
    fun updateMagazineKeywords(
        @PathVariable magazineId: String,
        @RequestBody @Valid keywordIds: List<Long>
    ) {
    }

    @Operation(summary = "[어드민] 매거진 이미지 등록", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @PutMapping(
        "/{magazineId}/images",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
    fun createMagazineImage(
        @PathVariable magazineId: Long,
        @RequestBody @Valid multipartFiles: List<MultipartFile>,
    ) {
    }

    @Operation(summary = "[어드민] 매거진 이미지 제거", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @PutMapping(
        "/{magazineId}/images/{imageId}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
    fun deleteMagazineImage(
        @PathVariable magazineId: Long,
        @PathVariable imageId: String,
    ) {
    }

    @Operation(summary = "[어드민] 매거진 제거", description = "어드민 전용")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상응답"),
        ApiResponse(responseCode = "401", description = "인증 필요"),
        ApiResponse(responseCode = "403", description = "권한 없음"),
        ApiResponse(responseCode = "404", description = "찾을 수 없음"),
    )
    @DeleteMapping("/{magazineId}")
    fun deleteMagazine(
        @PathVariable magazineId: Long,
    ) {
    }
}