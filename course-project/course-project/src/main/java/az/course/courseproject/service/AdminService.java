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
import az.course.courseproject.exception.NotDeleteSelfException;
import az.course.courseproject.exception.UserIdNotFounException;
import az.course.courseproject.exception.UsernameAlreadyDefinedException;
import az.course.courseproject.model.Admin;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.AdminRepository;
import az.course.courseproject.repository.UserAuthorityRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.AdminRequest;
import az.course.courseproject.response.AdminResponse;

@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;

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

	public List<AdminResponse> findAll() {
		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		List<AdminResponse> admins = adminRepository.findByCourseId(currentAdmin.getCourseId()).stream()
				.map(admin -> convertAdminResp(admin)).collect(Collectors.toList());

		return admins;
	}

	public void saveAdmin(AdminRequest adminRequest) {

		boolean usernameFound = false;

		User findedUser = userRepository.findByUsername(adminRequest.getUsername());
		if (findedUser != null)
			usernameFound = true;
		if (usernameFound) {
			throw new UsernameAlreadyDefinedException("??stifad????i ad?? art??q m??vcuddur! " + adminRequest.getUsername());
		}

		Admin admin = new Admin();
		mapper.map(adminRequest, admin);
		admin.setCreatorUsername(getUsername());

		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		admin.setCourseId(currentAdmin.getCourseId());
		adminRepository.save(admin);
		
		User user = new User();
		user.setUsername(adminRequest.getUsername());
		user.setPassword("{bcrypt}" + encoder.encode(adminRequest.getPassword()));
		user.setEnabled(true);
		user.setType("Admin");
		user.setCourseId(currentAdmin.getCourseId());
		userRepository.save(user);
		userAuthorityRepository.saveAdminAuthorities(adminRequest.getUsername());
	}

	public void editAdmin(Integer id, AdminRequest adminRequest) {
		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		Optional<Admin> editAdmin = adminRepository.findById(id);
		if (editAdmin.isEmpty()) {
			throw new UserIdNotFounException("Admin tap??lmad??!");
		}
		Admin edit = editAdmin.get();
		if (edit.getCourseId() == currentAdmin.getCourseId()) {
			edit.setName(adminRequest.getName());
			edit.setSurname(adminRequest.getSurname());
			adminRepository.save(edit);
		} else {
			throw new MyAccesDeniedException("Bu ??m??liyyat?? ed?? bilm??rsiz!");
		}
	}

	public AdminResponse findById(Integer id) {
		Optional<Admin> findId = adminRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Admin tap??lmad??!");
		}
		Admin currentAdmin = adminRepository.findByUsername(getUsername());
		Admin admin = adminRepository.findById(id).get();
		AdminResponse adminResponse = convertAdminResp(admin);
		if (admin.getCourseId() == currentAdmin.getCourseId()) {
			return adminResponse;
		} else {
			throw new MyAccesDeniedException("Bu ??m??liyyat?? ed?? bilm??rsiz!");
		}
	}

	public void deleteById(Integer id) {
		Optional<Admin> findedOpt = adminRepository.findById(id);
		if (findedOpt.isEmpty()) {
			throw new UserIdNotFounException("Se??diyiniz istifad????i tap??lmad??-" + id);

		} else {
			Admin currentAdmin = adminRepository.findByUsername(getUsername());
			Admin findedAdmin = findedOpt.get();

			if (currentAdmin.getId() == findedAdmin.getId()) {
				throw new NotDeleteSelfException("??z??n??z?? sil?? bilm??rsiz!");
			}

			if (findedAdmin.getCourseId() == currentAdmin.getCourseId()) {
				adminRepository.delete(findedAdmin);
				userRepository.deleteByUsername(findedAdmin.getUsername());
				userAuthorityRepository.deleteByUsername(findedAdmin.getUsername());

			} else {
				throw new MyAccesDeniedException("Bu ??m??liyyat?? ed?? bilm??rsiz!");
			}
		}
	}

	private AdminResponse convertAdminResp(Admin admin) {
		AdminResponse adminResponse = new AdminResponse();
		mapper.map(admin, adminResponse);
		return adminResponse;
	}
}
