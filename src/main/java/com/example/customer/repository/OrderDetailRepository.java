package com.example.customer.repository;

import com.example.commanentity.Order_Detail;
import com.example.customer.model.OrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<Order_Detail, String> {

    @Query("SELECT new com.example.customer.model.OrderDTO(o) FROM Order_Detail o WHERE o.customer.id = :id AND o.status = 1 ORDER BY o.created_at DESC")
    List<OrderDTO> findByCustomer(String id);
}
