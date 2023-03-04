package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.StudentNote;
@Transactional
public interface StudentNoteRepository extends JpaRepository<StudentNote, Integer> {

	
	public List <StudentNote>findAllByStudentId(Integer studentId);
	
	void deleteAllNoteByStudentId(Integer id);
}
