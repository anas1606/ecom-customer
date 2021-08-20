package com.example.customer.controller;

import com.example.customer.model.*;
import com.example.customer.service.CustomerService;
import com.example.customer.util.CommanUtil;
import com.example.customer.util.Message;
import com.example.customer.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/customer/")
public class CustomerAuthController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAuthController.class);

    @Autowired
    private CustomerService customerService;

    @GetMapping("ping")
    public String ping() {
        return "pinging auth.... Success";
    }

    @PostMapping("login")
    public ResponseModel customerLogin(@RequestBody LoginModel loginModel) {
        return customerService.login(loginModel);
    }

    @PostMapping("register")
    public ResponseModel register(HttpServletRequest req, @RequestParam("data") String data, MultipartHttpServletRequest multipartRequest) {
        try {
            CustomerRegistrationModel model;
            model = ObjectMapperUtil.getObjectMapper().readValue(data, CustomerRegistrationModel.class);

            final MultipartFile[] image = {null};
            multipartRequest.getFileMap().entrySet().stream().forEach(e -> {
                switch (e.getKey()) {
                    case "profile":
                        image[0] = e.getValue();
                        break;
                    default:
                        logger.warn("Some thing Wrong Add new product image fetch");
                        break;
                }
            });
            model.setProfileURL(image[0]);
            return customerService.registration(model);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception {}", e.getMessage());
            return new CommanUtil().create(Message.SOMTHING_WRONG, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("upload/profile")
    public ResponseModel uploadProfile(HttpServletRequest req, MultipartHttpServletRequest multipartRequest) {
        try {
            final MultipartFile[] image = {null};
            multipartRequest.getFileMap().entrySet().stream().forEach(e -> {
                switch (e.getKey()) {
                    case "profile":
                        image[0] = e.getValue();
                        break;
                    default:
                        logger.warn("Some thing Wrong Add new product image fetch");
                        break;
                }
            });
            return customerService.uploadProfile(image[0]);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception {}", e.getMessage());
            return new CommanUtil().create(Message.SOMTHING_WRONG, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("verify")
    public ResponseModel verify(@RequestBody VerificationModel model) {
        return customerService.verifyCustomer(model);
    }

    @GetMapping("myprofile")
    public ResponseModel viewProfile() {
        return customerService.viewProfile();
    }

    @PutMapping("update")
    public ResponseModel updateCustomer(@RequestBody CustomerUpdateModel model) {
        return customerService.updateCustomer(model);
    }

    @PostMapping("buynow/{id}")
    public ResponseModel buyNow(@PathVariable("id") String id) {
        return customerService.buyProduct(id);
    }

    @GetMapping("myorders")
    public ResponseModel myOrders() {
        return customerService.viewMyOrders();
    }

    @PostMapping("feed")
    public PageResponseModel feed(@RequestBody PageDetailModel model) {
        return customerService.homeFeed(model);
    }
}
