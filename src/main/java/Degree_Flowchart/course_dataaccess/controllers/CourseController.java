package Degree_Flowchart.course_dataaccess.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Degree_Flowchart.course_dataaccess.dto.CourseDto;
import Degree_Flowchart.course_dataaccess.dto.PrerequisiteDto;
import Degree_Flowchart.course_dataaccess.services.CourseDataAccessService;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseDataAccessService service;

    public CourseController(CourseDataAccessService service) {
        this.service = service;
    }

    @GetMapping
    public List<CourseDto> getCourses() {
        return service.findAllCourses();
    }

    @GetMapping("/{code}")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable String code) {
        return service.findCourseByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addCourse(@RequestBody CourseDto course) {
        service.addCourse(course);
    }

    @DeleteMapping("/{code}")
    public void deleteCourse(@PathVariable String code) {
        service.deleteCourse(code);
    }

    @GetMapping("/prerequisites")
    public List<PrerequisiteDto> getPrerequisites(
            @RequestParam(required = false) String courseCode
    ) {
        if (courseCode == null || courseCode.isBlank()) {
            return service.findAllPrerequisites();
        }

        return service.findPrerequisitesByCourseCode(courseCode);
    }

    @PostMapping("/prerequisites")
    public void addPrerequisite(@RequestBody PrerequisiteDto prerequisite) {
        service.addPrerequisite(prerequisite);
    }

    @DeleteMapping("/prerequisites")
    public void deletePrerequisite(@RequestBody PrerequisiteDto prerequisite) {
        service.deletePrerequisite(prerequisite);
    }
}