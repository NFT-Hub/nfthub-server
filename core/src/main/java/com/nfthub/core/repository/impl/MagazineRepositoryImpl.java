package com.nfthub.core.repository.impl;

import com.nfthub.core.entity.*;
import com.nfthub.core.repository.support.MagazineRepositorySupport;
import com.nfthub.core.util.ProjectStringUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
public class MagazineRepositoryImpl extends QuerydslRepositorySupport implements MagazineRepositorySupport {
    final QMagazine magazine = QMagazine.magazine;
    final QMagazineImage magazineImage = QMagazineImage.magazineImage;
    final QMagazineTag magazineTag = QMagazineTag.magazineTag;
    final QCategory category = QCategory.category;
    final QCategoryGroup categoryGroup = QCategoryGroup.categoryGroup;
    final QTag tag = QTag.tag;
    private final JPAQueryFactory queryFactory;

    public MagazineRepositoryImpl(
            @Autowired JPAQueryFactory jpaQueryFactory
    ) {
        super(Magazine.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Magazine> findAllBy(
            Pageable pageable,
            // 필터조건
            List<Long> tagIds,
            List<Long> categoryIds,
            List<Long> categoryGroupIds,
            // 필터이후 적용조건 - keyword는 secondary condition
            String keyword,
            String tagKeyword,
            String categoryKeyword,
            String categoryGroupKeyword,
            String titleKeyword
    ) {
        JPQLQuery<Magazine> query = queryFactory.selectDistinct(magazine)
                .from(magazine)
                .leftJoin(magazineImage).on(magazineImage.magazine.id.eq(magazine.id))
                .leftJoin(magazineTag).on(magazineTag.magazine.id.eq(magazine.id))
                .leftJoin(tag).on(tag.id.eq(magazineTag.tag.id))
                .leftJoin(category).on(category.id.eq(magazine.category.id))
                .leftJoin(categoryGroup).on(categoryGroup.id.eq(category.categoryGroup.id))
                .where(booleanBuilder(tagIds, categoryIds, categoryGroupIds, keyword, tagKeyword, categoryKeyword, categoryGroupKeyword, titleKeyword));
        Querydsl querydsl = Objects.requireNonNull(getQuerydsl());
        List<Magazine> magazines = querydsl.applyPagination(pageable, query).fetch();
        return new PageImpl<>(magazines, pageable, getTotal());
    }

    // 토탈은 코스트상 필요시 재정의
    private long getTotal() {
        return 100;
    }

    private BooleanBuilder booleanBuilder(
            List<Long> tagIds,
            List<Long> categoryIds,
            List<Long> categoryGroupIds,
            String keyword,
            String tagKeyword,
            String categoryKeyword,
            String categoryGroupKeyword,
            String titleKeyword
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        BooleanBuilder filterBuilder = new BooleanBuilder();
        categoryIn(filterBuilder, categoryIds);
        tagIn(filterBuilder, tagIds);
        categoryGroupIn(filterBuilder, categoryGroupIds);

        BooleanBuilder includeBuilder = new BooleanBuilder();
        tagNameLike(includeBuilder, tagKeyword, keyword);
        categoryNameLike(includeBuilder, categoryKeyword, keyword);
        categoryGroupNameLike(includeBuilder, categoryGroupKeyword, keyword);
        magazineTitleLike(includeBuilder, titleKeyword, keyword);

        builder.and(filterBuilder).and(includeBuilder);
        return builder;
    }

    // where predicate with tagIds and categoryIds
    private void categoryIn(BooleanBuilder builder, List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }
        builder.and(category.id.in(categoryIds));
    }

    private void tagIn(BooleanBuilder builder, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        builder.and(tag.id.in(tagIds));
    }

    private void categoryGroupIn(BooleanBuilder builder, List<Long> categoryGroupId) {
        if (categoryGroupId == null || categoryGroupId.isEmpty()) {
            return;
        }
        builder.and(categoryGroup.id.in(categoryGroupId));
    }

    private void categoryNameLike(BooleanBuilder builder, String categoryKeyword, String keyword) {
        if (categoryKeyword == null || categoryKeyword.isEmpty()) {
            isKeywordLike(builder, category.name, keyword);
            return;
        }
        builder.or(category.name.like(ProjectStringUtil.toStartLikeKeyword(categoryKeyword)));
    }

    private void categoryGroupNameLike(BooleanBuilder builder, String categoryGroupKeyword, String keyword) {
        if (categoryGroupKeyword == null || categoryGroupKeyword.isEmpty()) {
            isKeywordLike(builder, categoryGroup.name, keyword);
            return;
        }
        builder.or(categoryGroup.name.like(ProjectStringUtil.toStartLikeKeyword(categoryGroupKeyword)));
    }

    private void tagNameLike(BooleanBuilder builder, String tagKeyword, String keyword) {
        if (tagKeyword == null || tagKeyword.isEmpty()) {
            isKeywordLike(builder, tag.name, keyword);
            return;
        }
        builder.or(tag.name.like(ProjectStringUtil.toStartLikeKeyword(tagKeyword)));
    }

    private void magazineTitleLike(BooleanBuilder builder, String titleKeyword, String keyword) {
        if (titleKeyword == null || titleKeyword.isEmpty()) {
            isKeywordLike(builder, magazine.title, keyword);
            return;
        }
        builder.or(magazine.title.like(ProjectStringUtil.toStartLikeKeyword(titleKeyword)));
    }

    private void isKeywordLike(BooleanBuilder builder, StringPath path, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return;
        }
        builder.or(path.like(ProjectStringUtil.toStartLikeKeyword(keyword)));
    }

}
