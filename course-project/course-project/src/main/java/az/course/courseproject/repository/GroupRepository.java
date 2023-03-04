package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Group;
@Transactional
public interface GroupRepository extends JpaRepository<Group, Integer> {
	List<Group> findByCourseId(Integer id);

	List<Group> findByTeacherId(Integer id);
}
