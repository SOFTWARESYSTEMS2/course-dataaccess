package Degree_Flowchart.course_dataaccess.services;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Degree_Flowchart.course_dataaccess.data.entities.Course;
import Degree_Flowchart.course_dataaccess.data.entities.CourseOffering;
import Degree_Flowchart.course_dataaccess.data.entities.Prerequisite;
import Degree_Flowchart.course_dataaccess.data.repositories.CourseOfferingRepository;
import Degree_Flowchart.course_dataaccess.data.repositories.CourseRepository;
import Degree_Flowchart.course_dataaccess.data.repositories.PrerequisiteRepository;
import Degree_Flowchart.course_dataaccess.data.repositories.TermRepository;
import Degree_Flowchart.course_dataaccess.dto.CourseDto;
import Degree_Flowchart.course_dataaccess.dto.CourseOfferingDto;
import Degree_Flowchart.course_dataaccess.dto.PrerequisiteDto;

@Service
@Transactional(readOnly = true)
public class CourseDataAccessService {

    private final CourseRepository courseRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    private final PrerequisiteRepository prerequisiteRepository;
    private final TermRepository termRepository;

    public CourseDataAccessService(
            CourseRepository courseRepository,
            CourseOfferingRepository courseOfferingRepository,
            PrerequisiteRepository prerequisiteRepository,
            TermRepository termRepository) {
        this.courseRepository = courseRepository;
        this.courseOfferingRepository = courseOfferingRepository;
        this.prerequisiteRepository = prerequisiteRepository;
        this.termRepository = termRepository;
    }

    public Optional<CourseDto> findCourseByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        return courseRepository.findByCode(normalize(code))
                .map(this::toCourseDto);
    }

    public List<CourseOfferingDto> findOfferingsByTermLabel(String termLabel) {
        if (termLabel == null || termLabel.isBlank()) {
            return Collections.emptyList();
        }

        return termRepository.findByLabel(termLabel)
                .map(courseOfferingRepository::findByTerm)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::toOfferingDto)
                .toList();
    }

    public List<CourseOfferingDto> findOfferingsForActiveTerm() {
        return termRepository.findByActiveTrue()
                .map(courseOfferingRepository::findByTerm)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::toOfferingDto)
                .toList();
    }

    public Optional<CourseOfferingDto> findOffering(String courseCode, String termLabel) {
        if (courseCode == null || courseCode.isBlank() || termLabel == null || termLabel.isBlank()) {
            return Optional.empty();
        }

        return courseRepository.findByCode(normalize(courseCode))
                .flatMap(course -> termRepository.findByLabel(termLabel)
                        .flatMap(term -> courseOfferingRepository.findByCourseAndTerm(course, term)))
                .map(this::toOfferingDto);
    }

    public List<PrerequisiteDto> findPrerequisitesByCourseCode(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            return Collections.emptyList();
        }

        return courseRepository.findByCode(normalize(courseCode))
                .map(prerequisiteRepository::findByCourse)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::toPrerequisiteDto)
                .toList();
    }

    private CourseDto toCourseDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getCode(),
                course.getTitle(),
                course.getCredits(),
                course.isRepeatable(),
                course.getDepartment() == null ? null : course.getDepartment().getName()
        );
    }

    private CourseOfferingDto toOfferingDto(CourseOffering offering) {
        return new CourseOfferingDto(
                offering.getId(),
                offering.getCourse().getCode(),
                offering.getCourse().getTitle(),
                offering.getTerm().getLabel(),
                offering.getCapacity(),
                offering.getEnrolledCount()
        );
    }

    private PrerequisiteDto toPrerequisiteDto(Prerequisite prerequisite) {
        return new PrerequisiteDto(
                prerequisite.getCourse().getCode(),
                prerequisite.getRequiredCourse().getCode()
        );
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }
}