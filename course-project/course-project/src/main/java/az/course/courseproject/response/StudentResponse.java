package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "student")
public class StudentResponse {

	private String name;
	private String surname;

}
