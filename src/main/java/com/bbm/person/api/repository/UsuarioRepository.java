package com.bbm.person.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bbm.person.api.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.userName = ?1")
	Usuario findByUserName(String userName);

	@Query("select p from Usuario p where upper(trim(p.fullName)) like %?1%")
	List<Usuario> findByName(String name);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update usuario set token = ?1 where user_name = ?2")
	void updateTokenUser(String token, String userName);

	@Query(nativeQuery = true, value = "select constraint_name from information_schema.constraint_column_usage "
			+ "where table_name = 'usuario_roles' and column_name = 'role_id' "
			+ "and constraint_name <> 'unique_role_user';")
	String consultaConstraint();

	/*
	 * @Modifying
	 * 
	 * @Query(nativeQuery = true, value =
	 * "alter table usuario_roles drop constraint ?1") void removeConstraint(String
	 * constraint);
	 */

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "insert into usuario_roles(usuario_id, role_id)"
			+ "values(?1, (select id from role where name_role = 'ROLE_USER'))")
	void insereAcessoPadrao(Long id);
}
