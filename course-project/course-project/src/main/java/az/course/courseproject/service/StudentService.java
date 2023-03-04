package az.course.courseproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import az.course.courseproject.exception.MyAccesDeniedException;
import az.course.courseproject.exception.UserIdNotFounException;
import az.course.courseproject.exception.UsernameAlreadyDefinedException;
import az.course.courseproject.model.Manager;
import az.course.courseproject.model.Student;
import az.course.courseproject.model.StudentNote;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.ContractRepository;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.StudentNoteRepository;
import az.course.courseproject.repository.StudentRepository;
import az.course.courseproject.repository.UserAuthorityRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.StudentRequest;
import az.course.courseproject.response.StudentResponse;

@Service
public class StudentService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAuthorityRepository userAuthorityRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentNoteRepository studentNoteRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ModelMapper mapper;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public void save(StudentRequest studentRequest) {
		boolean usernameFound = false;
		User findedUser = userRepository.findByUsername(studentRequest.getUsername());
		if (findedUser != null)
			usernameFound = true;
		if (usernameFound) {
			throw new UsernameAlreadyDefinedException(
					"İstifadəçi adı artıq mövcuddur! " + studentRequest.getUsername());
		}
		Student student = new Student();
		mapper.map(studentRequest, student);

		Manager currentManager = managerRepository.findByUsername(getUsername());
		student.setCreatorUsername(currentManager.getUsername());
		student.setManagerId(currentManager.getId());
		student.setCourseId(currentManager.getCourseId());

		User user = new User();
		user.setUsername(studentRequest.getUsername());
		user.setPassword("{bcrypt}" + encoder.encode(studentRequest.getPassword()));
		user.setEnabled(true);
		user.setType("Student");
		user.setCourseId(currentManager.getCourseId());
		userRepository.save(user);
		userAuthorityRepository.saveStudentAuthorities(studentRequest.getUsername());
		studentRepository.save(student);
	}

	public void editStudent(Integer id, StudentRequest studentRequest) {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Optional<Student> editStudent = studentRepository.findById(id);
		if (editStudent.isEmpty()) {
			throw new UserIdNotFounException("Tələbə tapılmadı!");
		}
		Student edit = editStudent.get();
		if (edit.getCourseId() == currentManager.getCourseId()) {
			edit.setName(studentRequest.getName());
			edit.setSurname(studentRequest.getSurname());
			studentRepository.save(edit);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public List<StudentResponse> findAll() {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		List<StudentResponse> students = studentRepository.findByCourseId(currentManager.getCourseId()).stream()
				.map(student -> convertStudentResp(student)).collect(Collectors.toList());

		return students;
	}

	public StudentResponse findById(Integer id) {
		Optional<Student> findId = studentRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Tələbə tapılmadı!");
		}
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Student student = studentRepository.findById(id).get();
		StudentResponse studentResponse = convertStudentResp(student);

		if (student.getCourseId() == currentManager.getCourseId()) {
			return studentResponse;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void deleteById(Integer id) {
		Optional<Student> findedOpt = studentRepository.findById(id);
		if (findedOpt.isEmpty()) {
			throw new UserIdNotFounException("Seçdiyiniz tələbə tapılmadı-" + id);
		}
		Student findedStudent = findedOpt.get();
		Manager currentManager = managerRepository.findByUsername(getUsername());
		if (findedStudent.getCourseId() == currentManager.getCourseId()) {
			studentRepository.delete(findedStudent);
			userRepository.deleteByUsername(findedStudent.getUsername());
			userAuthorityRepository.deleteByUsername(findedStudent.getUsername());
			List<StudentNote> studentNotes = studentNoteRepository.findAllByStudentId(id);
			studentNoteRepository.deleteAll(studentNotes);
			contractRepository.deleteByStudentId(findedStudent.getId());
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public List<StudentResponse> findSearch(String name, String surname) {
		List<StudentResponse> students = studentRepository.searchStudent(name, surname).stream()
				.map(student -> convertStudentResp(student)).collect(Collectors.toList());
		return students;
	}

	private StudentResponse convertStudentResp(Student student) {
		StudentResponse studentResponse = new StudentResponse();
		mapper.map(student, studentResponse);
		return studentResponse;
	}
}
