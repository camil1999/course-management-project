package az.course.courseproject.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;
@Getter
public class MyValidationException extends RuntimeException {

	private BindingResult br;

	public MyValidationException(BindingResult br) {
		this.br = br;
	}
}
