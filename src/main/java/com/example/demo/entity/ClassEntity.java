	package com.example.demo.entity;
	
	import java.util.ArrayList;
	import java.util.List;
	
	import jakarta.persistence.Entity;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.OneToMany;
	import lombok.AllArgsConstructor;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Entity
	public class ClassEntity {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String name;
		
		@OneToMany(mappedBy = "classEntity")
		private List<Student> students = new ArrayList<>() ;
	}
