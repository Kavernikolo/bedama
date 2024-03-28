package com.dama.cerbero.entities.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dama.cerbero.entities.Subscriber;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {
	List<Subscriber> findByAddress(String address);
}
