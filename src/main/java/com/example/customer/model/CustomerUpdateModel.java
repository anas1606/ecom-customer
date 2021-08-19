package com.example.customer.model;

import com.example.commanentity.Customer;
import com.example.commanentity.Customer_Address;
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
    private String state;
    private int pincode;
    private List<String> hobby;

    public Customer getUpdatedCustomerFromModel(Customer c) {
        c.setFirst_name(firstName);
        c.setLast_name(lastName);
        c.setPhoneno(phoneno);
        return c;
    }

    public Customer_Address getUpdatedCustomerAddressFromModel(Customer_Address c) {
        c.setAddress1(address1);
        c.setAddress2(address2);
        c.setPincode(pincode);
        return c;
    }
}
