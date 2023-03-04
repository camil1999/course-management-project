package az.course.courseproject.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import az.course.courseproject.model.User;
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
	@Query(value = "delete from users where username=?1",nativeQuery = true)
	@Modifying
	void deleteByUsername(String username);

	User findByUsername(String username);
	

	

}
