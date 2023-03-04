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
import az.course.courseproject.model.Admin;
import az.course.courseproject.model.Manager;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.AdminRepository;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.UserAuthorityRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.ManagerRequest;
import az.course.courseproject.response.ManagerResponse;

@Service
public class ManagerService {
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private ManagerRepository managerRepository;

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

	public void saveManager(ManagerRequest managerRequest) {

		boolean usernameFound = false;
		User findedUser = userRepository.findByUsername(managerRequest.getUsername());
		if (findedUser != null)
			usernameFound = true;
		if (usernameFound) {
			throw new UsernameAlreadyDefinedException(
					"İstifadəçi adı artıq mövcuddur! " + managerRequest.getUsername());
		}

		Manager manager = new Manager();
		manager.setCreatorUsername(getUsername());
		mapper.map(managerRequest, manager);

		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		manager.setCourseId(currentAdmin.getCourseId());

		User user = new User();
		user.setUsername(managerRequest.getUsername());
		user.setPassword("{bcrypt}" + encoder.encode(managerRequest.getPassword()));
		user.setEnabled(true);
		user.setType("Manager");
		user.setCourseId(currentAdmin.getCourseId());
		userRepository.save(user);
		managerRepository.save(manager);
		userAuthorityRepository.saveManagerAuthorities(managerRequest.getUsername());
	}

	public List<ManagerResponse> findAll() {
		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		List<ManagerResponse> managers = managerRepository.findByCourseId(currentAdmin.getCourseId()).stream()
				.map(manager -> convertManagerResp(manager)).collect(Collectors.toList());

		return managers;
	}

	public ManagerResponse findById(Integer id) {
		Optional<Manager> findId = managerRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Menecer tapılmadı!");
		}
		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		Manager manager = managerRepository.findById(id).get();
		ManagerResponse managerResponse = convertManagerResp(manager);
		if (manager.getCourseId() == currentAdmin.getCourseId()) {
			return managerResponse;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}

	}

	public void editManager(Integer id, ManagerRequest managerRequest) {
		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		Optional<Manager> editManager = managerRepository.findById(id);
		if (editManager.isEmpty()) {
			throw new UserIdNotFounException("Menecer tapılmadı!");
		}
		Manager edit = editManager.get();
		if (edit.getCourseId() == currentAdmin.getCourseId()) {
			edit.setName(managerRequest.getName());
			edit.setSurname(managerRequest.getSurname());
			managerRepository.save(edit);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}

	}

	public void deleteById(Integer id) {
		Optional<Manager> findedOpt = managerRepository.findById(id);
		if (findedOpt.isPresent()) {
			Manager findedManager = findedOpt.get();
			Admin currentAdmin = adminRepository.findByUsername(getUsername());
			if (findedManager.getCourseId() == currentAdmin.getCourseId()) {
				managerRepository.delete(findedManager);
				userRepository.deleteByUsername(findedManager.getUsername());
				userAuthorityRepository.deleteByUsername(findedManager.getUsername());
			} else {
				throw new UserIdNotFounException("Seçdiyiniz istifadəçi tapılmadı-" + id);
			}
		}
	}

	private ManagerResponse convertManagerResp(Manager manager) {
		ManagerResponse managerResponse = new ManagerResponse();
		mapper.map(manager, managerResponse);
		return managerResponse;
	}
}
