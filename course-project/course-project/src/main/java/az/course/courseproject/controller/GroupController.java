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
import az.course.courseproject.request.GroupRequest;
import az.course.courseproject.response.GroupResponse;
import az.course.courseproject.service.GroupService;

@RestController
@RequestMapping(path = "/groups")
@CrossOrigin(origins = "*")
public class GroupController {

	@Autowired
	private GroupService groupService;

	@GetMapping
	@PreAuthorize(value = "hasAuthority('find:all:groups')")
	public MappingJacksonValue findAllGroups() {
		List<GroupResponse> groups = groupService.findAllGroups();
		return filter(groups, "group", "id", "name", "weekDay", "time");

	}

	@GetMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('find:by:id:group')")
	public MappingJacksonValue findById(@PathVariable Integer id) {
		GroupResponse group = groupService.findById(id);
		return filter(group, "group", "id", "name", "weekDay", "time");

	}

	@PostMapping
	@PreAuthorize(value = "hasAuthority('created:group')")
	public void saveGroup(@Valid @RequestBody GroupRequest groupRequest, BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		groupService.saveGroup(groupRequest);

	}

	@PutMapping
	@PreAuthorize(value = "hasAuthority('edit:group')")
	public void editGroupv(@Valid @PathVariable Integer id, @RequestBody GroupRequest groupRequest,
			BindingResult result) {
		if (result.hasErrors()) {
			throw new MyValidationException(result);
		}
		groupService.editGroup(id, groupRequest);
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:group')")
	public void deleteById(@PathVariable Integer id) {
		groupService.deleteById(id);
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}
}
