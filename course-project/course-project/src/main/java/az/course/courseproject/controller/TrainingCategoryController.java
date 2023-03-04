package az.course.courseproject.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import az.course.courseproject.exception.MyValidationException;
import az.course.courseproject.request.TrainingCategoryRequest;
import az.course.courseproject.response.TrainingCategoryResponse;
import az.course.courseproject.service.TrainingCategoryService;

@RestController
@RequestMapping(path = "category")
@CrossOrigin(origins = "*")
public class TrainingCategoryController {

	@Autowired
	private TrainingCategoryService trainingCategoryService;

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:category')")
	public MappingJacksonValue findAll() {
		List<TrainingCategoryResponse> trainingCategories = trainingCategoryService.findAll();
		return filter(trainingCategories, "category", "name");
	}

	@PostMapping
	@PreAuthorize(value = "hasAuthority('save:category')")
	public void saveTrainingCategory(@Valid @RequestBody TrainingCategoryRequest trainingCategoryRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		trainingCategoryService.saveTrainingCategory(trainingCategoryRequest);
	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:category')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		TrainingCategoryResponse trainingCategory = trainingCategoryService.findById(id);
		return filter(trainingCategory, "category", "name");

	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}
}
