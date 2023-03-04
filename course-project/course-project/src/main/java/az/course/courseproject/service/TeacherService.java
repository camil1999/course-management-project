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
import az.course.courseproject.model.Teacher;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.TeacherRepository;
import az.course.courseproject.repository.UserAuthorityRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.TeacherRequest;
import az.course.courseproject.response.TeacherResponse;

@Service
public class TeacherService {
	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAuthorityRepository userAuthorityRepository;

	@Autowired
	private ModelMapper mapper;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public void saveTeacher(TeacherRequest teacherRequest) {
		boolean usernameFound = false;
		User findedUser = userRepository.findByUsername(teacherRequest.getUsername());
		if (findedUser != null)
			usernameFound = true;
		if (usernameFound) {
			throw new UsernameAlreadyDefinedException(
					"İstifadəçi adı artıq mövcuddur! " + teacherRequest.getUsername());
		}
		Teacher teacher = new Teacher();
		mapper.map(teacherRequest, teacher);

		Manager man = managerRepository.findByUsername(getUsername());
		teacher.setCourseId(man.getCourseId());
		teacher.setCreatorUsername(man.getUsername());
		teacher.setManagerId(man.getId());

		User user = new User();
		user.setUsername(teacherRequest.getUsername());
		user.setPassword("{bcrypt}" + encoder.encode(teacherRequest.getPassword()));
		user.setEnabled(true);
		user.setType("Teacher");
		user.setCourseId(man.getCourseId());
		userRepository.save(user);
		userAuthorityRepository.saveTeacherAuthorities(teacherRequest.getUsername());
		teacherRepository.save(teacher);
	}

	public List<TeacherResponse> findAll() {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		List<TeacherResponse> teachers = teacherRepository.findByCourseId(currentManager.getCourseId()).stream()
				.map(teacger -> convertAdminResp(teacger)).collect(Collectors.toList());

		return teachers;
	}

	public TeacherResponse findById(Integer id) {
		Optional<Teacher> findId = teacherRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Müəllim tapılmadı!");
		}
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Teacher teacher = teacherRepository.findById(id).get();
		TeacherResponse teacherResponse = convertAdminResp(teacher);
		if (teacher.getCourseId() == currentManager.getCourseId()) {
			return teacherResponse;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void editTeacher(Integer id, TeacherRequest teacherRequest) {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Optional<Teacher> editTeacher = teacherRepository.findById(id);
		if (editTeacher.isEmpty()) {
			throw new UserIdNotFounException("Valideyn tapılmadı!");
		}
		Teacher edit = editTeacher.get();
		if (edit.getCourseId() == currentManager.getCourseId()) {
			edit.setName(teacherRequest.getName());
			edit.setSurname(teacherRequest.getSurname());
			teacherRepository.save(edit);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void deleteById(Integer id) {
		Optional<Teacher> findedOpt = teacherRepository.findById(id);
		if (findedOpt.isPresent()) {
			Teacher findedTeacher = findedOpt.get();
			Manager currentManager = managerRepository.findByUsername(getUsername());
			if (findedTeacher.getCourseId() == currentManager.getCourseId()) {
				teacherRepository.delete(findedTeacher);
				userRepository.deleteByUsername(findedTeacher.getUsername());
				userAuthorityRepository.deleteByUsername(findedTeacher.getUsername());
			} else {
				throw new UserIdNotFounException("Seçdiyiniz istifadəçi tapılmadı-" + id);
			}
		}
	}

	private TeacherResponse convertAdminResp(Teacher teacher) {
		TeacherResponse teacherResponse = new TeacherResponse();
		mapper.map(teacher, teacherResponse);
		return teacherResponse;
	}

}
