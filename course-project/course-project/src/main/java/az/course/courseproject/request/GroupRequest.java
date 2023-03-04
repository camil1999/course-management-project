package az.course.courseproject.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;


@Data
public class GroupRequest {
	private Integer id;
	@NotEmpty(message = "Ad hissəsini boş qoymaq olmaz!")
	@Size(min = 3, message = "Ad minimum 3 simvol olmalıdır!")
	@Size(max = 10, message = "Ad maksimum 10 simvol olmalıdır!")
	private String name;
	@NotEmpty(message = "Həftənin gününü boş qoymaq olmaz!")
	private String weekDay;
	@NotEmpty(message = "Vaxtı boş qoymaq olmaz!")
	private String time;
	private Integer categoryId;
	private Integer trainingId;

}
