package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {
	private final StudentRepository studentRepository;
	
	//constructor
	public StudentService (StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
	
	//CRUD
	
	//CREATE
	public StudentResponse createStudent(StudentRequest studentRequest) {
		
		Student student = new Student();
		
		student.setAge(studentRequest.getAge());
		student.setName(studentRequest.getName());
		
		Student savedStudent = studentRepository.save(student);

		StudentResponse response = new StudentResponse();
		response.setName(savedStudent.getName());
		return response;
	}
	
	//READ
	
	// + get all
	public Page<StudentResponse> getAllStudent(int page, int size, String sort, String direction){
		
		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")?Sort.Direction.DESC:Sort.Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
		
		Page<Student> students =  studentRepository.findAll(pageable);

		return students	.map(student -> {
					StudentResponse response = new StudentResponse();
					
					response.setName(student.getName());
					response.setId(student.getId());
					response.setAge(student.getAge());
					
					return response;
				});
	}
	
	// + get one
	public StudentResponse getIdStudent(Long id) {
		Student student = studentRepository.findById(id)
				.orElseThrow(
						() -> new StudentNotFoundException("Student not found with id: " + id)
				);
		StudentResponse response = new StudentResponse();
		
		response.setName(student.getName());
		return response;		
	}
	
	//UPDATE
	public StudentResponse updateStudent(Long idToFind, StudentRequest student) {
		Student existing = studentRepository.findById(idToFind)
				.orElseThrow(() -> 
					new StudentNotFoundException("Student not found with id : " + idToFind)
				);
		
		existing.setName(student.getName());
		existing.setAge(student.getAge());
		
		studentRepository.save(existing);
		
		
		 return new StudentResponse(existing.getId(), existing.getName(), existing.getAge());
	}
	
	public void deleteStudent(Long idToDel) {
		studentRepository.findById(idToDel)
				.orElseThrow(
						()-> new StudentNotFoundException("Student not found with id: " + idToDel)
				);
		studentRepository.deleteById(idToDel);
	}
	
	
}
