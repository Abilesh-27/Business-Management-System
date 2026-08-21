package com.jp.business.service;

import com.jp.business.dto.CustomerRequest;
import com.jp.business.entity.Customer;
import com.jp.business.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(
            CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CustomerRequest request) {

        String phone = request.getPhone().trim();

        if (customerRepository.existsByPhone(phone)) {
            throw new RuntimeException(
                    "Customer with this phone number already exists"
            );
        }

        Customer customer = new Customer();

        customer.setName(request.getName().trim());
        customer.setCompanyName(request.getCompanyName());
        customer.setPhone(phone);
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setDistrict(request.getDistrict());
        customer.setState(request.getState());
        customer.setPincode(request.getPincode());

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found"
                        )
                );
    }

    public Customer updateCustomer(
            Long id,
            CustomerRequest request
    ) {

        Customer customer = getCustomerById(id);

        customer.setName(request.getName().trim());
        customer.setCompanyName(request.getCompanyName());
        customer.setPhone(request.getPhone().trim());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setDistrict(request.getDistrict());
        customer.setState(request.getState());
        customer.setPincode(request.getPincode());

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {

        Customer customer = getCustomerById(id);

        customer.setActive(false);

        customerRepository.save(customer);
    }
}