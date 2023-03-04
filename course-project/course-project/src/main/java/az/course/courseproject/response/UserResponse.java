package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "user")
public class UserResponse {

	private String username;

	private String password;

	private Boolean enabled;

	private String type;

	private Integer courseId;
}
