package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Teacher;
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	List<Teacher> findByCourseId(Integer id);

	Teacher findByUsername(String username);
}