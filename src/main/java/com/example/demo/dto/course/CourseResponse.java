package com.example.demo.dto.course;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.student.StudentInCourseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseResponse {

	private Long id;
	private String name;
	private String description;

	private List<StudentInCourseResponse> students = new ArrayList<>();
}
