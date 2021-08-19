package com.example.customer.model;

import com.example.commanentity.Product;
import lombok.Data;

@Data
public class HomeFeedDTO {
    private String productid;
    private String productName;
    private String desc;
    private String price;
    private String category;
    private String companyName;

    public HomeFeedDTO(Product product) {
        this.productid = product.getId();
        this.productName = product.getName();
        this.desc = product.getDescription();
        this.price = product.getPrice().toString();
        this.category = product.getCategory().getName();
        this.companyName = product.getVendor().getCompany_name();
    }
}
