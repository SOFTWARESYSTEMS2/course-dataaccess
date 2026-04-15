package Degree_Flowchart.course_dataaccess.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Degree_Flowchart.course_dataaccess.data.entities.Term;

public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByLabel(String label);
    Optional<Term> findByActiveTrue();
    List<Term> findAllByOrderByIdAsc();
}
