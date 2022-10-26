package com.nfthub.core.mapper;

import com.nfthub.core.entity.CategoryGroup;
import com.nfthub.core.response.CategoryGroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CategoryMapper.class})
public interface CategoryGroupMapper extends ResponseMapper<CategoryGroup, CategoryGroupResponse> {
    public static CategoryGroupMapper INSTANCE = Mappers.getMapper(CategoryGroupMapper.class);
}
