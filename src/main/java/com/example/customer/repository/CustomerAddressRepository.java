package com.example.customer.repository;

import com.example.commanentity.Customer_Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaRepository<Customer_Address, String> {
    Customer_Address findByCustomer_Id(String customerid);
}
