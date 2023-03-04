package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "training")
public class TrainingResponse {

	private String name;
	private Double price;
	private String period;
	private Integer categoryId;

}
