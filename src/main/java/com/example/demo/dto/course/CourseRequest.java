package com.example.demo.dto.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CourseRequest {
	
	@NotBlank(message = "khong duoc de trong ten")
	private String name;
	
	private String description;

}
