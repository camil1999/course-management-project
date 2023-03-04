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
import az.course.courseproject.model.Group;
import az.course.courseproject.model.Teacher;
import az.course.courseproject.model.Training;
import az.course.courseproject.model.TrainingCategory;
import az.course.courseproject.repository.GroupRepository;
import az.course.courseproject.repository.TeacherRepository;
import az.course.courseproject.repository.TrainingCategoryRepository;
import az.course.courseproject.repository.TrainingRepository;
import az.course.courseproject.request.GroupRequest;
import az.course.courseproject.response.GroupResponse;

@Service
public class GroupService {
	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private TrainingRepository trainingRepository;

	@Autowired
	private TrainingCategoryRepository trainingCategoryRepository;

	@Autowired
	private ModelMapper mapper;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public List<GroupResponse> findAllGroups() {
		Teacher teacher = teacherRepository.findByUsername(getUsername());
		List<GroupResponse> groups = groupRepository.findByTeacherId(teacher.getId()).stream()
				.map(group -> convertGroupResp(group)).collect(Collectors.toList());
		return groups;
	}

	public GroupResponse findById(Integer id) {
		Optional<Group> findId = groupRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Qrup tapılmadı!");
		}
		Teacher currentTeacher = teacherRepository.findByUsername(getUsername());
		Group group = findId.get();
		if (group.getTeacherId() == currentTeacher.getId()) {
			GroupResponse groupResponse = convertGroupResp(group);
			return groupResponse;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void saveGroup(GroupRequest groupRequest) {
		Optional<TrainingCategory> category = trainingCategoryRepository.findById(groupRequest.getCategoryId());
		if (category.isEmpty()) {
			throw new UserIdNotFounException(groupRequest.getCategoryId() + "-Kateqoriya tapılmadı!");
		}
		Optional<Training> training = trainingRepository.findById(groupRequest.getTrainingId());
		if (training.isEmpty()) {
			throw new UserIdNotFounException(groupRequest.getTrainingId() + "-Təlim tapılmadı!");
		}
		TrainingCategory trainingCategory = category.get();
		Training tr = training.get();
		Teacher teacher = teacherRepository.findByUsername(getUsername());
		if (trainingCategory.getCourseId() == teacher.getCourseId() && tr.getCourseId() == teacher.getCourseId()) {
			Group group = new Group();
			mapper.map(groupRequest, group);
			group.setCourseId(teacher.getCourseId());
			group.setTeacherId(teacher.getId());
			groupRepository.save(group);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}

	}

	public void editGroup(Integer id,GroupRequest groupRequest) {
		Teacher currentTeacher = teacherRepository.findByUsername(getUsername());
		Optional<Group> editGroup = groupRepository.findById(id);
		if (editGroup.isEmpty()) {
			throw new UserIdNotFounException("Seçdiyiniz qrup tapılmadı-" + groupRequest.getId());
		}
		Group getGroup = editGroup.get();
		Optional<TrainingCategory> category = trainingCategoryRepository.findById(groupRequest.getCategoryId());
		if (category.isEmpty()) {
			throw new UserIdNotFounException(groupRequest.getCategoryId() + "-Kateqoriya tapılmadı!");
		}
		Optional<Training> training = trainingRepository.findById(groupRequest.getTrainingId());
		if (training.isEmpty()) {
			throw new UserIdNotFounException(groupRequest.getTrainingId() + "-Təlim tapılmadı!");
		}
		if (currentTeacher.getId() == getGroup.getTeacherId() && category.get().getCourseId() == getGroup.getCourseId()
				&& training.get().getCourseId() == getGroup.getCourseId()) {
			mapper.map(groupRequest, getGroup);
			groupRepository.save(getGroup);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void deleteById(Integer id) {
		Optional<Group> findedOpt = groupRepository.findById(id);
		if (findedOpt.isEmpty()) {
			throw new UserIdNotFounException("Seçdiyiniz qrup tapılmadı-" + id);
		}
		Group findedGroup = findedOpt.get();
		Teacher currentTeacher = teacherRepository.findByUsername(getUsername());
		if (findedGroup.getTeacherId() == currentTeacher.getId()) {
			groupRepository.delete(findedGroup);
		} else {
			throw new MyAccesDeniedException("Bu əməliyatı etməyə hüququnuz yoxdur!");
		}

	}

	private GroupResponse convertGroupResp(Group group) {
		GroupResponse groupResponse = new GroupResponse();
		mapper.map(group, groupResponse);
		return groupResponse;
	}
}
