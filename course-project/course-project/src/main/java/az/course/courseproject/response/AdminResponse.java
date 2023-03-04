package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "admin")
public class AdminResponse {

	private String name;

	private String surname;

}
