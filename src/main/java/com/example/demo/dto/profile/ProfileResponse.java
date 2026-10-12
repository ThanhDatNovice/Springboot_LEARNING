package com.example.demo.dto.profile;

import com.example.demo.dto.student.StudentInProfileResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProfileResponse {

	private Long id;
	
	private String email;
	
	private String phone;
	
	private String address;
	
	private StudentInProfileResponse studentInProfileResponse;
	
}
