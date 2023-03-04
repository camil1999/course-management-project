package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Parent;
@Transactional
public interface ParentRepository extends JpaRepository<Parent, Integer> {
	List<Parent> findByCourseId(Integer id);
}