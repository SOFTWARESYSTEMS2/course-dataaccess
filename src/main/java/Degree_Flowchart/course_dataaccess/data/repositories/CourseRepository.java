package Degree_Flowchart.course_dataaccess.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Degree_Flowchart.course_dataaccess.data.entities.Course;
import Degree_Flowchart.course_dataaccess.data.entities.Department;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
    List<Course> findByDepartment(Department department);
}
