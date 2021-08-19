package com.example.customer.service;

import com.example.customer.model.*;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {

    ResponseModel login(LoginModel loginModel);

    ResponseModel registration(CustomerRegistrationModel model);

    ResponseModel verifyCustomer(VerificationModel model);

    ResponseModel viewProfile();

    ResponseModel updateCustomer(CustomerUpdateModel model);

    ResponseModel buyProduct(String productid);

    ResponseModel viewMyOrders();

    PageResponseModel homeFeed(PageDetailModel pageDetailModel);

    ResponseModel uploadProfile(MultipartFile file);
}
