package az.course.courseproject.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorities_list")
public class AuthoritiesList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer author;
	private String authority;
	private Integer admin;
	private Integer manager;
	private Integer parent;
	private Integer student;
	private Integer teacher;

//	@OneToMany
//	@JoinColumn(name = "course_id")
//	private List<CourseUser> courseUser;

}
