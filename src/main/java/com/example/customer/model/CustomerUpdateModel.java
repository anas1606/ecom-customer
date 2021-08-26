package com.example.customer.model;

import com.example.commanentity.Customer;
import com.example.commanentity.CustomerAddress;
import com.example.commanentity.enums.Gender;
import lombok.Data;

import java.util.List;

@Data
public class CustomerUpdateModel {
    private String firstName;
    private String lastName;
    private String phoneno;
    private String address1;
    private String address2;
    private String country;
    private String gender;
    private String state;
    private String pincode;
    private List<String> hobby;

    public Customer getUpdatedCustomerFromModel(Customer c) {
        c.setFirst_name(firstName);
        c.setLast_name(lastName);
        c.setPhoneno(phoneno);
        c.setGender(Gender.valueOf(gender).getGender());
        return c;
    }

    public CustomerAddress getUpdatedCustomerAddressFromModel(CustomerAddress c) {
        c.setAddress1(address1);
        c.setAddress2(address2);
        c.setPincode(pincode);
        return c;
    }
}
