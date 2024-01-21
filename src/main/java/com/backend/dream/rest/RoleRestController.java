package com.backend.dream.rest;


import com.backend.dream.entity.Role;
import com.backend.dream.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/roles")
public class RoleRestController {
	@Autowired
	RoleService roleService;

	@GetMapping()
	public List<Role> getAll() {
		List<String> targetRoleIdStrings  = Arrays.asList("1","2");
		List<Long> targetRoleIds = targetRoleIdStrings.stream()
				.map(Long::valueOf)
				.collect(Collectors.toList());
		return roleService.findAllById(targetRoleIds);
	}
}
