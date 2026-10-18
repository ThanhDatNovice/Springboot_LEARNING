package com.example.demo.dto.student;

import com.example.demo.dto.classEntity.ClassEntityBasicResponse;
import com.example.demo.dto.profile.ProfileBasicResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class StudentInCourseResponse {
	private Long id;
	private String name;
	private Integer age;
	
	private ProfileBasicResponse profile;
	private ClassEntityBasicResponse classEntity;
}
