package Degree_Flowchart.course_dataaccess.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Degree_Flowchart.course_dataaccess.dto.CourseOfferingDto;
import Degree_Flowchart.course_dataaccess.services.CourseDataAccessService;

@RestController
@RequestMapping("/courses/offerings")
public class OfferingController {

    private final CourseDataAccessService service;

    public OfferingController(CourseDataAccessService service) {
        this.service = service;
    }

    @GetMapping
    public List<CourseOfferingDto> getOfferingsByTerm(@RequestParam String term) {
        return service.findOfferingsByTerm(term);
    }

    @GetMapping("/{offeringId}")
    public CourseOfferingDto getOffering(@PathVariable String offeringId) {
        return service.findOfferingById(offeringId);
    }

    @PostMapping
    public void addOffering(@RequestBody CourseOfferingDto offering) {
        service.addOffering(offering);
    }

    @DeleteMapping("/{offeringId}")
    public void deleteOffering(@PathVariable String offeringId) {
        service.deleteOffering(offeringId);
    }
}