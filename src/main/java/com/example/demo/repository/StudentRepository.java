package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	@Query("SELECT s FROM Student s WHERE (:name IS NULL OR s.name LIKE %:name%) AND (:minAge IS NULL OR s.age >= :minAge) AND (:maxAge IS NULL OR s.age <= :maxAge)")
	Page<Student> filterStudents(@Param("name") String name, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge, Pageable pagination);
	
	
	
	
}
