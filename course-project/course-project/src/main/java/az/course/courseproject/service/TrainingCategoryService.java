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
import az.course.courseproject.model.Manager;
import az.course.courseproject.model.TrainingCategory;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.TrainingCategoryRepository;
import az.course.courseproject.request.TrainingCategoryRequest;
import az.course.courseproject.response.TrainingCategoryResponse;

@Service
public class TrainingCategoryService {
	@Autowired
	private TrainingCategoryRepository trainingCategoryRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private ModelMapper mapper;

	private String getUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public List<TrainingCategoryResponse> findAll() {
		Manager currentManager = managerRepository.findByUsername(getUser());
		List<TrainingCategoryResponse> categories = trainingCategoryRepository
				.findAllByCourseId(currentManager.getCourseId()).stream().map(category -> convertCategoryResp(category))
				.collect(Collectors.toCollection(null));

		return categories;
	}

	public void saveTrainingCategory(TrainingCategoryRequest trainingCategoryRequest) {
		Manager manager = managerRepository.findByUsername(getUser());
		TrainingCategory category = new TrainingCategory();
		category.setName(trainingCategoryRequest.getName());
		category.setCourseId(manager.getCourseId());
		trainingCategoryRepository.save(category);
	}

	public TrainingCategoryResponse findById(Integer id) {
		Optional<TrainingCategory> findId = trainingCategoryRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Seçdiyiniz kateqoriya tapılmadı-" + id);
		}
		TrainingCategory category = findId.get();

		Manager manager = managerRepository.findByUsername(getUser());
		if (category.getCourseId() == manager.getCourseId()) {
			TrainingCategoryResponse resp = convertCategoryResp(category);
			return resp;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiniz!");
		}
	}

	private TrainingCategoryResponse convertCategoryResp(TrainingCategory trainingCategory) {
		TrainingCategoryResponse trainingCategoryResponse = new TrainingCategoryResponse();
		mapper.map(trainingCategory, trainingCategoryResponse);
		return trainingCategoryResponse;
	}
}
