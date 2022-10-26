package com.nfthub.core.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryGroupResponse {
    private Long id;
    private String name;
    private List<CategoryResponse> categories = new ArrayList<>();
}
