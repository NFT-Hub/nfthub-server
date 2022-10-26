package com.nfthub.core.mapper;

import com.nfthub.core.entity.Tag;
import com.nfthub.core.response.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper extends ResponseMapper<Tag, TagResponse> {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);
}
