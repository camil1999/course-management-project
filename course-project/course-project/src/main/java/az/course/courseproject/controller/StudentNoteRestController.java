package az.course.courseproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
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

import az.course.courseproject.request.StudentNoteRequest;
import az.course.courseproject.response.StudentNoteResponse;
import az.course.courseproject.service.StudentNoteRestService;

@RestController
@RequestMapping(path = "/student-note")
@CrossOrigin(origins = "*")
public class StudentNoteRestController {

	@Autowired
	private StudentNoteRestService studentNoteRestService;

	@PostMapping
	@PreAuthorize(value = "hasAuthority('add:student:note')")
	public void saveNote(@RequestBody StudentNoteRequest studentNoteRequest) {
		studentNoteRestService.saveNote(studentNoteRequest);
	}

	@GetMapping(path = "/student/{studentId}")
	@PreAuthorize(value = "hasAuthority('find:all:notes:by:studentId')")
	public MappingJacksonValue getAllNotewithStudentId(@PathVariable Integer studentId) {
		List<StudentNoteResponse> notes = studentNoteRestService.getAllNotewithStudentId(studentId);
		return filter(notes, "notes", "id", "note", "nowDate", "creatorUsername");
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}
}