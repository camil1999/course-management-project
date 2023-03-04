package az.course.courseproject.data;

import java.util.List;

import az.course.courseproject.model.Group;
import lombok.Data;

@Data
public class DataWrapper {
	private List<Group> groups;
}
