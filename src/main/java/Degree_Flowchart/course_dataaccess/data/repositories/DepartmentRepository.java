package Degree_Flowchart.course_dataaccess.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Degree_Flowchart.course_dataaccess.data.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
