package com.example.demo.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.profile.ProfileResponse;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Service
	public class ProfileService {
		
		private final ProfileRepository profileRepository;		
		
		//READ
		public Page<ProfileResponse> filterProfiles(String email, String phone, String address, Integer page, Integer size, String sort, String direction){
			
			//xy ly sort
			Set<String> allowFields = Set.of("id", "email", "address");
			
			if(!allowFields.contains(sort)) {
				sort = "id";
			}
			
			//xy ly direction
			Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")? Sort.Direction.DESC:Sort.Direction.ASC;
			
			//xy ly page
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
			
			Page<Profile> profiles = profileRepository.filterProfiles(email, phone, address, pageable);
			
			return profiles.map(profile ->
			        new ProfileResponse(
			                profile.getId(),
			                profile.getEmail(),
			                profile.getPhone(),
			                profile.getAddress()
			        )
			);
		}
		
		
		//DELETE
		public void deleteProfile(Long id) {
//			Profile profile = profileRepository.findById(id).orElse(null);			
			profileRepository.deleteById(id);
		}
		
	}
