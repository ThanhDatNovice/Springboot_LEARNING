package com.example.demo.dto.student;

import com.example.demo.dto.classEntity.ClassEntityBasicResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
	private Long id;
	private String name;
	private Integer age;
	
	private ClassEntityBasicResponse classEntity;
}
