package com.example.customer.repository;

import com.example.commanentity.OrderDetail;
import com.example.customer.model.OrderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    @Query("SELECT new com.example.customer.model.OrderDTO(o) FROM OrderDetail o WHERE o.customer.id = :id AND o.status = 1 ORDER BY o.created_at DESC")
    List<OrderDTO> findByCustomer(String id);
}
