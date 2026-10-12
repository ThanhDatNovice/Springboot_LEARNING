package com.example.demo.dto.student;

import com.example.demo.dto.classEntity.ClassEntityBasicResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentInProfileResponse {

	private Long id;
	private String name;
	private Integer age;
	
	private ClassEntityBasicResponse classEntityResponse;
}
