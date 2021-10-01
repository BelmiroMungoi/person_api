package com.bbm.person.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbm.person.api.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	@Query("select u from Usuario u where u.userName = ?1")
	Usuario findByUserName(String userName);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update usuario set token = ?1 where user_name = ?2")
	void updateTokenUser(String token, String userName);
}
