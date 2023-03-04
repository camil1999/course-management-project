package az.course.courseproject.author;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import az.course.courseproject.exception.UserIdNotFounException;
import az.course.courseproject.exception.UsernameAlreadyDefinedException;
import az.course.courseproject.model.Admin;
import az.course.courseproject.model.Course;
import az.course.courseproject.model.Group;
import az.course.courseproject.model.Manager;
import az.course.courseproject.model.Parent;
import az.course.courseproject.model.Student;
import az.course.courseproject.model.Teacher;
import az.course.courseproject.model.Training;
import az.course.courseproject.model.TrainingCategory;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.AdminRepository;
import az.course.courseproject.repository.ContractRepository;
import az.course.courseproject.repository.CourseRepository;
import az.course.courseproject.repository.GroupRepository;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.ParentRepository;
import az.course.courseproject.repository.StudentNoteRepository;
import az.course.courseproject.repository.StudentRepository;
import az.course.courseproject.repository.TeacherRepository;
import az.course.courseproject.repository.TrainingCategoryRepository;
import az.course.courseproject.repository.TrainingRepository;
import az.course.courseproject.repository.UserAuthorityRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.CourseRequest;
import az.course.courseproject.response.CourseResponse;

@Service
public class CourseService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private UserAuthorityRepository userAuthorityRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentNoteRepository studentNoteRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private TrainingRepository trainingRepository;

	@Autowired
	private TrainingCategoryRepository trainingCategoryRepository;

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ModelMapper mapper;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public List<CourseResponse> findAll() {
		List<CourseResponse> courses = courseRepository.findAll().stream().map(course -> convertCourseResp(course))
				.collect(Collectors.toList());
		if(courses.isEmpty()) {
			throw new UserIdNotFounException("Kurs tapılmadı!");
		}
		return courses;
	}

	public void saveCourse(CourseRequest courseRequest) {
		boolean usernameFound = false;
		User findedUser = userRepository.findByUsername(courseRequest.getUsername());
		if (findedUser != null)
			usernameFound = true;
		if (usernameFound) {
			throw new UsernameAlreadyDefinedException("İstifadəçi adı artıq mövcuddur! " + courseRequest.getUsername());
		}
		Course course = new Course();
		mapper.map(courseRequest, course);

		courseRepository.save(course);
		Admin admin = new Admin();
		mapper.map(courseRequest, admin);

		Course cours = courseRepository.findByUsername(course.getUsername());
		admin.setCreatorUsername(getUsername());
		admin.setCourseId(cours.getId());
		course.setCourseStatus(true);
		adminRepository.save(admin);

		User user = new User();
		user.setUsername(courseRequest.getUsername());
		user.setPassword("{bcrypt}" + encoder.encode(courseRequest.getPassword()));
		user.setEnabled(true);
		user.setType("Admin");
		user.setCourseId(cours.getId());
		userAuthorityRepository.saveAdminAuthorities(courseRequest.getUsername());
		userRepository.save(user);

	}

	public void editCourse(Integer id,CourseRequest courseRequest) {
		Optional<Course> editCourse = courseRepository.findById(id);
		if (editCourse.isEmpty()) {
			throw new UserIdNotFounException("Kurs tapılmadı!");
		}
		Course course = editCourse.get();
		course.setCourseName(courseRequest.getCourseName());
		courseRepository.save(course);

	}

	public CourseResponse findById(Integer id) {
		Optional<Course> findId = courseRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Kurs tapılmadı!");
		}
		CourseResponse course = convertCourseResp(findId.get());
		
		return course;
	}

	public void deleteById(Integer id) {
		Optional<Course> findId = courseRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Seçdiyiniz kurs tapılmadı-" + id);

		} else {
			courseRepository.delete(findId.get());
			List<Admin> courseAdmins = adminRepository.findByCourseId(findId.get().getId());
			for (Admin admin : courseAdmins) {
				adminRepository.delete(admin);
				userRepository.deleteByUsername(admin.getUsername());
				userAuthorityRepository.deleteByUsername(admin.getUsername());
			}
			List<Manager> courseManagers = managerRepository.findByCourseId(findId.get().getId());
			for (Manager manager : courseManagers) {
				managerRepository.delete(manager);
				userRepository.deleteByUsername(manager.getUsername());
				userAuthorityRepository.deleteByUsername(manager.getUsername());
			}
			List<Teacher> courseTeachers = teacherRepository.findByCourseId(findId.get().getId());
			for (Teacher teacher : courseTeachers) {
				teacherRepository.delete(teacher);
				userRepository.deleteByUsername(teacher.getUsername());
				userAuthorityRepository.deleteByUsername(teacher.getUsername());
			}
			List<Student> courseStudents = studentRepository.findByCourseId(findId.get().getId());
			for (Student student : courseStudents) {
				studentRepository.delete(student);
				userRepository.deleteByUsername(student.getUsername());
				userAuthorityRepository.deleteByUsername(student.getUsername());
				studentNoteRepository.deleteAllNoteByStudentId(student.getId());
				contractRepository.deleteByStudentId(student.getId());
			}
			List<Parent> courseParents = parentRepository.findByCourseId(findId.get().getId());
			for (Parent parent : courseParents) {
				parentRepository.delete(parent);
				userRepository.deleteByUsername(parent.getUsername());
				userAuthorityRepository.deleteByUsername(parent.getUsername());
			}
			List<Group> courseGroups = groupRepository.findByCourseId(findId.get().getId());
			for (Group group : courseGroups) {
				groupRepository.delete(group);

			}
			List<Training> courseTraining = trainingRepository.findByCourseId(findId.get().getId());
			for (Training training : courseTraining) {
				trainingRepository.delete(training);

			}
			List<TrainingCategory> courseCategory = trainingCategoryRepository.findAllByCourseId(findId.get().getId());
			for (TrainingCategory category : courseCategory) {
				trainingCategoryRepository.delete(category);
			}

		}
	}

	private CourseResponse convertCourseResp(Course course) {
		CourseResponse courseResponse = new CourseResponse();
		mapper.map(course, courseResponse);
		return courseResponse;
	}

}
