package com.nfthub.core.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
public class MagazineRequest {
    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String url;
    private String author;
    private Long categoryId;
    private List<Long> tagIds;
}
