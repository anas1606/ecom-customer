package com.example.customer.repository;

import com.example.commanentity.Customer_Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CustomerHobbyRepository extends JpaRepository<Customer_Hobby, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Customer_Hobby h WHERE h.customer.id = :id")
    void deleteByCustomer(String id);
}
