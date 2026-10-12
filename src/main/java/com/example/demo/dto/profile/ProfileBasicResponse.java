package com.example.demo.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileBasicResponse {
	private Long id;
	
	private String email;
	
	private String phone;
	
	private String address;
}
