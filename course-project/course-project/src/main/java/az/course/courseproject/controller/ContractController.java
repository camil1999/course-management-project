package az.course.courseproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import az.course.courseproject.request.ContractRequest;
import az.course.courseproject.response.ContractResponse;
import az.course.courseproject.service.ContractService;

@RestController
@RequestMapping("/contract")
@CrossOrigin(origins = "*")
public class ContractController {

	@Autowired
	private ContractService contractService;

	@GetMapping(path = "/studentId/{studentId}")
	@PreAuthorize(value = "hasAuthority('find:all:contract:by:studentId')")
	public MappingJacksonValue getAllContractForManager(@PathVariable Integer studentId) {
		List<ContractResponse> contracts = contractService.getAllContractForManager(studentId);
		return filter(contracts, "contract", "studentId", "groupId");

	}

	@GetMapping(path = "/groupId/{groupId}")
	@PreAuthorize(value = "hasAuthority('find:all:contract:by:groupId')")
	MappingJacksonValue getAllContractForTeacher(@PathVariable Integer groupId) {
		List<ContractResponse> contracts = contractService.getAllContractForTeacher(groupId);
		return filter(contracts, "contract", "studentId", "groupId");

	}

	@PostMapping
	@PreAuthorize(value = "hasAuthority('save:contract')")
	public void saveContract(@RequestBody ContractRequest dto) {
		contractService.saveContract(dto);
	}

	@DeleteMapping(path = "/{id}")
	@PreAuthorize(value = "hasAuthority('delete:contract:by:id')")
	public void deleteById(@PathVariable Integer id) {
		contractService.deleteById(id);
	}

	public MappingJacksonValue filter(Object data, String dto, String... fields) {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
		FilterProvider provider = new SimpleFilterProvider().addFilter(dto, filter);
		MappingJacksonValue value = new MappingJacksonValue(data);
		value.setFilters(provider);
		return value;
	}

}
