package com.example.customer.model;


import com.example.commanentity.CustomerAddress;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {
    private String firstname;
    private String lastname;
    private String emailid;
    private String phoneno;
    private int gender;
    private String profile_url;
    private String address1;
    private String address2;
    private String country;
    private String state;
    private String pincode;
    private List<String> hobby;

    public CustomerDTO(CustomerAddress customerAddress) {
        this.firstname = customerAddress.getCustomer().getFirst_name();
        this.lastname = customerAddress.getCustomer().getLast_name();
        this.emailid = customerAddress.getCustomer().getEmailid();
        this.phoneno = customerAddress.getCustomer().getPhoneno();
        this.gender = customerAddress.getCustomer().getGender();
        this.profile_url = customerAddress.getCustomer().getProfile_url();
        this.address1 = customerAddress.getAddress1();
        this.address2 = customerAddress.getAddress2();
        this.country = customerAddress.getCountry().getName();
        this.state = customerAddress.getState().getName();
        this.pincode = customerAddress.getPincode();
    }
}
