package az.course.courseproject.response;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Data;

@Data
@JsonFilter(value = "notes")
public class StudentNoteResponse {
	private Integer studentId;
	private String note;
	private String nowDate;
	private String creatorUsername;

}
