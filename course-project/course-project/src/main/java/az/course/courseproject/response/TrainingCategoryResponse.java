package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@JsonFilter(value = "category")
@Data
public class TrainingCategoryResponse {

	private String name;

}
