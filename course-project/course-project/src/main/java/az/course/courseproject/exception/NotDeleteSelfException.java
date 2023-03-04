package az.course.courseproject.exception;

import lombok.Getter;

@Getter
public class NotDeleteSelfException extends RuntimeException {
	private String message;

	public NotDeleteSelfException(String message) {
		this.message = message;
	}
}
