package com.nfthub.core.response;

import lombok.Data;

@Data
public class MagazineImageResponse {
    private Long id;
    private String url;
    private Boolean isMain;
}
