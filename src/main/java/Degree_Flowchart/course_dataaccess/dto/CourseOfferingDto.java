package Degree_Flowchart.course_dataaccess.dto;

public record CourseOfferingDto(
        Long id,
        String courseCode,
        String courseTitle,
        String termLabel,
        int capacity,
        int enrolledCount
) {}