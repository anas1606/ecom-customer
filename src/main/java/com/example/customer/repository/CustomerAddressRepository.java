package com.example.customer.repository;

import com.example.commanentity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, String> {
    CustomerAddress findByCustomer_Id(String customerid);
}
