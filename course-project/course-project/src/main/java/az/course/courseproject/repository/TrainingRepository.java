package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Training;
@Transactional
public interface TrainingRepository extends JpaRepository<Training, Integer> {
	List<Training> findByCourseId(Integer id);
	List<Training> findAllByCategoryId(Integer categoryId);
}