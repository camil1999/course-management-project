package az.course.courseproject.controller;

import java.util.ArrayList;
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
import az.course.courseproject.request.ParentRequest;
import az.course.courseproject.response.ParentResponse;
import az.course.courseproject.service.ParentService;

@RestController
@RequestMapping(path = "/parent")
@CrossOrigin(origins = "*")
public class ParentController {
	@Autowired
	private ParentService parentService;

	@PostMapping
	@PreAuthorize(value = "hasAuthority('add:parent')")
	public void saveParent(@Valid @RequestBody ParentRequest parentRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		parentService.saveParent(parentRequest);
	}

	@PreAuthorize(value = "hasAuthority('find:all:parents')")
	@GetMapping
	public MappingJacksonValue findAll() {
		List<ParentResponse> parents = new ArrayList<>();
		return filter(parents, "parent", "id", "name", "surname");
	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:parent')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		ParentResponse parent = parentService.findById(id);
		return filter(parent, "parent", "id", "name", "surname");
	}

	@PutMapping
	@PreAuthorize(value = "hasAuthority('edit:parent')")
	public void editParent(@Valid @PathVariable Integer id, @RequestBody ParentRequest parentRequest,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		parentService.editParent(id, parentRequest);
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:parent')")
	public void deleteById(@PathVariable Integer id) {
		parentService.deleteById(id);
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}
}