package com.example.demo.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.classEntity.ClassEntityBasicResponse;
import com.example.demo.dto.profile.ProfileResponse;
import com.example.demo.dto.student.StudentInProfileResponse;
import com.example.demo.entity.Profile;
import com.example.demo.entity.Student;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.StudentRepository;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Service
	public class ProfileService {
		
		private final ProfileRepository profileRepository;
		private final StudentRepository studentRepository;
		
		
		//READ
		public Page<ProfileResponse> filterProfiles(String email, String phone, String address, Integer minAgeStudent, Integer maxAgeStudent, String nameClass, Integer page, Integer size, String sort, String direction){
			
			//xy ly sort
			Set<String> allowFields = Set.of("id", "email", "address");
			
			if(!allowFields.contains(sort)) {
				sort = "id";
			}
			
			//xy ly direction
			Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")? Sort.Direction.DESC:Sort.Direction.ASC;
			
			//xy ly page
			Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
			
			Page<Profile> profiles = profileRepository.filterProfiles(email, phone, address, minAgeStudent ,maxAgeStudent, nameClass, pageable);
			
			return profiles.map(profile ->
			        new ProfileResponse(
			                profile.getId(),
			                profile.getEmail(),
			                profile.getPhone(),
			                profile.getAddress(),
			                Optional.ofNullable(profile.getStudent())
			                        .map(student -> new StudentInProfileResponse(
			                                student.getId(),
			                                student.getName(),
			                                student.getAge(),
			                                Optional.ofNullable(student.getClassEntity())
			                                        .map(classEntity -> new ClassEntityBasicResponse(
			                                                classEntity.getId(),
			                                                classEntity.getName()
			                                        ))
			                                        .orElse(null)
			                        ))
			                        .orElse(null)
			        )
			);
		}
		
		
		//DELETE
		public void deleteProfile(Long id) {
			Profile profile = profileRepository.findById(id).orElse(null);
			
			Student student = profile.getStudent();
			if(student != null) {
				student.setProfile(null);
				studentRepository.save(student);
				
			}
			profileRepository.deleteById(id);
		}
		
	}
