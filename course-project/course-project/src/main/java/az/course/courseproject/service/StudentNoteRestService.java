package az.course.courseproject.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import az.course.courseproject.exception.MyAccesDeniedException;
import az.course.courseproject.exception.UserIdNotFounException;
import az.course.courseproject.model.Manager;
import az.course.courseproject.model.Student;
import az.course.courseproject.model.StudentNote;
import az.course.courseproject.model.Teacher;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.StudentNoteRepository;
import az.course.courseproject.repository.StudentRepository;
import az.course.courseproject.repository.TeacherRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.StudentNoteRequest;
import az.course.courseproject.response.StudentNoteResponse;

@Service
public class StudentNoteRestService {
	@Autowired
	private StudentNoteRepository studentNoteRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private UserRepository userRepository;;

	@Autowired
	private ModelMapper mapper;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public void saveNote(StudentNoteRequest studentNoteRequest) {
		Optional<Student> findId = studentRepository.findById(studentNoteRequest.getStudentId());
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Tələbə tapılmadı!");
		}
		Student student = findId.get();
		User userType = userRepository.findByUsername(getUsername());

		switch (userType.getType()) {

		case "Manager":
			Manager manager = managerRepository.findByUsername(getUsername());
			if (manager.getCourseId() == student.getCourseId()) {
				StudentNote note = new StudentNote();

				note.setNote(studentNoteRequest.getNote());
				note.setNowDate(LocalDate.now().toString());
				note.setCreatorUsername(manager.getUsername());
				note.setStudentId(student.getId());
				studentNoteRepository.save(note);
			} else {
				throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
			}
			break;

		case "Teacher":
			Teacher teacher = teacherRepository.findByUsername(getUsername());
			if (teacher.getCourseId() == student.getCourseId()) {
				StudentNote note = new StudentNote();
				note.setNote(studentNoteRequest.getNote());
				note.setNowDate(LocalDate.now().toString());
				note.setCreatorUsername(teacher.getUsername());
				note.setStudentId(student.getId());
				studentNoteRepository.save(note);
			} else {
				throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
			}
			break;
		default:
			System.out.println("errorr");
		}
	}

	public List<StudentNoteResponse> getAllNotewithStudentId(Integer studentId) {
		Optional<Student> findId = studentRepository.findById(studentId);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Tələbə tapılmadı!");
		}
		Manager manager = managerRepository.findByUsername(getUsername());
		Student student = findId.get();
		if (manager.getCourseId() == student.getCourseId()) {
			List<StudentNoteResponse> notes = studentNoteRepository.findAllByStudentId(studentId).stream()
					.map(studentNote -> convertNoteResp(studentNote)).collect(Collectors.toList());
			if (notes.isEmpty()) {
				throw new UserIdNotFounException("Tələbənin qeydi yoxdur!");
			}
			return notes;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	private StudentNoteResponse convertNoteResp(StudentNote studentNote) {
		StudentNoteResponse studentNoteResponse = new StudentNoteResponse();
		mapper.map(studentNote, studentNoteResponse);
		return studentNoteResponse;
	}
}
