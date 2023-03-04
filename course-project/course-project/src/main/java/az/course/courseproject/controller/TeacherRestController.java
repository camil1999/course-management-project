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
import az.course.courseproject.request.TeacherRequest;
import az.course.courseproject.response.TeacherResponse;
import az.course.courseproject.service.TeacherService;

@RestController
@RequestMapping(path = "/teachers")
@CrossOrigin(origins = "*")
public class TeacherRestController {

	@Autowired
	private TeacherService teacherService;

	@PreAuthorize(value = "hasAuthority('add:teacher')")
	@PostMapping
	public void saveTeacher(@Valid @RequestBody TeacherRequest teacherRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		teacherService.saveTeacher(teacherRequest);
	}

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:teachers')")
	public MappingJacksonValue findAll() {

		List<TeacherResponse> teachers = teacherService.findAll();

		return filter(teachers, "teacher", "id", "name", "surname");
	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:teacher')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		TeacherResponse teacherResponse = teacherService.findById(id);
		return filter(teacherResponse, "teacher", "id", "name", "surname");
	}

	@PutMapping
	@PreAuthorize(value = "hasAuthority('edit:teacher')")
	public void editTeacher(@Valid @PathVariable Integer id, @RequestBody TeacherRequest teacherRequest,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		teacherService.editTeacher(id, teacherRequest);
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:teacher')")
	public void deleteById(@PathVariable Integer id) {
		teacherService.deleteById(id);
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}
}