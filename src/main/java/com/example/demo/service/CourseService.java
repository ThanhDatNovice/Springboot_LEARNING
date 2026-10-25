package com.example.demo.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.classEntity.ClassEntityBasicResponse;
import com.example.demo.dto.course.CourseBasicResponse;
import com.example.demo.dto.course.CourseRequest;
import com.example.demo.dto.course.CourseResponse;
import com.example.demo.dto.profile.ProfileBasicResponse;
import com.example.demo.dto.student.StudentInCourseResponse;
import com.example.demo.entity.Course;
import com.example.demo.entity.Student;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.repository.CourseRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CourseService {

	private final CourseRepository courseRepository;




	
	//CREATE
	public CourseBasicResponse createCourse(CourseRequest courseRequest) {
		//nhan vao thong tin request
		Course courseInput = new Course();
		courseInput.setName(courseRequest.getName());
		courseInput.setDescription(courseRequest.getDescription());
		
		Course savedCourse = courseRepository.save(courseInput);
		return new CourseBasicResponse(savedCourse.getId(), savedCourse.getName(), savedCourse.getDescription());
	}
	
	//READ
	public Page<CourseResponse> filterCourses(
			String name,
			String description,
			String nameStudent,
			String addProfile,
			String nameClassEntity,
			Integer page,
			Integer size,
			String sort,
			String direction
	){
		
		//set up sort and direction -->
		Set<String> allowFields = Set.of("id", "name");
		if(!allowFields.contains(sort)) {
			sort = "id";
		}
		
		Sort.Direction sortDirection = (direction.equalsIgnoreCase("desc"))? Sort.Direction.DESC: Sort.Direction.ASC;
		//<--
		
		//create pageable
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
		
		Page<Course> courses = courseRepository.filterCourses(name, description, nameStudent, addProfile, nameClassEntity, pageable);
		
		return courses.map(course -> new CourseResponse(
				course.getId(),
				course.getName(),
				course.getDescription(),
				course.getStudents().stream()
									.map(student -> new StudentInCourseResponse(
											student.getId(),
											student.getName(),
											student.getAge(),
											Optional.ofNullable(student.getProfile())
													.map(profile -> new ProfileBasicResponse(
															profile.getId(),
															profile.getEmail(),
															profile.getPhone(),
															profile.getAddress()
													))
													.orElse(null),
											Optional.ofNullable(student.getClassEntity())
													.map(classEntity -> new ClassEntityBasicResponse(
															classEntity.getId(),
															classEntity.getName()
													))
													.orElse(null)
									))
									.toList()
		));
	}
	
	//UPDATE
	
	public CourseBasicResponse updateCourse(Long id, CourseRequest courseRequest) {

		//kiem tra ton tai
		Course existing = courseRepository.findById(id).orElseThrow(
				() -> new CourseNotFoundException("[!]Khong tim thay Course"));

		existing.setName(courseRequest.getName());
		existing.setDescription(courseRequest.getDescription());
		
		Course savedCourse = courseRepository.save(existing);
		
		return new CourseBasicResponse(savedCourse.getId(), savedCourse.getName(), savedCourse.getDescription());
	}
	
	//DELETE
	public void deleteCourse(Long id) {
		Course existing = courseRepository.findById(id).orElseThrow(
				() -> new CourseNotFoundException("[!]Khong tim thay Course"));
		
		// Cach 1: get student va foreach vao student
//		existing.getStudents().forEach(student -> student.getCourses().remove(existing));
	
		//Cach 2: dung for di vao student lay được từ method reference existing.getStudent()
		for(Student student : existing.getStudents()) {
			student.getCourses().remove(existing);
		}
		
		existing.getStudents().clear();
		courseRepository.delete(existing);
	}
	
	
}
