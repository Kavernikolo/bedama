package com.dama.cerbero.entities.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dama.cerbero.entities.MailTable;


@Repository
public interface MailRepository extends CrudRepository<MailTable, Long>{
	List<MailTable> findByEmail(String email);
}
