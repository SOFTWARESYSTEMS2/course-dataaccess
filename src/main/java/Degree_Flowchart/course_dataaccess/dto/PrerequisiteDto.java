package Degree_Flowchart.course_dataaccess.dto;

public record PrerequisiteDto(
        String courseCode,
        String requiredCourseCode
) {}