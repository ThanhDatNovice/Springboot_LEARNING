package com.example.demo.controller;

import java.util.List;

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

import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponse;
import com.example.demo.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {
	private final StudentService studentService;
	
	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}
	//CRUD
	
		//Create
	@PostMapping
	public StudentResponse createStudent(@Valid @RequestBody StudentRequest request) {
		return studentService.createStudent(request);
	}
	
		//Read
//	@GetMapping
//	public Page<StudentResponse> getAllStudent(@RequestParam int page, @RequestParam int size, @RequestParam String sort, @RequestParam String direction){
//		return studentService.getAllStudent(page, size, sort, direction);
//	}
	//New Version
	@GetMapping
	public Page<StudentResponse> getFilterStudents(
			@RequestParam (required = false) String name, 
			@RequestParam(required = false) Integer minAge, 
			@RequestParam(required = false) Integer maxAge, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "age") String Sort, 
			@RequestParam(defaultValue = "asc") String direction){
		return studentService.getAllStudent(name, minAge, maxAge, page, size, Sort, direction);
	}
	
	
	@GetMapping("/{id}")
	public StudentResponse getIdStudent(@PathVariable Long id) {
		return studentService.getIdStudent(id);
	}
	
	@GetMapping("/age")
	public List<StudentResponse> getAgeStudent(@RequestParam Integer age, @RequestParam String name){
		return studentService.findByAge(age, name);
	}
	@GetMapping("/orderby")
	public List<StudentResponse> getOrderbyAgeStudent(){
		return studentService.findAllOrderby();
	}
	
		//Update
	@PutMapping("/{id}")
	public StudentResponse updateStudent (@PathVariable Long id,@Valid @RequestBody StudentRequest student) {
		return studentService.updateStudent(id, student);
	}
	
		//Delete
	@DeleteMapping("/{id}")
	public void deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
	}
	
	
	
}
