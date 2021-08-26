package com.example.customer.model;

import com.example.commanentity.Customer;
import com.example.commanentity.CustomerAddress;
import com.example.commanentity.enums.Gender;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CustomerRegistrationModel {
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String gender;
    private String phoneno;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String pincode;
    private List<String> hobby;
    private MultipartFile profileURL;

    public Customer getCustomerFromModel() {
        Customer c = new Customer();
        c.setEmailid(emailId);
        c.setFirst_name(firstName);
        c.setLast_name(lastName);
        c.setPassword(password);
        c.setPhoneno(phoneno);
        c.setGender(Gender.valueOf(gender).getGender());
        return c;
    }

    public CustomerAddress getCustomerAddressFromModel() {
        CustomerAddress ca = new CustomerAddress();
        ca.setAddress1(address1);
        ca.setAddress2(address2);
        ca.setPincode(pincode);
        return ca;
    }
}
