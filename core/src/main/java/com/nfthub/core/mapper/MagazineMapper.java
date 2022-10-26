package com.nfthub.core.mapper;

import com.nfthub.core.entity.Magazine;
import com.nfthub.core.request.MagazineRequest;
import com.nfthub.core.response.MagazineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        CategoryMapper.class,
        TagMapper.class,
        MagazineImageMapper.class
})
public interface MagazineMapper extends ResponseMapper<Magazine, MagazineResponse>, RequestMapper<MagazineRequest, Magazine> {
    MagazineMapper INSTANCE = Mappers.getMapper(MagazineMapper.class);
}
