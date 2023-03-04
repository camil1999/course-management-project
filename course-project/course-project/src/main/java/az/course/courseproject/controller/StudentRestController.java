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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import az.course.courseproject.exception.MyValidationException;
import az.course.courseproject.request.StudentRequest;
import az.course.courseproject.response.StudentResponse;
import az.course.courseproject.service.StudentService;

@RestController
@RequestMapping(path = "/students")
@CrossOrigin(origins = "*")
public class StudentRestController {

	@Autowired
	private StudentService studentService;

	@PostMapping
	@PreAuthorize(value = "hasAuthority('add:student')")
	public void save(@Valid @RequestBody StudentRequest studentRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		studentService.save(studentRequest);
	}

	@PutMapping
	@PreAuthorize(value = "hasAuthority('edit:student')")
	public void editStudent(@Valid @PathVariable Integer id, @RequestBody StudentRequest studentRequest,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		studentService.editStudent(id, studentRequest);
	}

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:students')")
	public MappingJacksonValue findAll() {
		List<StudentResponse> students = studentService.findAll();
		return filter(students, "student", "id", "name", "surname");
	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:student')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		StudentResponse student = studentService.findById(id);
		return filter(student, "student", "id", "name", "surname");

	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:student')")
	public void deleteById(@PathVariable Integer id) {
		studentService.deleteById(id);
	}

	@GetMapping(path = "/search")
	@PreAuthorize(value = "hasAuthority('search:student')")
	public MappingJacksonValue findSearch(@RequestParam String name, @RequestParam String surname) {
		List<StudentResponse> students = studentService.findSearch(name,surname);
		return filter(students, "student", "id", "name", "surname");
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}
}