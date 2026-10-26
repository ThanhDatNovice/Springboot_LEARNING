package com.example.demo.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudentDto {
 private Long studentId;
 private Long courseId;
 private String courseName;
 private String courseDescription;
}
