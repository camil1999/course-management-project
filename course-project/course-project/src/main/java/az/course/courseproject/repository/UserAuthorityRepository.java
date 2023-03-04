package az.course.courseproject.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import az.course.courseproject.model.UserAuthority;

@Transactional
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Integer> {

	@Query(value = "insert into authorities(username, authority) select ?1, authority from authorities_list where manager=1", nativeQuery = true)
	@Modifying
	void saveManagerAuthorities(String username);

	@Query(value = "insert into authorities(username, authority) select ?1, authority from authorities_list where teacher=1", nativeQuery = true)
	@Modifying
	void saveTeacherAuthorities(String username);

	@Query(value = "insert into authorities(username, authority) select ?1, authority from authorities_list where admin=1", nativeQuery = true)
	@Modifying
	void saveAdminAuthorities(String username);

	@Query(value = "insert into authorities(username, authority) select ?1, authority from authorities_list where student=1", nativeQuery = true)
	@Modifying
	void saveStudentAuthorities(String username);

	@Query(value = "insert into authorities(username, authority) select ?1, authority from authorities_list where parent=1", nativeQuery = true)
	@Modifying
	void saveParentAuthorities(String username);
	
	@Query(value = "delete from authorities where username=?1",nativeQuery = true)
	@Modifying
	void deleteByUsername(String username);

}
