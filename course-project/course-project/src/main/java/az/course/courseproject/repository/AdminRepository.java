package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Admin;
@Transactional
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Admin findByUsername(String username);

	List<Admin> findByCourseId(Integer id);

}