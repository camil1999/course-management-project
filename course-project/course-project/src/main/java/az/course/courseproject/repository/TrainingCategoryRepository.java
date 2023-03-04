package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.TrainingCategory;
@Transactional
public interface TrainingCategoryRepository extends JpaRepository<TrainingCategory, Integer> {

	List<TrainingCategory> findAllByCourseId(Integer courseId);
	
	
	
	
	
	
	
	
	
	
	
	
	

}
