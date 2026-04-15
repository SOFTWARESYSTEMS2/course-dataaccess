package Degree_Flowchart.course_dataaccess.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Degree_Flowchart.course_dataaccess.dto.CourseOfferingDto;
import Degree_Flowchart.course_dataaccess.services.CourseDataAccessService;

@RestController
@RequestMapping("/offerings")
public class OfferingController {

    private final CourseDataAccessService service;

    public OfferingController(CourseDataAccessService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CourseOfferingDto> getOffering(
            @RequestParam String courseCode,
            @RequestParam String termLabel) {

        return service.findOffering(courseCode, termLabel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}