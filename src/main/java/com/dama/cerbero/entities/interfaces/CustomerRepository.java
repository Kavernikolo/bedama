package com.dama.cerbero.entities.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dama.cerbero.entities.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {}
