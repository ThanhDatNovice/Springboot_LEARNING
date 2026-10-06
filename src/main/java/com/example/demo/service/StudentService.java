package com.example.demo.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.classEntity.ClassEntityBasicResponse;
import com.example.demo.dto.student.StudentRequest;
import com.example.demo.dto.student.StudentResponse;
import com.example.demo.entity.ClassEntity;
import com.example.demo.entity.Student;
import com.example.demo.exception.ClassEntityNotFoundException;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.repository.ClassEntityRepository;
import com.example.demo.repository.StudentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class StudentService {
	private final StudentRepository studentRepository;
	private final ClassEntityRepository classEntityRepository;
	
	
	
	//CRUD
	
	//CREATE
	public StudentResponse createStudent(StudentRequest studentRequest) {
		
		ClassEntity existClassEntity = null;
		
		if(studentRequest.getIdClass() != null)
			existClassEntity = classEntityRepository.findById(studentRequest.getIdClass())
				.orElseThrow(
					() -> new ClassEntityNotFoundException("Not Found Class Or Class No Exist")
				);
		
		Student student = new Student();
		
		student.setAge(studentRequest.getAge());
		student.setName(studentRequest.getName());
		student.setClassEntity(existClassEntity);
		
		Student savedStudent = studentRepository.save(student);

		StudentResponse response = new StudentResponse();
		response.setId(savedStudent.getId());
		response.setName(savedStudent.getName());
		response.setAge(savedStudent.getAge());
		
		if(existClassEntity != null) {
			response.setClassEntity(
					new ClassEntityBasicResponse(existClassEntity.getId(), existClassEntity.getName())
			);
		}
		return response;
	}
	
	//READ
	
	// + get all
	public Page<StudentResponse> getAllStudent(String name, Integer minAge, Integer maxAge, Long idClass, String nameClass, int page, int size, String sort, String direction){
		Set<String> allowFields = Set.of("age", "name", "id", "classEntity");
		
		if(!allowFields.contains(sort)) {
			sort = "id";
		}
		
		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")?Sort.Direction.DESC:Sort.Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
		
		Page<Student> students =  studentRepository.filterStudents(name, minAge, maxAge, idClass, nameClass, pageable);

		return students	.map(student -> {
					StudentResponse response = new StudentResponse();
					
					response.setName(student.getName());
					response.setId(student.getId());
					response.setAge(student.getAge());
					Optional.ofNullable(student.getClassEntity())
							.map(classEntity -> new ClassEntityBasicResponse(classEntity.getId(), classEntity.getName()))
							.ifPresent(classEntityResponse -> response.setClassEntity(classEntityResponse));
					return response;
				});
	}
	
	// + get by id
//	public StudentResponse getIdStudent(Long id) {
//		Student student = studentRepository.findById(id)
//				.orElseThrow(
//						() -> new StudentNotFoundException("Student not found with id: " + id)
//				);
//		StudentResponse response = new StudentResponse();
//		
//		response.setName(student.getName());
//		return response;		
//	}
//	
//	//get by age
//	public List<StudentResponse> findByAge(Integer age, String name){
//		List<Student> students= studentRepository.findByAge(age, name);
//
//		return students.stream()
//				.map(student -> new StudentResponse(student.getId(),student.getName(), student.getAge()))
//				.toList();
//	}
//	
//	//get all by ORDER BY age
//	public List<StudentResponse> findAllOrderby(){
//		List<Student> students = studentRepository.findAllOrderby();
//		return students.stream()
//				.map(student -> new StudentResponse(student.getId(), student.getName(), student.getAge()))
//				.toList();
//	}
	
	
	//UPDATE
	public StudentResponse updateStudent(Long idToFind, StudentRequest student) {
		Student existing = studentRepository.findById(idToFind)
				.orElseThrow(() -> 
					new StudentNotFoundException("Student not found with id : " + idToFind)
				);
		
		ClassEntity classEntityRequest = null;
		
		if(student.getIdClass() != null) {
			classEntityRequest = classEntityRepository.findById(student.getIdClass()).orElseThrow(
				() -> new ClassEntityNotFoundException("[!]Class khong ton tai.")	
			);
		}
		
		existing.setName(student.getName());
		existing.setAge(student.getAge());
		existing.setClassEntity(classEntityRequest);

		Student savedStudent = studentRepository.save(existing);
		
		StudentResponse studentResponse = new StudentResponse();
		studentResponse.setId(savedStudent.getId());
		studentResponse.setName(savedStudent.getName());
		studentResponse.setAge(savedStudent.getAge());
		if(savedStudent.getClassEntity() !=null) {
			studentResponse.setClassEntity(new ClassEntityBasicResponse(classEntityRequest.getId(), classEntityRequest.getName()));
		}
		
		return studentResponse;
	}
	
	public void deleteStudent(Long idToDel) {
		studentRepository.findById(idToDel)
				.orElseThrow(
						()-> new StudentNotFoundException("Student not found with id: " + idToDel)
				);
		studentRepository.deleteById(idToDel);
	}
	
	
}
