package Degree_Flowchart.course_dataaccess.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Degree_Flowchart.course_dataaccess.data.entities.Course;
import Degree_Flowchart.course_dataaccess.data.entities.CourseOffering;
import Degree_Flowchart.course_dataaccess.data.entities.Term;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Long> {
    List<CourseOffering> findByTerm(Term term);
    Optional<CourseOffering> findByCourseAndTerm(Course course, Term term);
}
