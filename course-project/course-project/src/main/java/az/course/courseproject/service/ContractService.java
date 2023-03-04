package az.course.courseproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import az.course.courseproject.exception.MyAccesDeniedException;
import az.course.courseproject.exception.UserIdNotFounException;
import az.course.courseproject.model.Contract;
import az.course.courseproject.model.Group;
import az.course.courseproject.model.Manager;
import az.course.courseproject.model.Student;
import az.course.courseproject.model.Teacher;
import az.course.courseproject.repository.ContractRepository;
import az.course.courseproject.repository.GroupRepository;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.StudentRepository;
import az.course.courseproject.repository.TeacherRepository;
import az.course.courseproject.request.ContractRequest;
import az.course.courseproject.response.ContractResponse;

@Service
public class ContractService {
	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private ModelMapper mapper;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public List<ContractResponse> getAllContractForManager(Integer studentId) {
		Optional<Student> findedStudent = studentRepository.findById(studentId);
		if (findedStudent.isEmpty()) {
			throw new UserIdNotFounException(studentId + "-Tələbə tapılmadı!");
		}
		Manager manager = managerRepository.findByUsername(getUsername());
		Student student = findedStudent.get();
		if (manager.getCourseId() == student.getCourseId()) {
			List<ContractResponse> contracts = contractRepository.findAllByStudentId(studentId).stream()
					.map(contract -> convertContractResp(contract)).collect(Collectors.toList());
			if (contracts.isEmpty()) {
				throw new UserIdNotFounException(studentId + "-Tələbənin contract-ı yoxdur!");
			}
			return contracts;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public List<ContractResponse> getAllContractForTeacher(Integer groupId) {
		Optional<Group> findedGroup = groupRepository.findById(groupId);
		if (findedGroup.isEmpty()) {
			throw new UserIdNotFounException(groupId + "-Qrup tapılmadı!");
		}
		Group group = findedGroup.get();
		Teacher teacher = teacherRepository.findByUsername(getUsername());
		if (teacher.getId() == group.getTeacherId()) {
			List<ContractResponse> contracts = contractRepository.findAllByGroupId(groupId).stream()
					.map(contract -> convertContractResp(contract)).collect(Collectors.toList());
			if (contracts.isEmpty()) {
				throw new UserIdNotFounException(groupId + "-Qrupun contract-ı yoxdur!");
			}
			return contracts;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void saveContract(ContractRequest contractRequest) {
		Optional<Student> findedStudent = studentRepository.findById(contractRequest.getStudentId());
		if (findedStudent.isEmpty()) {
			throw new UserIdNotFounException(contractRequest.getStudentId() + "-Tələbə tapılmadı!");
		}
		Optional<Group> findedGroup = groupRepository.findById(contractRequest.getGroupId());
		if (findedGroup.isEmpty()) {
			throw new UserIdNotFounException(contractRequest.getGroupId() + "-Qrup tapılmadı!");
		}
		Student student = findedStudent.get();
		Group group = findedGroup.get();
		Teacher teacher = teacherRepository.findByUsername(getUsername());
		if (student.getCourseId() == teacher.getCourseId() && group.getCourseId() == teacher.getCourseId()) {
			Contract contract = new Contract();
			mapper.map(contractRequest, contract);
			contractRepository.save(contract);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void deleteById(Integer id) {
		Optional<Contract> findedContract = contractRepository.findById(id);
		if (findedContract.isEmpty()) {
			throw new UserIdNotFounException(id + "Kontrakt tapılmadı");
		}
		Teacher teacher = teacherRepository.findByUsername(getUsername());
		Contract contract = findedContract.get();
		if (teacher.getId() == contract.getStudentId()) {
			contractRepository.delete(contract);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}

	}

	private ContractResponse convertContractResp(Contract contract) {
		ContractResponse contractDTO = new ContractResponse();
		mapper.map(contract, contractDTO);
		return contractDTO;
	}

}
