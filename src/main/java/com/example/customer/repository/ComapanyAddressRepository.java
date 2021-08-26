package com.example.customer.repository;

import com.example.commanentity.CompanyAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComapanyAddressRepository extends JpaRepository<CompanyAddress, String> {
}
