package az.course.courseproject.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import az.course.courseproject.model.Person;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequest extends Person {
	private Integer id;
	@NotEmpty(message = "Kurs adını boş qoymaq olmaz!")
	@Size(min = 3, message = "Kurs adı minimum 3 simvol olmalıdır!")
	@Size(max = 15, message = "Kurs adı maksimum 15 simvol olmalıdır!")
	private String courseName;

}
