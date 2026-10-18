package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.course.CourseBasicResponse;
import com.example.demo.dto.course.CourseRequest;
import com.example.demo.dto.course.CourseResponse;
import com.example.demo.service.CourseService;

import lombok.AllArgsConstructor;

@AllArgsConstructor	
@RestController
@RequestMapping("/courses")
public class CourseController {

	private final CourseService courseService;
	
	@PostMapping
	public CourseBasicResponse CourseBasicResponse(@RequestBody CourseRequest courseRequest) {
		return courseService.createCourse(courseRequest);
	}
	
	@GetMapping
	public Page<CourseResponse> filterCourses(
			@RequestParam (required = false) String name,
			@RequestParam (required = false) String description,
			@RequestParam (required = false) String nameStudent,
			@RequestParam (required = false) String addressProfile,
			@RequestParam (required = false) String nameClassEntity,
			@RequestParam (defaultValue = "0") Integer page,
			@RequestParam (defaultValue = "5") Integer size,
			@RequestParam (defaultValue = "id") String sort,
			@RequestParam (defaultValue = "asc") String direction
	){
		return courseService.filterCourses(name, description, nameStudent, addressProfile, nameClassEntity, page, size, sort, direction);
	}
	
	@PutMapping("/{id}")
	public CourseBasicResponse updateCourse(@PathVariable Long id, @RequestBody CourseRequest courseRequest) {
		return courseService.updateCourse(id, courseRequest);
	}
	
	@DeleteMapping("/{id}")
	public void deleteCourse(@PathVariable Long id) {
		courseService.deleteCourse(id);
	}
}
