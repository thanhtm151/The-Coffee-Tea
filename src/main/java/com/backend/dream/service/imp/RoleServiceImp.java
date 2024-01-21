package com.backend.dream.service.imp;


import com.backend.dream.entity.Role;
import com.backend.dream.repository.RoleRepository;
import com.backend.dream.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public class RoleServiceImp implements RoleService {
	@Autowired
	RoleRepository roleRepository;


	@Override
	public List<Role> findALL() {
		return roleRepository.findAll();
	}

	@Override
	public Role findById(Long id) {
		return roleRepository.findById(id).get();
	}

	@Override
	public List<Role> findAllById(List<Long> targetRoleIds) {
		return roleRepository.findAllById(targetRoleIds);
	}
}
