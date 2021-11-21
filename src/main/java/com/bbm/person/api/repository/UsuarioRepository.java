package com.bbm.person.api.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	Page<Usuario> findByName(String name, PageRequest pageRequest);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update usuario set token = ?1 where user_name = ?2")
	void updateTokenUser(String token, String userName);

	@Query(nativeQuery = true, value = "select constraint_name from "
			+ "information_schema.constraint_column_usage "
			+ "where table_name = 'usuario_roles' and column_name = 'role_id' "
			+ "and constraint_name <> 'unique_role_user';")
	String consultaConstraint();

	@Modifying
	@Query(nativeQuery = true, value = "alter table usuario_roles drop constraint ?1")
	void removeConstraint(String constraint);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "insert into usuario_roles(usuario_id, role_id)"
			+ "values(?1, (select id from role where name_role = 'ROLE_USER'))")
	void insereAcessoPadrao(Long id);

	default Page<Usuario> findByNamePerPage(String nome, PageRequest pageRequest){
		
		Usuario usuario = new Usuario();
		usuario.setFullName(nome);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("fullName",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		// Une o objecto com o valor e configura a consulta
		Example<Usuario> example = Example.of(usuario, exampleMatcher);

		Page<Usuario> usuarios = findAll(example, pageRequest);

		return usuarios;
	}
	

}
