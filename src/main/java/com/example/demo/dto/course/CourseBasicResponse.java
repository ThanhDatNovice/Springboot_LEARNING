package com.example.demo.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseBasicResponse {

	private Long id;
	private String name;
	private String description;

}
