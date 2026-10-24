package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long>  {

	@Query(
			value = "SELECT c FROM Course c LEFT JOIN c.students s LEFT JOIN s.profile p LEFT JOIN s.classEntity cl WHERE"
					+ "(:name IS NULL OR c.name LIKE CONCAT('%', :name, '%')) AND"
					+ "(:description IS NULL OR c.description LIKE CONCAT('%', :description, '%')) AND"
					+ "(:nameStudent IS NULL OR s.name LIKE CONCAT('%', :nameStudent, '%')) AND"
					+ "(:addressProfile IS NULL OR p.address LIKE CONCAT('%', :addressProfile, '%')) AND"
					+ "(:nameClassEntity IS NULL OR cl.name LIKE CONCAT('%', :nameClassEntity, '%'))",
			countQuery = "SELECT COUNT(DISTINCT(c.id)) FROM Course c LEFT JOIN c.students s LEFT JOIN s.profile p LEFT JOIN s.classEntity cl WHERE"
					+ "(:name IS NULL OR c.name LIKE CONCAT('%', :name, '%')) AND"
					+ "(:description IS NULL OR c.description LIKE CONCAT('%', :description, '%')) AND"
					+ "(:nameStudent IS NULL OR s.name LIKE CONCAT('%', :nameStudent, '%')) AND"
					+ "(:addressProfile IS NULL OR p.address LIKE CONCAT('%', :addressProfile, '%')) AND"
					+ "(:nameClassEntity IS NULL OR cl.name LIKE CONCAT('%', :nameClassEntity, '%'))"
			)
	Page<Course> filterCourses(
			@Param("name") String name,
			@Param("description") String description,
			@Param("nameStudent") String nameStudent,
			@Param("addressProfile") String addressProfile,
			@Param("nameClassEntity") String nameClassEntity,
			Pageable pageable
	);
}
