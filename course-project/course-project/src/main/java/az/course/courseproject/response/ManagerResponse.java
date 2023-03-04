package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@JsonFilter(value = "manager")
@Data
public class ManagerResponse {

	private String name;
	private String surname;

}
