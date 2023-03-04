package az.course.courseproject.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import az.course.courseproject.model.Contract;
@Transactional
public interface ContractRepository extends JpaRepository<Contract, Integer> {

	List<Contract> findAllByStudentId(Integer studentId);

	List<Contract> findAllByGroupId(Integer groupId);

	void deleteByStudentId(Integer id);
}
 