package Degree_Flowchart.course_dataaccess.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{code}")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable String code) {
        return service.findCourseByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{code}/prerequisites")
    public List<PrerequisiteDto> getPrerequisites(@PathVariable String code) {
        return service.findPrerequisitesByCourseCode(code);
    }
}