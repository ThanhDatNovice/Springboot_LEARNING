package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.profile.ProfileResponse;
import com.example.demo.service.ProfileService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/profiles")
public class ProfileController {
	
	private final ProfileService profileService;
	
	
	@GetMapping
	public Page<ProfileResponse> filterProfiles(
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) String address,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(defaultValue = "id") String sort,
			@RequestParam(defaultValue = "asc") String direction
			){
		return profileService.filterProfiles(email, phone, address, page, size, sort, direction);
	}

	@DeleteMapping("/{id}")
	public void deleteProfile(@PathVariable Long id) {
		
		profileService.deleteProfile(id);
	}
}
