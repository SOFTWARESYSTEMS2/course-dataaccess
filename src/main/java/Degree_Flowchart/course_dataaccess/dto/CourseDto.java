package Degree_Flowchart.course_dataaccess.dto;

public record CourseDto(
        Long id,
        String code,
        String title,
        int credits,
        boolean repeatable,
        String departmentName
) {}