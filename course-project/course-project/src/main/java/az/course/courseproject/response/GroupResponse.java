package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@JsonFilter(value = "group")
@Data
public class GroupResponse {
	private String name;
	private String weekDay;
	private String time;
	private Integer categoryId;
	private Integer trainingId;

}
