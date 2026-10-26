package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.student.CourseStudentDto;
import com.example.demo.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	
	//search where age AND LIKE name
	@Query("Select s FROM Student s Where s.age >= :age AND s.name LIKE %:name%")
	List<Student> findByAge (@Param("age") Integer age, @Param("name") String name);

	//Order by
	@Query("SELECT s FROM Student s ORDER BY s.age ASC")
	List<Student> findAllOrderby ();
		
	//Order by + OR + WHERE
	@Query("SELECT s FROM Student s WHERE s.age < 20 OR s.age >20 ORDER BY s.age ASC")
	List<Student> findAllOrderbyOrWhere ();
	
//	Bài thực hành, yêu cầu: Tìm sinh viên có tên chứa từ khóa truyền vào, tuổi >= tuổi truyền vào, sau đó sắp xếp tuổi giảm dần.
	@Query("SELECT s FROM Student s WHERE s.name LIKE %:name% AND s.age >= :age ORDER BY s.age DESC")
	List<Student> baiThucHanh(@Param("name") String name, @Param("age") Integer age);
	
	//New Version for get all (partern).
		//query1: tao page student 
	@Query(value = "SELECT s.id FROM Student s WHERE (:name IS NULL OR s.name LIKE %:name%) AND (:minAge IS NULL OR s.age >= :minAge) AND (:maxAge IS NULL OR s.age <= :maxAge) AND (:idClass IS NULL OR s.classEntity.id = :idClass) AND (:nameClass IS NULL OR s.classEntity.name LIKE %:nameClass%)",
			countQuery = "SELECT count(s.id) FROM Student s WHERE (:name IS NULL OR s.name LIKE %:name%) AND (:minAge IS NULL OR s.age >= :minAge) AND (:maxAge IS NULL OR s.age <= :maxAge) AND (:idClass IS NULL OR s.classEntity.id = :idClass) AND (:nameClass IS NULL OR s.classEntity.name LIKE %:nameClass%)")
	Page<Long> filterStudents(
			@Param("name") String name,
			@Param("minAge") Integer minAge,
			@Param("maxAge") Integer maxAge,
			@Param("idClass") Long idClass,
			@Param("nameClass") String nameClass,
			Pageable pagination);
		//query2: join fetch courses
	@Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.classEntity LEFT JOIN FETCH s.profile LEFT JOIN FETCH s.courses WHERE s.id IN :ids")
	List<Student> FindAllStudentByIds(@Param("ids") List<Long> ids);
	
	
	//Entity Graph and To One after check log
	// com/example/demo/repository/StudentRepository.java

	@EntityGraph(
	    attributePaths = {"classEntity", "profile"},
	    type = EntityGraph.EntityGraphType.FETCH
	)
	@Query("SELECT s FROM Student s")
	List<Student> findAllWithToOneRelations();
	
	
	//day3: two query best pratice
	// Phase 1: Lấy Student kèm To-One (An toàn tuyệt đối với LIMIT/OFFSET)
    @EntityGraph(attributePaths = {"classEntity", "profile"})
    @Query("SELECT s FROM Student s")
    Page<Student> findAllWithToOne(Pageable pageable);

    // Phase 2: Lấy toàn bộ môn học của danh sách studentId
    @Query("""
        SELECT new com.example.demo.dto.CourseStudentDto(
            s.id, 
            c.id, 
            c.name, 
            c.description
        )
        FROM Student s
        JOIN s.courses c
        WHERE s.id IN :studentIds
    """)
    List<CourseStudentDto> findCoursesByStudentIds(@Param("studentIds") List<Long> studentIds);
	
	
	
	
}
