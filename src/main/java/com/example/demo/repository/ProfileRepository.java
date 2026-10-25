package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{

	@Query(
			value = " SELECT p FROM Profile p  WHERE"
					+ "(:email IS NULL OR p.email LIKE CONCAT('%', :email, '%')) AND"
					+ "(:phone IS NULL OR p.phone LIKE CONCAT('%', :phone, '%')) AND"
					+ "(:address IS NULL OR p.address LIKE CONCAT('%', :address, '%'))"
					,
			countQuery = " SELECT count (p.id) FROM Profile p WHERE"
					+ "(:email IS NULL OR p.email LIKE CONCAT('%', :email, '%')) AND"
					+ "(:phone IS NULL OR p.phone LIKE CONCAT('%', :phone, '%')) AND"
					+ "(:address IS NULL OR p.address LIKE CONCAT('%', :address, '%'))"
	)
	Page<Profile> filterProfiles(
			@Param("email") String email,
			@Param("phone") String phone,
			@Param("address") String address,
			Pageable pageable
			);
}
