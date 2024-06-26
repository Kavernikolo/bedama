package com.dama.cerbero.entities.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dama.cerbero.entities.TransactionTable;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionTable, Long>{
	List<TransactionTable> findByTransaction(String txid);
}
