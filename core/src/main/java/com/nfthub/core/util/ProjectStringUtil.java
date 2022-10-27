package com.nfthub.core.util;

public class ProjectStringUtil {
//    public static String toIncludeLikeKeyword(String keyword) {
//        return "%" + keyword + "%";
//    }

    public static String toStartLikeKeyword(String keyword) {
        return keyword + "%";
    }

    public static String toEndLikeKeyword(String keyword) {
        return "%" + keyword;
    }
}
