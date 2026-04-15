package Degree_Flowchart.course_dataaccess.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Degree_Flowchart.course_dataaccess.dto.CourseOfferingDto;
import Degree_Flowchart.course_dataaccess.services.CourseDataAccessService;

@RestController
@RequestMapping("/terms")
public class TermController {

    private final CourseDataAccessService service;

    public TermController(CourseDataAccessService service) {
        this.service = service;
    }

    @GetMapping("/{termLabel}/offerings")
    public List<CourseOfferingDto> getOfferingsByTerm(@PathVariable String termLabel) {
        return service.findOfferingsByTermLabel(termLabel);
    }

    @GetMapping("/active/offerings")
    public List<CourseOfferingDto> getActiveTermOfferings() {
        return service.findOfferingsForActiveTerm();
    }
}