package net.salla.springboottesting.repository;

import net.salla.springboottesting.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIT {

    @Autowired
    private EmployeeRepository employeeRepository;

    Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("sall@gmail.com")
                .build();

    }

    //Junit test for save employee operation
    @DisplayName("Junit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        /*
        @Builder --> builder()
            When you annotate a class with @Builder, Lombok generates a builder class with
            fluent APIs for each non-static, non-final field in the annotated class.
            This builder class can be used to create instances of the annotated class without
            having to write lengthy constructor calls with multiple parameters.
        */
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("salla")
//                .lastName("mallesh")
//                .email("sall@gmail.com")
//                .build();

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit for test for get all employee operations
    @DisplayName("Junit for test for get all employee operations")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {

        // given - precondition  or  setup

        /*   Employee employee1 = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("sall@gmail.com")
                .build();*/

        Employee employee1 = Employee.builder()
                .firstName("suresh")
                .lastName("kuruma")
                .email("kuruma@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit for test get employee by id
    @DisplayName("Junit for test get employee by id ")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        // given - precondition  or  setup
      /*  Employee employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();*/
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();


        // then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    //Junit for test get Employee by Email operation
    @DisplayName("Junit for test get Employee by Email operation")
    @Test
    public void  givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        // given - precondition  or  setup
      /*  Employee employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("salla@gmail.com")
                .build();*/
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test

        Employee employeeDb = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output

        assertThat(employeeDb).isNotNull();

    }

    //Junit test for update employee operation
    @DisplayName("Junit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition  or  setup

/*        Employee employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("mallesh@gmail.com")
                .build();*/

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("Salla");
        savedEmployee.setLastName("Mallesh");
        savedEmployee.setEmail("Salla@gmail.com");
        Employee updateEmployee = employeeRepository.save(savedEmployee);


        // then - verify the output
        assertThat(updateEmployee.getFirstName()).isEqualTo("Salla");
        assertThat(updateEmployee.getLastName()).isEqualTo("Mallesh");
        assertThat(updateEmployee.getEmail()).isEqualTo("Salla@gmail.com");
    }

    //Junit test for delete employee operation
    @DisplayName("Junit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenReturn() {

        // given - precondition  or  setup
     /*   Employee employee = Employee.builder()
                .firstName("Salla")
                .lastName("Mallesh")
                .email("Salla@gmail.com")
                .build()*/;
        employeeRepository.save(employee);
        // when - action or the behaviour that we are going to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(employeeOptional).isEmpty();

    }

    //Junit test for custom query using JPQL with index
    @DisplayName("Junit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        // given - precondition  or  setup
     /*   Employee employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("Salla@gmail.com")
                .build();*/

        employeeRepository.save(employee);
        String firstName = "salla";
        String lastName = "mallesh";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName,lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for custom query using JPQL with named params
    @DisplayName("Junit test for custom query using JPQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition  or  setup
  /*      Employee employee = Employee.builder()
                .firstName("salla")
                .lastName("mallesh")
                .email("Salla@gmail.com")
                .build();*/

        employeeRepository.save(employee);
        String firstName = "salla";
        String lastName = "mallesh";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName,lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


    //Junit test for custom query using Native SQL with index
    @DisplayName("Junit test for custom query using Native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenNativeSQL_thenReturnEmployeeObject() {

        // given - precondition  or  setup
/*        Employee employee = Employee.builder()
                .firstName("Salla")
                .lastName("Mallesh")
                .email("Salla@gmail.com")
                .build();*/

        employeeRepository.save(employee);
//        String firstName = "Salla";
//        String lastName = "Mallesh";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLIndex(employee.getFirstName(),employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for custom query using Native SQL with named Params
    @DisplayName("Junit test for custom query using Native SQL with named Params")
    @Test
    public void givenFirstNameAndLastName_whenNativeSQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition  or  setup
      /*  Employee employee = Employee.builder()
                .firstName("Salla")
                .lastName("Mallesh")
                .email("Salla@gmail.com")
                .build();*/

        employeeRepository.save(employee);
//        String firstName = "Salla";
//        String lastName = "Mallesh";

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(employee.getFirstName(),employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

}
