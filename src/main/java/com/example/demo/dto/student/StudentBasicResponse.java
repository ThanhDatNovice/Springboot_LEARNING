package com.example.demo.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentBasicResponse {

	private Long id;
	
	private String name;
	
	private Integer age;
}
