package com.cigarettestore.repository;

import org.springframework.data.repository.CrudRepository;

import com.cigarettestore.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByname(String name);
}
