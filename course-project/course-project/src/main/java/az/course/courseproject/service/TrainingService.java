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
import az.course.courseproject.model.Training;
import az.course.courseproject.model.TrainingCategory;
import az.course.courseproject.repository.ManagerRepository;
import az.course.courseproject.repository.TrainingCategoryRepository;
import az.course.courseproject.repository.TrainingRepository;
import az.course.courseproject.request.TrainingRequest;
import az.course.courseproject.response.TrainingResponse;

@Service
public class TrainingService {
	@Autowired
	private TrainingRepository trainingRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private TrainingCategoryRepository trainingCategoryRepository;

	private String getUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@Autowired
	private ModelMapper mapper;

	public void saveTraining(TrainingRequest trainingRequest) {
		Optional<TrainingCategory> findId = trainingCategoryRepository.findById(trainingRequest.getCategoryId());
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Təlim kateqoriyası tapılmadı!");
		}
		TrainingCategory category = findId.get();
		Manager manager = managerRepository.findByUsername(getUsername());
		if (manager.getCourseId() == category.getCourseId()) {
			Training training = new Training();
			mapper.map(trainingRequest, training);
			training.setCourseId(manager.getCourseId());
			trainingRepository.save(training);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public List<TrainingResponse> findAll() {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		List<TrainingResponse> trainings = trainingRepository.findByCourseId(currentManager.getCourseId()).stream()
				.map(training -> convertTrainingResp(training)).collect(Collectors.toList());

		return trainings;
	}

	public List<TrainingResponse> findByCategoryId(Integer categoryId) {
		Optional<TrainingCategory> findId = trainingCategoryRepository.findById(categoryId);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Təlim kateqoriyası tapılmadı!");
		}
		Manager manager = managerRepository.findByUsername(getUsername());
		TrainingCategory category = findId.get();
		if (manager.getCourseId() == category.getCourseId()) {
			List<TrainingResponse> trainings = trainingRepository.findAllByCategoryId(categoryId).stream()
					.map(training -> convertTrainingResp(training)).collect(Collectors.toList());
			return trainings;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public TrainingResponse findById(Integer id) {
		Optional<Training> findId = trainingRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Təlim tapılmadı!");
		}
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Training training = findId.get();
		TrainingResponse trainingResponse = convertTrainingResp(training);

		if (training.getCourseId() == currentManager.getCourseId()) {
			return trainingResponse;
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void editTraining(Integer id, TrainingRequest trainingRequest) {
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Optional<Training> editTraining = trainingRepository.findById(id);
		if (editTraining.isEmpty()) {
			throw new UserIdNotFounException("Seçdiyiniz təlim tapılmadı-" + id);
		}
		Training getTraining = editTraining.get();
		Optional<TrainingCategory> category = trainingCategoryRepository.findById(trainingRequest.getCategoryId());
		if (category.isEmpty()) {
			throw new UserIdNotFounException(trainingRequest.getCategoryId() + "-Kateqoriya tapılmadı!");
		}
		if (currentManager.getCourseId() == getTraining.getCourseId()
				&& category.get().getCourseId() == currentManager.getCourseId()) {
			getTraining.setName(trainingRequest.getName());
			getTraining.setPeriod(trainingRequest.getPeriod());
			getTraining.setPrice(trainingRequest.getPrice());
			trainingRepository.save(getTraining);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	public void deleteById(Integer id) {
		Optional<Training> findId = trainingRepository.findById(id);
		if (findId.isEmpty()) {
			throw new UserIdNotFounException("Təlim tapılmadı!");
		}
		Manager currentManager = managerRepository.findByUsername(getUsername());
		Training training = findId.get();
		if (training.getCourseId() == currentManager.getCourseId()) {
			trainingRepository.delete(training);
		} else {
			throw new MyAccesDeniedException("Bu əməliyyatı edə bilmərsiz!");
		}
	}

	private TrainingResponse convertTrainingResp(Training training) {
		TrainingResponse trainingResponse = new TrainingResponse();
		mapper.map(training, trainingResponse);
		return trainingResponse;
	}

}
