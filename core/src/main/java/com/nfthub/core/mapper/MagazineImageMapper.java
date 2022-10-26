package com.nfthub.core.mapper;

import com.nfthub.core.entity.MagazineImage;
import com.nfthub.core.response.MagazineImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MagazineImageMapper extends ResponseMapper<MagazineImage, MagazineImageResponse> {
    MagazineImageMapper INSTANCE = Mappers.getMapper(MagazineImageMapper.class);
}
