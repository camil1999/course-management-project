package az.course.courseproject.exception;

import lombok.Getter;

@Getter
public class UserIdNotFounException extends RuntimeException {
	private String message;

	public UserIdNotFounException(String message) {
		this.message = message;

	}

}
