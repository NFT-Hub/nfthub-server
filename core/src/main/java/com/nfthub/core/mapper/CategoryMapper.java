package com.nfthub.core.mapper;

import com.nfthub.core.entity.Category;
import com.nfthub.core.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper extends ResponseMapper<Category, CategoryResponse> {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
}
