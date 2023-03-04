package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import az.course.courseproject.model.Student;

@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {
	List<Student> findByCourseId(Integer id);

	@Query(value = "select * from students where name like %?1% or surname like %?2%", nativeQuery = true)
	@Modifying
	List<Student> searchStudent(String name, String surname);
}