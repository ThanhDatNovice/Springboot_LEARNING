package com.example.demo.dto.classEntity;

import java.util.List;

import com.example.demo.dto.student.StudentBasicResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClassEntityResponse {
	
	private Long id;
	
	private String name;
	
	private List<StudentBasicResponse> students;

}
