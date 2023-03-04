package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@JsonFilter(value = "course")
@Data
public class CourseResponse {
	private Integer id;
	private String courseName;
	private Boolean status;

}
