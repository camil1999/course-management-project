package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Manager;
@Transactional
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	
	public Manager findByUsername(String username);
	public List<Manager> findByCourseId(Integer id);
	
}
