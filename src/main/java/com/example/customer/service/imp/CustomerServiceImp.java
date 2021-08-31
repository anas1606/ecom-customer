package com.example.customer.service.imp;

import com.example.commanentity.*;
import com.example.commanentity.enums.Status;
import com.example.customer.auth.JwtTokenUtil;
import com.example.customer.model.*;
import com.example.customer.repository.*;
import com.example.customer.service.CustomerService;
import com.example.customer.util.CommanUtil;
import com.example.customer.util.FileUpload;
import com.example.customer.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImp.class);
    @Value("${const.path}")
    private String folder;


    @Autowired
    private CommanUtil commanUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CustomerAddressRepository customerAddressRepository;
    @Autowired
    private HobbyRepository hobbyRepository;
    @Autowired
    private CustomerHobbyRepository customerHobbyRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public ResponseModel login(LoginModel loginModel) {
        ResponseModel responseModel;

        try {
            //Load the userdetail of the user where emailID = "Example@gmail.com"
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginModel.getEmailId(),
                            loginModel.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            Customer customer = customerRepository.findByEmailid(loginModel.getEmailId());
            final String token = jwtTokenUtil.generateToken(authentication);

            //set the Bearer token to AdminUser data
            customer.setSession_token(token);
            //update the adminuser to database with token And ExpirationDate
            customerRepository.save(customer);

            responseModel = commanUtil.create(Message.LOGIN_SUCCESS,
                    customer,
                    HttpStatus.OK);

        } catch (BadCredentialsException ex) {
            logger.info("Customer login BadCredentialsException ================== {}", ex.getMessage());
            responseModel = commanUtil.create(Message.LOGIN_ERROR,
                    null,
                    HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            logger.info("Customer Login Exception ================== {}", e.getMessage());
            responseModel = commanUtil.create(Message.DELETE_STATUS_ERROR,
                    null,
                    HttpStatus.BAD_REQUEST);
        }
        return responseModel;
    }

    @Override
    public ResponseModel registration(CustomerRegistrationModel model) {

//             check if user exist with email or username
        int exist = customerRepository.countByEmailid(model.getEmailId());
        if (exist == 0) {

            try {
//            get the Customer Object From DTO
                Customer customer = model.getCustomerFromModel();
                customer.setPassword(passwordEncoder.encode(model.getPassword()));
                String str = null;

//            Upload the image
                if (model.getProfileURL() != null)
                    str = new FileUpload().saveFile(folder, model.getProfileURL(), customer.getId());
                customer.setProfile_url(str);

                customer.setEmail_verification_otp(commanUtil.genrateRandomOTP());
                customerRepository.save(customer);

                insertAddress(customer, model);
                if (model.getHobby() != null)
                    insertHobby(customer, model.getHobby());
                commanUtil.sendVerificationEmail(customer.getEmailid(), customer.getEmail_verification_otp());

                return commanUtil.create(Message.CUSTOMER_REGISTER, customer.getId(), HttpStatus.OK);

            } catch (Exception e) {
                logger.error("Error Will Registration");
                e.printStackTrace();
                return commanUtil.create(Message.SOMTHING_WRONG, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return commanUtil.create(Message.EMAIL_EXIST, null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseModel uploadProfile(MultipartFile file) {
        //             check if user exist with email or username
        Customer customer = customerRepository.findByEmailid(commanUtil.getCurrentUserEmail());
        if (customer != null) {
            try {
                String str = null;

//            Upload the image
                if (file != null)
                    str = new FileUpload().saveFile(folder, file, customer.getId());
                customer.setProfile_url(str);
                customerRepository.save(customer);

                return commanUtil.create(Message.PROFILE_UPLODED, null, HttpStatus.OK);

            } catch (Exception e) {
                logger.error("Error Will upload Profile");
                e.printStackTrace();
                return commanUtil.create(Message.SOMTHING_WRONG, null, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return commanUtil.create(Message.EMAIL_EXIST, null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseModel verifyCustomer(VerificationModel model) {
        Customer customer = customerRepository.findById(model.getCustomerid()).orElse(null);
        if (customer != null) {
//            Check For OTP MATCH
            if (customer.getEmail_verification_otp().equals(model.getOtp())) {
                customer.setEmailverified(true);
                customer.setStatus(Status.ACTIVE.getStatus());
                customerRepository.save(customer);
                logger.info("Customer Verified");
                return commanUtil.create(Message.VERIFYED, null, HttpStatus.OK);
            } else {
                return commanUtil.create(Message.OTP_MISSMATCH, null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return commanUtil.create(Message.CUSTOMER_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseModel viewProfile() {
        Customer customer = customerRepository.findByEmailid(commanUtil.getCurrentUserEmail());
        if (customer != null) {
            CustomerAddress customerAddress = customerAddressRepository.findByCustomer_Id(customer.getId());
            CustomerDTO dto = new CustomerDTO(customerAddress);
            dto.setHobby(customerHobbyRepository.findAllByCustomer_Id(customer.getId()));

            return commanUtil.create(Message.SUCCESS, dto, HttpStatus.OK);
        } else {
            return commanUtil.create(Message.CUSTOMER_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseModel updateCustomer(CustomerUpdateModel model) {
        Customer customer = customerRepository.findByEmailid(commanUtil.getCurrentUserEmail());
        if (customer != null) {

//            Update the customer info And Save
            customer = model.getUpdatedCustomerFromModel(customer);
            customerRepository.save(customer);

//            Update the Customer Address
            UpdateAddress(customer, model);

//            update customer Hobby
            if (model.getHobby() != null)
                updateHobby(customer, model.getHobby());

            return commanUtil.create(Message.UPDATED, null, HttpStatus.OK);

        } else {
            return commanUtil.create(Message.CUSTOMER_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseModel buyProduct(String productid) {
        Customer customer = customerRepository.findByEmailid(commanUtil.getCurrentUserEmail());
        if (customer != null) {
            Product product = productRepository.findById(productid).orElse(null);
            if (product != null) {

                OrderDetail order = new OrderDetail();
                order.setCustomer(customer);
                order.setProduct(product);
                orderDetailRepository.save(order);

                logger.info("order Placed");
                return commanUtil.create(Message.ORDER_PLACED, null, HttpStatus.OK);
            } else {
                return commanUtil.create(Message.PRODUCT_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
        } else {
            return commanUtil.create(Message.CUSTOMER_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseModel viewMyOrders() {
        Customer customer = customerRepository.findByEmailid(commanUtil.getCurrentUserEmail());
        if (customer != null) {
            List<OrderDTO> orderDTO = orderDetailRepository.findByCustomer(customer.getId());
            return commanUtil.create(Message.ORDER_LIST, orderDTO, HttpStatus.OK);
        } else {
            return commanUtil.create(Message.CUSTOMER_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public PageResponseModel homeFeed(PageDetailModel pageDetailModel) {
        try {
            pageDetailModel = commanUtil.fillValueToPageModel(pageDetailModel);
            Pageable page = commanUtil.getPageDetail(pageDetailModel);
            Page<HomeFeedDTO> dto;
            String search = "%"+pageDetailModel.getSearch()+"%";
            if (commanUtil.checkNull(pageDetailModel.getCategory()))
                dto = productRepository.findAllPagable(search,page);
            else
                dto = productRepository.findAllByCategoryPagable(search,pageDetailModel.getCategory(), page);

            return commanUtil.create(Message.SUCCESS, dto.getContent(), commanUtil.pagersultModel(dto), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception Will getting HomeFeed");
            return commanUtil.create(Message.SOMTHING_WRONG, null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //    ***************************  Private Funtions ***************************************
    private void insertHobby(Customer customer, List<String> hobby) {
        logger.info("Inserting Hobby");
        hobby.forEach(h -> {
            CustomerHobby customerHobby = new CustomerHobby();
            customerHobby.setCustomer(customer);
            customerHobby.setHobby(hobbyRepository.findByName(h));
            customerHobbyRepository.save(customerHobby);
        });
    }

    private void insertAddress(Customer customer, CustomerRegistrationModel model) {
        logger.info("Inserting Address");
//        Get CustomerAddress object From DTO
        CustomerAddress customerAddress = model.getCustomerAddressFromModel();
        customerAddress.setCustomer(customer);
//        insert Country And State
        addCountryAndState(customerAddress, model.getCountry(), model.getState());
    }

    private void UpdateAddress(Customer customer, CustomerUpdateModel model) {
        logger.info("Updating Address");
//        Get CustomerAddress object From DTO
        CustomerAddress customerAddress = customerAddressRepository.findByCustomer_Id(customer.getId());
        customerAddress = model.getUpdatedCustomerAddressFromModel(customerAddress);
        customerAddress.setCustomer(customer);
//        insert Country And State
        addCountryAndState(customerAddress, model.getCountry(), model.getState());
    }

    private void addCountryAndState(CustomerAddress customerAddress, String countryName, String stateName) {
//        Fill the Country and State
        Country country = countryRepository.findByName(countryName);
        if (country != null)
            customerAddress.setCountry(country);
        State state = stateRepository.findByName(stateName);
        if (state != null)
            customerAddress.setState(state);

        customerAddressRepository.save(customerAddress);
    }

    private void updateHobby(Customer customer, List<String> hobby) {
        logger.info("Updating the Hobby");
//        Delete The Existing Hobby
        customerHobbyRepository.deleteByCustomer_Id(customer.getId());
//        insert The All Updated Hobby
        insertHobby(customer, hobby);
    }
}
