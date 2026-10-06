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

import com.example.demo.dto.classEntity.ClassEntityRequest;
import com.example.demo.dto.classEntity.ClassEntityResponse;
import com.example.demo.service.ClassEntityService;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@RestController
@RequestMapping("/classes")
public class ClassEntityController {

	private final ClassEntityService classEntityService;
	
	//CREATE
	@PostMapping
	public ClassEntityResponse createClass(@RequestBody ClassEntityRequest classEntityRequest) {
		return classEntityService.createClass(classEntityRequest);
	}
	
	//READ
	@GetMapping
	public Page<ClassEntityResponse> filterClasses(
				@RequestParam(required = false) String nameClass,
				@RequestParam(required = false) String nameStudent,
				@RequestParam(defaultValue = "0") Integer page,
				@RequestParam(defaultValue = "5") Integer size,
				@RequestParam(defaultValue = "id") String sort,
				@RequestParam(defaultValue = "asc") String direction
			){
		return classEntityService.filterClasses(nameClass, nameStudent, page, size, sort, direction);
	}
	
	//UPDATE
	@PutMapping("/{id}")
	public ClassEntityResponse updateClassEntity(
			@PathVariable Long id,
			@RequestBody ClassEntityRequest classEntityRequest
			) {
		return classEntityService.updateClassEntity(id, classEntityRequest);
	}
	
	//DELETE
	@DeleteMapping("/{id}")
	public void deleteClassEntity(@PathVariable Long id) {
		classEntityService.deleteClassEntity(id);
	}
	
}
