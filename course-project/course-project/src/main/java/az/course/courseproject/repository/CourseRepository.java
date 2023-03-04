package az.course.courseproject.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Course;
@Transactional
public interface CourseRepository extends JpaRepository<Course, Integer>{

	
	public Course findByUsername(String username);
}
