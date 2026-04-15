package Degree_Flowchart.course_dataaccess.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Degree_Flowchart.course_dataaccess.data.entities.Course;
import Degree_Flowchart.course_dataaccess.data.entities.Prerequisite;

public interface PrerequisiteRepository extends JpaRepository<Prerequisite, Long> {
    List<Prerequisite> findByCourse(Course course);

    // Fetch all prerequisites for a batch of courses in one query — avoids N+1
    @Query("SELECT p FROM Prerequisite p WHERE p.course IN :courses")
    List<Prerequisite> findByCourseIn(@Param("courses") List<Course> courses);
}
