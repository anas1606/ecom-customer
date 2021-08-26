package com.example.customer.repository;

import com.example.commanentity.CustomerHobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CustomerHobbyRepository extends JpaRepository<CustomerHobby, String> {

    @Query("SELECT h.hobby.name FROM CustomerHobby h WHERE h.customer.id = :id")
    List<String> findAllByCustomer_Id (String id);
    @Transactional
    @Modifying
    void deleteByCustomer_Id(String id);
}
