package com.example.demo.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProfileRequest {

	@NotBlank(message = "Khong duoc bo trong email")
	private String email;
	
	private String phone;
	
	private String address;
}
