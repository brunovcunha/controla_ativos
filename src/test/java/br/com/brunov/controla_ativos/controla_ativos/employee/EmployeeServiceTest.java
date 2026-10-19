package br.com.brunov.controla_ativos.controla_ativos.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceConflictException;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceNotFoundException;
import br.com.brunov.controla_ativos.controla_ativos.department.entities.Department;
import br.com.brunov.controla_ativos.controla_ativos.department.repository.DepartmentRepository;
import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeRequest;
import br.com.brunov.controla_ativos.controla_ativos.employee.dto.EmployeeUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.employee.entities.Employee;
import br.com.brunov.controla_ativos.controla_ativos.employee.entities.EmployeeStatus;
import br.com.brunov.controla_ativos.controla_ativos.employee.repository.EmployeeRepository;
import br.com.brunov.controla_ativos.controla_ativos.employee.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Department department;
    private Employee employee;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(10L)
                .code("TI")
                .name("Tecnologia da Informação")
                .description("Departamento de tecnologia")
                .active(true)
                .build();

        employee = Employee.builder()
                .id(1L)
                .registration("FUNC001")
                .name("João Silva")
                .email("joao@empresa.com")
                .phone("34999990000")
                .jobTitle("Analista de TI")
                .notes("Funcionário de teste")
                .status(EmployeeStatus.ACTIVE)
                .department(department)
                .build();
    }

    // CREATE

    @Test
    void shouldCreateEmployeeSuccessfully() {
        EmployeeRequest request = new EmployeeRequest(
                "FUNC002",
                "Maria Silva",
                "maria@empresa.com",
                "34988887777",
                "Desenvolvedora",
                "Novo funcionário",
                10L);

        when(employeeRepository.existsByRegistration("FUNC002"))
                .thenReturn(false);
        when(employeeRepository.existsByEmail("maria@empresa.com"))
                .thenReturn(false);
        when(departmentRepository.findById(10L))
                .thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = employeeService.create(request);

        assertNotNull(response);

        verify(employeeRepository).existsByRegistration("FUNC002");
        verify(employeeRepository).existsByEmail("maria@empresa.com");
        verify(departmentRepository).findById(10L);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void shouldThrowConflictWhenCreatingDuplicateRegistration() {
        EmployeeRequest request = new EmployeeRequest(
                "FUNC001",
                "Maria Silva",
                "maria@empresa.com",
                "34988887777",
                "Desenvolvedora",
                null,
                10L);

        when(employeeRepository.existsByRegistration("FUNC001"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> employeeService.create(request));

        verify(employeeRepository, never()).save(any(Employee.class));
        verify(departmentRepository, never()).findById(any());
    }

    @Test
    void shouldThrowIllegalArgumentWhenCreatingDuplicateEmail() {
        EmployeeRequest request = new EmployeeRequest(
                "FUNC002",
                "Maria Silva",
                "joao@empresa.com",
                "34988887777",
                "Desenvolvedora",
                null,
                10L);

        when(employeeRepository.existsByRegistration("FUNC002"))
                .thenReturn(false);
        when(employeeRepository.existsByEmail("joao@empresa.com"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> employeeService.create(request));

        verify(employeeRepository, never()).save(any(Employee.class));
        verify(departmentRepository, never()).findById(any());
    }

    @Test
    void shouldThrowNotFoundWhenCreatingEmployeeWithNonexistentDepartment() {
        EmployeeRequest request = new EmployeeRequest(
                "FUNC002",
                "Maria Silva",
                "maria@empresa.com",
                "34988887777",
                "Desenvolvedora",
                null,
                99L);

        when(employeeRepository.existsByRegistration("FUNC002"))
                .thenReturn(false);
        when(employeeRepository.existsByEmail("maria@empresa.com"))
                .thenReturn(false);
        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.create(request));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // FIND ALL

    @Test
    void shouldReturnAllEmployees() {
        Employee secondEmployee = Employee.builder()
                .id(2L)
                .registration("FUNC002")
                .name("Maria Silva")
                .email("maria@empresa.com")
                .status(EmployeeStatus.ACTIVE)
                .department(department)
                .build();

        when(employeeRepository.findAll())
                .thenReturn(List.of(employee, secondEmployee));

        var response = employeeService.findAll();

        assertEquals(2, response.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeesExist() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        var response = employeeService.findAll();

        assertTrue(response.isEmpty());
        verify(employeeRepository).findAll();
    }

    // FIND BY ID

    @Test
    void shouldFindEmployeeById() {
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        var response = employeeService.findById(1L);

        assertNotNull(response);
        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundWhenFindingNonexistentEmployee() {
        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.findById(99L));
    }

    // UPDATE

    @Test
    void shouldUpdateEmployeePartially() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                null, // registration
                "João Silva Atualizado", // name
                null, // email
                "34977776666", // phone
                "Coordenador de TI", // jobTitle
                null, // status
                "Atualização de dados", // notes
                null // departmentId
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = employeeService.update(1L, request);

        assertNotNull(response);
        assertEquals("João Silva Atualizado", employee.getName());
        assertEquals("34977776666", employee.getPhone());
        assertEquals("Coordenador de TI", employee.getJobTitle());
        assertEquals("Atualização de dados", employee.getNotes());

        verify(employeeRepository).save(employee);
        verifyNoInteractions(departmentRepository);
    }

    @Test
    void shouldUpdateRegistrationWhenItIsAvailable() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                "FUNC010",
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.existsByRegistration("FUNC010"))
                .thenReturn(false);
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        employeeService.update(1L, request);

        assertEquals("FUNC010", employee.getRegistration());
        verify(employeeRepository).existsByRegistration("FUNC010");
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldThrowConflictWhenUpdatingToExistingRegistration() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                "FUNC002",
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.existsByRegistration("FUNC002"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> employeeService.update(1L, request));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldUpdateEmailWhenItIsAvailable() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                null, // registration
                null, // name
                "novo@empresa.com", // email
                null, // phone
                null, // jobTitle
                null, // status
                null, // notes
                null // departmentId
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("novo@empresa.com"))
                .thenReturn(false);
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        employeeService.update(1L, request);

        assertEquals("novo@empresa.com", employee.getEmail());
        verify(employeeRepository).existsByEmail("novo@empresa.com");
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldThrowConflictWhenUpdatingToExistingEmail() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                null, // registration
                null, // name
                "outro@empresa.com", // email
                null, // phone
                null, // jobTitle
                null, // status
                null, // notes
                null // departmentId
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.existsByEmail("outro@empresa.com"))
                .thenReturn(true);

        assertThrows(
                ResourceConflictException.class,
                () -> employeeService.update(1L, request));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldNotCheckEmailConflictWhenOnlyCaseChanges() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                null, // registration
                null, // name
                "JOAO@EMPRESA.COM", // email
                null, // phone
                null, // jobTitle
                null, // status
                null, // notes
                null // departmentId
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        employeeService.update(1L, request);

        assertEquals("JOAO@EMPRESA.COM", employee.getEmail());
        verify(employeeRepository, never()).existsByEmail(any());
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldUpdateDepartmentSuccessfully() {
        Department newDepartment = Department.builder()
                .id(20L)
                .code("RH")
                .name("Recursos Humanos")
                .active(true)
                .build();

        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                null, // registration
                null, // name
                null, // email
                null, // phone
                null, // jobTitle
                null, // status
                null, // notes
                20L // departmentId
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(departmentRepository.findById(20L))
                .thenReturn(Optional.of(newDepartment));
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        employeeService.update(1L, request);

        assertEquals(newDepartment, employee.getDepartment());
        verify(departmentRepository).findById(20L);
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingToNonexistentDepartment() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                null, // registration
                null, // name
                null, // email
                null, // phone
                null, // jobTitle
                null, // status
                null, // notes
                99L // departmentId
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.update(1L, request));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingNonexistentEmployee() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                null,
                null,
                null,
                "Nome atualizado",
                null,
                null,
                null,
                null);

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.update(99L, request));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // DELETE

    @Test
    void shouldDeactivateEmployeeLogically() {
        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        employeeService.delete(1L);

        assertEquals(EmployeeStatus.INACTIVE, employee.getStatus());
        verify(employeeRepository).save(employee);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonexistentEmployee() {
        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.delete(99L));

        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
