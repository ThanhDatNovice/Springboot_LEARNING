package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.ClassEntity;

public interface ClassEntityRepository extends JpaRepository<ClassEntity, Long> {

	@Query(
			value = "SELECT DISTINCT c FROM ClassEntity c LEFT JOIN c.students s WHERE "
					+ "(:nameClass IS NULL OR c.name LIKE CONCAT('%', :nameClass, '%')) AND "
					+ "(:nameStudent IS NULL OR s.name Like CONCAT ('%', :nameStudent, '%'))",
			countQuery = "SELECT COUNT(DISTINCT c.id) FROM ClassEntity c LEFT JOIN c.students s WHERE"
					+ "(:nameClass IS NULL OR c.name LIKE CONCAT('%', :nameClass,'%')) AND"
					+ "(:nameStudent IS NULL OR s.name LIKE CONCAT('%', :nameStudent, '%'))"
	)
	Page<ClassEntity> filterClasses(
		@Param("nameClass") String nameClass,
		@Param("nameStudent") String nameStudent,
		Pageable pageable
	);
}
