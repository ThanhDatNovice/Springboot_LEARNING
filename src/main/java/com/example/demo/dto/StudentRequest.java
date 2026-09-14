package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StudentRequest {
	
	@NotBlank(message = "Khong duoc de trong Ten")
	private String name;
	@Min(value = 18, message = "Tuoi phai tren 18")
	@Max(value = 120, message = "Tuoi phai duoi 120")
	private Integer age;
	
}
