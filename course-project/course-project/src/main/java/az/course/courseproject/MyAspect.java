package az.course.courseproject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import az.course.courseproject.exception.MyAccesDeniedException;
import az.course.courseproject.model.Course;
import az.course.courseproject.model.User;
import az.course.courseproject.repository.CourseRepository;
import az.course.courseproject.repository.UserRepository;

@Component
@Aspect
public class MyAspect {

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private UserRepository userRepository;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@Before(value = "execution( * az.course.courseproject.controller.*.*(..))")
	public void doIt(JoinPoint jp) {
		User user = userRepository.findByUsername(getUsername());
		Course course = courseRepository.findById(user.getCourseId()).get();

		if (course.getCourseStatus() == false) {
			throw new MyAccesDeniedException("Bu kurs aktiv deyil!");
		}

	}

}
