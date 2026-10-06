package com.example.demo.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.classEntity.ClassEntityRequest;
import com.example.demo.dto.classEntity.ClassEntityResponse;
import com.example.demo.dto.student.StudentBasicResponse;
import com.example.demo.entity.ClassEntity;
import com.example.demo.exception.ClassEntityNotFoundException;
import com.example.demo.repository.ClassEntityRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClassEntityService {
	
	private final ClassEntityRepository classEntityRepository;


//	public ClassEntityService(StudentRepository studentRepository) {
//		this.studentRepository = studentRepository;
//	}

	
	//Create class
	public ClassEntityResponse createClass(ClassEntityRequest classEntityRequest) {
		
		ClassEntity newClass = new ClassEntity();
		newClass.setName(classEntityRequest.getName());
		
		ClassEntity savedClass = classEntityRepository.save(newClass);
		
		return new ClassEntityResponse(
			savedClass.getId(),
			savedClass.getName(),
			savedClass.getStudents().stream()
									.map(student -> new StudentBasicResponse(
											student.getId(),
											student.getName(),
											student.getAge()
										)
									)
									.toList()
		);
	}
	
	//READ
	public Page<ClassEntityResponse> filterClasses(String nameClass, String nameStudent, Integer page, Integer size, String sort, String direction){
		
		//kiem tra sort
		Set<String> allowFields = Set.of("id", "name");
		if(!allowFields.contains(sort))		sort = "id";
		
		//kiem
		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")? Sort.Direction.DESC: Sort.Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
		
		
		Page<ClassEntity> classEntity = classEntityRepository.filterClasses(nameClass, nameStudent, pageable);
		
		return classEntity.map(mapClass -> new ClassEntityResponse(
				mapClass.getId(),
				mapClass.getName(),
				mapClass.getStudents().stream()
										.map(mapStudent -> new StudentBasicResponse(mapStudent.getId(), mapStudent.getName(), mapStudent.getAge()))
										.toList()
				)
										
			);
	}
	
	//Update
	public ClassEntityResponse updateClassEntity (Long idClass, ClassEntityRequest classEntityRequest) {
		ClassEntity existing = classEntityRepository.findById(idClass).orElseThrow(
			() -> new ClassEntityNotFoundException("Khong tim thay Class id: " + idClass)
		);
		
		existing.setName(classEntityRequest.getName());
		ClassEntity savedClassEntity = classEntityRepository.save(existing);
		
		return new ClassEntityResponse(
			savedClassEntity.getId(),
			savedClassEntity.getName(),
			savedClassEntity.getStudents().stream()
											.map(student -> new StudentBasicResponse(student.getId(), student.getName(), student.getAge()))
											.toList()
		);
	}
	
	//Delete
	public void deleteClassEntity(Long id) {
		classEntityRepository.findById(id).orElseThrow(
			() -> new ClassEntityNotFoundException("Khong tim thay class co id: " + id)
		);
		classEntityRepository.deleteById(id);
	}
	
	
}
