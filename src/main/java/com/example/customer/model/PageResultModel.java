package com.example.customer.model;

import lombok.Data;

@Data
public class PageResultModel {
    private Long totalCount;
    private int pageno;
    private int pagecount;
}
