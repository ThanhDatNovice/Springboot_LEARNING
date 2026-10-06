package com.example.demo.dto.classEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ClassEntityRequest {

	@NotBlank(message = "[!] Khong duoc de trong ten lop")
	private String name;
	
}
