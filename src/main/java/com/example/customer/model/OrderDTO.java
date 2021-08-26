package com.example.customer.model;

import com.example.commanentity.OrderDetail;
import lombok.Data;

@Data
public class OrderDTO {
    private String oderId;
    private String productName;
    private String desc;
    private String price;
    private String orderDate;

    public OrderDTO(OrderDetail order) {
        this.oderId = order.getId();
        this.productName = order.getProduct().getName();
        this.desc = order.getProduct().getDescription();
        this.price = order.getProduct().getPrice().toString();
        this.orderDate = order.getCreated_at().toInstant().toString();
    }
}
