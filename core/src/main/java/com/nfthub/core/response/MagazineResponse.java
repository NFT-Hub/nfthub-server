package com.nfthub.core.response;

import lombok.Data;

import java.util.List;

@Data
public class MagazineResponse {
    private Long id;
    private String title;
    private String description;
    private String url;
    private CategoryResponse category;
    private List<TagResponse> tags;
    private List<MagazineImageResponse> images;
}
