package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "contract")
public class ContractResponse {

	
	private Integer id;
	private Integer studentId;
	private Integer groupId;

}
