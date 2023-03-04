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
import az.course.courseproject.request.AdminRequest;
import az.course.courseproject.response.AdminResponse;
import az.course.courseproject.service.AdminService;

@RestController
@RequestMapping(path = "/admin")
@CrossOrigin(origins = "*")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@PostMapping
	@PreAuthorize(value = "hasAuthority('add:admin')")
	public void saveAdmin(@Valid @RequestBody AdminRequest dto, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		adminService.saveAdmin(dto);
	}

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:admin')")
	public MappingJacksonValue findAll() {
		List<AdminResponse> admins = adminService.findAll();

		return filter(admins, "admin", "id", "name", "surname");

	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:admin')")
	public void deleteById(@PathVariable Integer id) {
		adminService.deleteById(id);

	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:admin')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		AdminResponse adminResponse = adminService.findById(id);
		return filter(adminResponse, "admin", "id", "name", "surname");

	}

	@PutMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('edit:admin')")
	public void editAdmin(@Valid @PathVariable Integer id, @RequestBody AdminRequest adminRequest,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		adminService.editAdmin(id, adminRequest);

	}
	@PostMapping(path = "/login")
	public void login() {

	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}

}
