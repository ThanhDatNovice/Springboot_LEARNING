package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dto.classEntity.ClassEntityBasicResponse;
import com.example.demo.dto.course.CourseBasicResponse;
import com.example.demo.dto.profile.ProfileBasicResponse;
import com.example.demo.dto.student.StudentRequest;
import com.example.demo.dto.student.StudentResponse;
import com.example.demo.entity.ClassEntity;
import com.example.demo.entity.Course;
import com.example.demo.entity.Profile;
import com.example.demo.entity.Student;
import com.example.demo.exception.ClassEntityNotFoundException;
import com.example.demo.exception.CourseNotFoundException;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.repository.ClassEntityRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.StudentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class StudentService {
	private final StudentRepository studentRepository;
	private final ClassEntityRepository classEntityRepository;
	private final ProfileRepository profileRepository;
	private final CourseRepository courseRepository;
	
	
	
	//CRUD
	
	//CREATE
	public StudentResponse createStudent(StudentRequest studentRequest) {
		
		
		//classEntity co the null
		ClassEntity existClassEntity = null;
		
		if(studentRequest.getIdClass() != null)
			existClassEntity = classEntityRepository.findById(studentRequest.getIdClass())
				.orElseThrow(
					() -> new ClassEntityNotFoundException("Not Found Class Or Class No Exist")
				);
		
		//khoi tao pròile
		Profile profile = new Profile();
		
		Optional.ofNullable(studentRequest.getProfileRequest())
		.ifPresent(profileRequest -> {
			profile.setEmail(profileRequest.getEmail());
			profile.setPhone(profileRequest.getPhone());
			profile.setAddress(profileRequest.getAddress());	
		});
		
		
		//luu xuong db
		Profile savedProfile = profileRepository.save(profile);
		
		Student student = new Student();
		
		//set student với entity với profile
		student.setAge(studentRequest.getAge());
		student.setName(studentRequest.getName());
		student.setClassEntity(existClassEntity);
		student.setProfile(savedProfile);
		
		if(studentRequest.getIdCourses()!= null && !studentRequest.getIdCourses().isEmpty()) {
			for(Long idcourse : studentRequest.getIdCourses()) {
				Course course = courseRepository.findById(idcourse).orElseThrow(
						() -> new CourseNotFoundException("[!] Khong tim thay Course"));
				student.getCourses().add(course);
			}
		}
		
		//luu thong tin student xuong db và tra ve student có id
		Student savedStudent = studentRepository.save(student);

		//khoi tao student response
		StudentResponse response = new StudentResponse();
		response.setId(savedStudent.getId());
		response.setName(savedStudent.getName());
		response.setAge(savedStudent.getAge());
		
		//neu student k có class thì k can tra ve hoac tra null
		if(existClassEntity != null) {
			response.setClassEntity(
					new ClassEntityBasicResponse(existClassEntity.getId(), existClassEntity.getName())
			);
		}
		response.setProfile(new ProfileBasicResponse(
				savedStudent.getProfile().getId(),
				savedStudent.getProfile().getEmail(),
				savedStudent.getProfile().getPhone(),
				savedStudent.getProfile().getAddress()
				));
		
		if(!savedStudent.getCourses().isEmpty()) {
			response.setCourses(
					savedStudent.getCourses().stream()
												.map(course -> new CourseBasicResponse(course.getId(), course.getName(), course.getDescription()))
												.toList()
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
		

		//Query1
		Page<Long> pageIds = studentRepository.filterStudents(name, minAge, maxAge, idClass, nameClass, pageable);
		
		if(pageIds.isEmpty())	return Page.empty(pageable);
		
		List<Long> listIds = pageIds.getContent();
		
		List<Student> listStudents = studentRepository.FindAllStudentByIds(listIds);
		
		Map<Long, Student> hashmapStudents = new HashMap<>();
		for(Student s : listStudents) {
			hashmapStudents.put(s.getId(), s);
		}
		
		
		return pageIds.map(id -> {
					Student student = hashmapStudents.get(id);
					if(student == null)		return null;
					
					StudentResponse response = new StudentResponse();
					
					response.setName(student.getName());
					response.setId(student.getId());
					response.setAge(student.getAge());
					Optional.ofNullable(student.getProfile())
							.map(profile -> new ProfileBasicResponse(profile.getId(), profile.getEmail(), profile.getPhone(), profile.getAddress()))
							.ifPresent(profileBasic -> response.setProfile(profileBasic));
							
					Optional.ofNullable(student.getClassEntity())
							.map(classEntity -> new ClassEntityBasicResponse(classEntity.getId(), classEntity.getName()))
							.ifPresent(classEntityResponse -> response.setClassEntity(classEntityResponse));
							
					if(student.getCourses() != null && !student.getCourses().isEmpty()) {
						response.setCourses(
								student.getCourses().stream()
													.map(course -> new CourseBasicResponse(course.getId(), course.getName(), course.getDescription()))
													.toList()
						);
					}
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
		
		ClassEntity classEntityRequest = existing.getClassEntity();
		
		if(student.getIdClass() != null) {
			classEntityRequest = classEntityRepository.findById(student.getIdClass()).orElseThrow(
				() -> new ClassEntityNotFoundException("[!]Class khong ton tai.")	
			);
		}
		
		Profile profile = existing.getProfile();
		
		existing.setName(student.getName());
		existing.setAge(student.getAge());
		existing.setClassEntity(classEntityRequest);
		
		if(student.getProfileRequest()!= null) {
			// truong hop student da bi xoa profile -> student.getProfile =null nen phai tao new profile va save(profile)
			if(profile == null) {
				profile = new Profile();
				profile.setEmail(student.getProfileRequest().getEmail());
				profile.setPhone(student.getProfileRequest().getPhone());
				profile.setAddress(student.getProfileRequest().getAddress());
				
				// luc nay da co object profile co new id
				existing.setProfile(profileRepository.save(profile));
			}
			
			//truong hop nay k can vì profile dang exist va co id
			profile.setEmail(student.getProfileRequest().getEmail());
			profile.setPhone(student.getProfileRequest().getPhone());
			profile.setAddress(student.getProfileRequest().getAddress());
			
		}
		
		//setup course
		existing.getCourses().clear();
		if(student.getIdCourses() != null && !student.getIdCourses().isEmpty()) {
			for(Long idCourse : student.getIdCourses()) {
				Course course = courseRepository.findById(idCourse).orElseThrow(
						() -> new CourseNotFoundException("[!] Khong tim thay Course."));
				existing.getCourses().add(course);
			}
		}
		
		

		Student savedStudent = studentRepository.save(existing);
		
		StudentResponse studentResponse = new StudentResponse();
		studentResponse.setId(savedStudent.getId());
		studentResponse.setName(savedStudent.getName());
		studentResponse.setAge(savedStudent.getAge());
		if(savedStudent.getClassEntity() !=null) {
			studentResponse.setClassEntity(new ClassEntityBasicResponse(classEntityRequest.getId(), classEntityRequest.getName()));
		}
		if(savedStudent.getProfile()!= null) {
			studentResponse.setProfile(new ProfileBasicResponse(
					savedStudent.getProfile().getId(),
					savedStudent.getProfile().getEmail(),
					savedStudent.getProfile().getPhone(),
					savedStudent.getProfile().getAddress()
					));
		}
		if(!savedStudent.getCourses().isEmpty()) {
			studentResponse.setCourses(savedStudent.getCourses().stream()
																.map(course -> new CourseBasicResponse(course.getId(), course.getName(), course.getDescription()))
																.toList()
			);
		}
		
		
		
		return studentResponse;
	}
	
	
	//DELETE
	
	public void deleteStudent(Long idToDel) {
		
		Student student = studentRepository.findById(idToDel)
				.orElseThrow(
						()-> new StudentNotFoundException("Student not found with id: " + idToDel)
				);
		Profile profile = student.getProfile();
		if(profile != null) {
//			profile.setStudent(null);

//			student.setProfile(null);
		}
		if(!student.getCourses().isEmpty()) {
			for(Course course : student.getCourses()) {
				course.getStudents().remove(student);
			}
			student.getCourses().clear();
		}
		
		studentRepository.deleteById(idToDel);
	}
	
	
	
	//test entitygraph and to log
	public List<StudentResponse> getStudentsWithToOne() {
	    List<Student> students = studentRepository.findAllWithToOneRelations();
	    
	    return students.stream().map(student -> {
	        StudentResponse response = new StudentResponse();
	        response.setId(student.getId());
	        response.setName(student.getName());
	        response.setAge(student.getAge());

	        // Đã được nạp sẵn trong RAM qua @EntityGraph -> Không tốn SQL
	        Optional.ofNullable(student.getProfile())
	                .ifPresent(p -> response.setProfile(
	                        new ProfileBasicResponse(p.getId(), p.getEmail(), p.getPhone(), p.getAddress())
	                ));

	        // Đã được nạp sẵn trong RAM qua @EntityGraph -> Không tốn SQL
	        Optional.ofNullable(student.getClassEntity())
	                .ifPresent(c -> response.setClassEntity(
	                        new ClassEntityBasicResponse(c.getId(), c.getName())
	                ));

	        // CHÚ Ý NGHIỆM THU: Tạm thời KHÔNG gọi student.getCourses() ở bước này
	        // để kiểm chứng thuần túy hành vi của @EntityGraph với To-One.

	        return response;
	    }).toList();
	}
	
	
	//test batching
	// Thêm vào com/example/demo/service/StudentService.java

//	@Transactional(readOnly = true)
	public List<StudentResponse> verifyBatchSize40() {
	    // 1. Nạp 40 sinh viên lên RAM (Query 1)
	    Pageable pageable = PageRequest.of(0, 40, Sort.by("id").ascending());
	    Page<Student> studentPage = studentRepository.findAll(pageable);

	    // 2. Map sang DTO và kích hoạt nạp môn học con
	    return studentPage.getContent().stream().map(student -> {
	        StudentResponse res = new StudentResponse();
	        res.setId(student.getId());
	        res.setName(student.getName());
	        res.setAge(student.getAge());

	        // Kích hoạt collection courses
	        if (student.getCourses() != null) {
	            res.setCourses(
	                student.getCourses().stream()
	                    .map(c -> new CourseBasicResponse(c.getId(), c.getName(), c.getDescription()))
	                    .toList()
	            );
	        }
	        return res;
	    }).toList();
	}
	
}
