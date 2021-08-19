package com.example.customer.repository;

import com.example.commanentity.Product;
import com.example.customer.model.HomeFeedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT new com.example.customer.model.HomeFeedDTO(p) FROM Product p WHERE p.status = 1")
    Page<HomeFeedDTO> findAllPagable(Pageable page);
}
