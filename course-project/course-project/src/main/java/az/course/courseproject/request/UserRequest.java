package az.course.courseproject.request;

import lombok.Data;

@Data
public class UserRequest {

	private String username;

	private String password;

	private Boolean enabled;

	private String type;

	private Integer courseId;
}
