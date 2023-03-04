package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "teacher")
public class TeacherResponse {

	private String name;

	private String surname;

}
