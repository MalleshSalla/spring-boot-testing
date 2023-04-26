package net.salla.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.salla.springboottesting.model.Employee;
import net.salla.springboottesting.service.EmployeeService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper; // convert the object to json

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
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(0)));
       /* when(employeeService.saveEmployee(any(Employee.class)))
                .then(invocation -> invocation.getArgument(0));*/

        // when - action or the behaviour that we are going to test

        ResultActions responce = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statement
        responce.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //Junit test for get all employees Rest api
    @DisplayName("Junit test for get all employees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployee_thenReturnListOfEmployees() throws Exception {

        // given - precondition  or  setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("salla").lastName("mallesh").email("salla@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("shiva").lastName("shakti").email("shiva@gmail.com").build());

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

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
            given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

            // when - action or the behaviour that we are going to test
            ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

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
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

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
            long employeeId = 1L;
            Employee savedEmployee = Employee.builder()
                    .firstName("salla")
                    .lastName("mallesh")
                    .email("salla@gmail.com")
                    .build();
            Employee updateEmployee = Employee.builder()
                    .firstName("shiva")
                    .lastName("Gudeti")
                    .email("shiva@gmail.com")
                    .build();
            given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
            given(employeeService.updateEmployee(any(Employee.class)))
                    .willAnswer(invocation -> invocation.getArgument(0));

            // when - action or the behaviour that we are going to test
            ResultActions response =  mockMvc.perform(put("/api/employees/{id}",employeeId)
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
        long employeeId = 5L;
        Employee savedEmployee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();
        Employee updateEmployee = Employee.builder()
                .firstName("shiva")
                .lastName("Gudeti")
                .email("shiva@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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
            willDoNothing().given(employeeService).deleteEmployee(employeeId);

            // when - action or the behaviour that we are going to test
            ResultActions response = mockMvc.perform(delete("/api/employees/{id}",employeeId));

            // then - verify the output
            response.andExpect(status().isOk())
                    .andDo(print());

        }

}
