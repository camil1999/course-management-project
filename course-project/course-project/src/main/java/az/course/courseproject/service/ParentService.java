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
import az.course.courseproject.model.Parent;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.ParentRepository;
import az.course.courseproject.repository.UserAuthorityRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.ParentRequest;
import az.course.courseproject.response.ParentResponse;

@Service
public class ParentService {

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private UserAuthorityRepository userAuthorityRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public void saveParent(ParentRequest parentRequest) {
		boolean usernameFound = false;
		User findedUser = userRepository.findByUsername(parentRequest.getUsername());
		if (findedUser != null)
			usernameFound = true;
		if (usernameFound) {
			throw new UsernameAlreadyDefinedException("İstifadəçi adı artıq mövcuddur! " + parentRequest.getUsername());
		}
		Parent parent = new Parent();
		mapper.map(parentRequest, parent);

		Manager currentManager = managerRepository.findByUsername(getUsername());
		parent.setManagerId(currentManager.getId());
		parent.setCourseId(currentManager.getCourseId());
		parentRepository.save(parent);

		User user = new User();
		user.setUsername(parentRequest.getUsername());
		user.setPassword("{bcrypt}" + encoder.encode(parentRequest.getPassword()));
		user.setEnabled(true);
		user.setType("Parent");
		user.setCourseId(currentManager.getCourseId());
		userRepository.save(user);
		userAuthorityRepository.saveParentAuthorities(parentRequest.getUsername());
	}

	public List<ParentResponse> findAll() {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		List<ParentResponse> parents = parentRepository.findByCourseId(currentManager.getCourseId()).stream()
				.map(parent -> convertParentResp(parent)).collect(Collectors.toList());

		return parents;
	}

	public ParentResponse findById(Integer id) {
		Optional<Parent> findId = parentRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Valideyn tapılmadı!");
		}
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Parent parent = parentRepository.findById(id).get();
		ParentResponse parentResponse = convertParentResp(parent);
		if (parent.getCourseId() == currentManager.getCourseId()) {
			return parentResponse;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void editParent(Integer id, ParentRequest parentRequest) {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Optional<Parent> editParent = parentRepository.findById(id);
		if (editParent.isEmpty()) {
			throw new UserIdNotFounException("Valideyn tapılmadı!");
		}
		Parent edit = editParent.get();
		if (edit.getCourseId() == currentManager.getCourseId()) {
			edit.setName(parentRequest.getName());
			edit.setSurname(parentRequest.getSurname());
			parentRepository.save(edit);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void deleteById(Integer id) {
		Optional<Parent> findedOpt = parentRepository.findById(id);
		if (findedOpt.isPresent()) {
			Parent findedParent = findedOpt.get();
			Manager currentManager = managerRepository.findByUsername(getUsername());
			if (findedParent.getCourseId() == currentManager.getCourseId()) {
				parentRepository.delete(findedParent);
				userRepository.deleteByUsername(findedParent.getUsername());
				userAuthorityRepository.deleteByUsername(findedParent.getUsername());
			} else {
				throw new UserIdNotFounException("Seçdiyiniz istifadəçi tapılmadı-" + id);
			}
		}
	}

	private ParentResponse convertParentResp(Parent parent) {
		ParentResponse parentResponse = new ParentResponse();
		mapper.map(parent, parentResponse);
		return parentResponse;
	}
}
