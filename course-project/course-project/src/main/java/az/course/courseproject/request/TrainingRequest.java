package az.course.courseproject.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TrainingRequest {
	private Integer id;
	@NotEmpty(message = "Ad hissəsini boş qoymaq olmaz!")
	@Size(min = 3, message = "Ad minimum 3 simvol olmalıdır!")
	@Size(max = 10, message = "Ad maksimum 10 simvol olmalıdır!")
	private String name;
	private Double price;
	private String period;
	private Integer courseId;
	private Integer categoryId;

}
