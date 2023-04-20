package net.salla.springboottesting.service;

import net.salla.springboottesting.exception.ResourceNotFoundException;
import net.salla.springboottesting.model.Employee;
import net.salla.springboottesting.repository.EmployeeRepository;
import net.salla.springboottesting.service.impl.EmployeeServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@ExtendWith(MockitoExtension.class) is a JUnit 5 annotation used in the Mockito framework
// to enable the use of Mockito annotations such as @Mock, @InjectMocks, and @Spy in JUnit 5 tests.
// When this annotation is used, JUnit 5 will use the MockitoExtension class
// to automatically initialize any annotated Mockito objects before each test method is executed.
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        //  employeeRepository = Mockito.mock(EmployeeRepository.class);
        // employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();

    }

    //Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        // given - precondition  or  setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        System.out.println(employeeRepository);
        System.out.println(employeeService);
        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);
        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for saveEmployee method which throw exception
    @DisplayName("Junit test for saveEmployee method which throw exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowException() {

        // given - precondition  or  setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        System.out.println(employeeRepository);
        System.out.println(employeeService);
        // when - action or the behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // Junit test for get all employees method
    @DisplayName("Junit test for get all employees method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("shiva")
                .lastName("gudeti")
                .email("shiva@gmail.com")
                .build();
        // given - precondition  or  setup

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // Junit test for get all employees' method negative scenario
    @DisplayName("Junit test for get all employees method (negative scenario")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {

        // given - precondition  or  setup

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //Junit test for get employee by id
    @DisplayName("Junit test for get employee by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given - precondition  or  setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for update employee method
    @DisplayName("Junit test for update employee  method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition  or  setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Salla");
        employee.setLastName("Mallesh");

        // when - action or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        // then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Salla");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Mallesh");

    }

    //Junit test for delete method
    @DisplayName("Junit test for delete method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {

        long employeeId = 1L;
        // given - precondition  or  setup
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or the behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository,times(1)).deleteById(employeeId);

    }
}
