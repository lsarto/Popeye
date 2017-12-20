package com.popeyestore.repository;

import org.springframework.data.repository.CrudRepository;

import com.popeyestore.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByname(String name);
}
