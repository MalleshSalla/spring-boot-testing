package net.salla.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.salla.springboottesting.model.Employee;
import net.salla.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }
    //Junit test for save employee Rest api
    @DisplayName("Junit test for save employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition  or  setup
        Employee employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();

        // when - action or the behaviour that we are going to test

        ResultActions responce = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statement
        responce.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));

    }

    //Junit test for get all employees Rest api
    @DisplayName("Junit test for get all employees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployee_thenReturnListOfEmployees() throws Exception {

        // given - precondition  or  setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("salla").lastName("mallesh").email("salla@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("shiva").lastName("shakti").email("shiva@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
        // $ - root member of a Json structure whether it is an object or array

    }

    //Junit test for get employee by id Rest api
    @DisplayName("Junit test for get employee by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        // given - precondition  or  setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();
       employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(employee.getLastName())))
                .andExpect(jsonPath("$.email",is(employee.getEmail())));

    }

    // Invalid scenario - Invalid employee Id
    //Junit test for get employee by id Rest api
    @DisplayName("Junit test for get employee by id negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        // given - precondition  or  setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("durga")
                .lastName("prasad")
                .email("durga@gmail.com")
                .build();
        employeeRepository.save(employee);


        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Junit test for update employee Rest api - positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        // given - precondition  or  setup

        Employee savedEmployee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updateEmployee = Employee.builder()
                .firstName("shiva")
                .lastName("Gudeti")
                .email("shiva@gmail.com")
                .build();


        // when - action or the behaviour that we are going to test
        ResultActions response =  mockMvc.perform(put("/api/employees/{id}",savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        // then - verify the output

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email",is(updateEmployee.getEmail())));

    }

    //Junit test for update employee Rest api - negative scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        // given - precondition  or  setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updateEmployee = Employee.builder()
                .firstName("shiva")
                .lastName("Gudeti")
                .email("shiva@gmail.com")
                .build();


        // when - action or the behaviour that we are going to test
        ResultActions response =  mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        // then - verify the output

        response.andDo(print())
                .andExpect(status().isNotFound());

    }


    //Junit test for delete employee Rest api
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        // given - precondition  or  setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}",savedEmployee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }
}
