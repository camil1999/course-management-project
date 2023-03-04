package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "parent")
public class ParentResponse {
	private String name;
	private String surname;

}
