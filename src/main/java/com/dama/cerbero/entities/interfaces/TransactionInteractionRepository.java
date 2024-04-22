package com.dama.cerbero.entities.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dama.cerbero.entities.TransactionInteraction;

@Repository
public interface TransactionInteractionRepository extends CrudRepository<TransactionInteraction, Long>{
	List<TransactionInteraction> findByTransaction(String txid);
}
