package az.course.courseproject.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import az.course.courseproject.exception.MyValidationException;
import az.course.courseproject.request.TrainingRequest;
import az.course.courseproject.response.TrainingResponse;
import az.course.courseproject.service.TrainingService;

@RestController
@RequestMapping(path = "/training")
@CrossOrigin(origins = "*")
public class TrainingController {

	@Autowired
	private TrainingService trainingService;

	@PostMapping
	@PreAuthorize(value = "hasAuthority('add:training')")
	public void saveTraining(@Valid @RequestBody TrainingRequest trainingRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		trainingService.saveTraining(trainingRequest);
	}

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:trainings')")
	public MappingJacksonValue findAll() {
		List<TrainingResponse> trainings = trainingService.findAll();
		return filter(trainings, "training", "id", "name", "price", "period");
	}

	@GetMapping(path = "/category/{categoryId}")
	@PreAuthorize(value = "hasAuthority('find:by:categoryId:traning')")
	public MappingJacksonValue findByCategoryId(@PathVariable Integer categoryId) {
		List<TrainingResponse> trainings = trainingService.findByCategoryId(categoryId);
		return filter(trainings, "training", "id", "name", "price", "period");
	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:training')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		TrainingResponse training = trainingService.findById(id);
		return filter(training, "training", "name", "price", "period");
	}

	@PutMapping
	@PreAuthorize(value = "hasAuthority('edit:training')")
	public void editTraining(@Valid @PathVariable Integer id, @RequestBody TrainingRequest trainingRequest,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		trainingService.editTraining(id, trainingRequest);
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:training')")
	public void deleteById(@PathVariable Integer id) {
		trainingService.deleteById(id);
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}

}
