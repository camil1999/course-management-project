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
import az.course.courseproject.request.ManagerRequest;
import az.course.courseproject.response.ManagerResponse;
import az.course.courseproject.service.ManagerService;

@RestController
@RequestMapping(path = "/managers")
@CrossOrigin(origins = "*")
public class ManagerController {

	@Autowired
	private ManagerService managerService;

	@PreAuthorize(value = "hasAuthority('add:manager')")
	@PostMapping
	public void saveManager(@Valid @RequestBody ManagerRequest managerRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		managerService.saveManager(managerRequest);

	}

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:managers')")
	public MappingJacksonValue findAll() {
		List<ManagerResponse> managers = managerService.findAll();
		return filter(managers, "manager", "id", "name", "surname");
	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:manager')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		ManagerResponse managerResponse = managerService.findById(id);
		return filter(managerResponse, "manager", "id", "name", "surname");
	}

	@PutMapping
	@PreAuthorize(value = "hasAuthority('edit:manager')")
	public void editManager(@Valid @PathVariable Integer id, @RequestBody ManagerRequest managerRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		managerService.editManager(id,managerRequest);
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:manager')")
	public void deleteById(@PathVariable Integer id) {
		managerService.deleteById(id);
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);

		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}
}
