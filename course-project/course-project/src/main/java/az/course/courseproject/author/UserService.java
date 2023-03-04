package az.course.courseproject.author;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import az.course.courseproject.exception.UsernameAlreadyDefinedException;
import az.course.courseproject.model.User;
import az.course.courseproject.model.UserAuthority;
import az.course.courseproject.repository.UserAuthorityRepository;
import az.course.courseproject.repository.UserRepository;
import az.course.courseproject.request.UserRequest;
import az.course.courseproject.response.UserResponse;

@Service
public class UserService {
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

	public List<UserResponse> findAllUser() {
		List<UserResponse> users = userRepository.findAll().stream().map(user -> convertUserResp(user))
				.collect(Collectors.toList());

		return users;

	}

	public void addUser(UserRequest request) {
		boolean usernameFound = false;

		User findedUser = userRepository.findByUsername(request.getUsername());
		if (findedUser != null)
			usernameFound = true;
		if (usernameFound) {
			throw new UsernameAlreadyDefinedException("İstifadəçi adı artıq mövcuddur! " + request.getUsername());
		}

		User user = new User();

		user.setUsername(request.getUsername());
		user.setPassword("{bcrypt}" + encoder.encode(request.getPassword()));
		user.setEnabled(true);
		user.setType("User");
		user.setCourseId(0);
		userRepository.save(user);
		UserAuthority authority = new UserAuthority();
		authority.setUsername(request.getUsername());
		authority.setAuthority("User");
		userAuthorityRepository.save(authority);

	}

	private UserResponse convertUserResp(User user) {
		UserResponse userResponse = new UserResponse();
		mapper.map(user, userResponse);
		return userResponse;
	}
}
