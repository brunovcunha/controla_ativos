package br.com.brunov.controla_ativos.controla_ativos.department;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceConflictException;
import br.com.brunov.controla_ativos.controla_ativos.config.exception.ResourceNotFoundException;
import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentRequest;
import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentResponse;
import br.com.brunov.controla_ativos.controla_ativos.department.dto.DepartmentUpdateRequest;
import br.com.brunov.controla_ativos.controla_ativos.department.entities.Department;
import br.com.brunov.controla_ativos.controla_ativos.department.repository.DepartmentRepository;
import br.com.brunov.controla_ativos.controla_ativos.department.service.DepartmentService;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .code("TI")
                .name("Tecnologia da Informação")
                .description("Departamento de tecnologia")
                .active(true)
                .build();
    }

    @Test
    void shouldCreateDepartmentSuccessfully() {
        // Arrange
        DepartmentRequest request = new DepartmentRequest(
                "RH",
                "Recursos Humanos",
                "Departamento de pessoas");

        when(departmentRepository.existsByCode("RH"))
                .thenReturn(false);

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        DepartmentResponse response = departmentService.create(request);

        // Assert
        assertNotNull(response);

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);

        verify(departmentRepository).save(captor.capture());

        Department savedDepartment = captor.getValue();

        assertEquals("RH", savedDepartment.getCode());
        assertEquals("Recursos Humanos", savedDepartment.getName());
        assertEquals(
                "Departamento de pessoas",
                savedDepartment.getDescription());
        assertTrue(savedDepartment.getActive());

        verify(departmentRepository).existsByCode("RH");
    }

    @Test
    void shouldThrowConflictWhenCreatingDuplicateCode() {
        // Arrange
        DepartmentRequest request = new DepartmentRequest(
                "TI",
                "Tecnologia",
                "Descrição");

        when(departmentRepository.existsByCode("TI"))
                .thenReturn(true);

        // Act & Assert
        ResourceConflictException exception = assertThrows(
                ResourceConflictException.class,
                () -> departmentService.create(request));

        assertEquals(
                "Já existe um departamento com o código: TI",
                exception.getMessage());

        verify(departmentRepository, never())
                .save(any(Department.class));
    }

    @Test
    void shouldReturnAllDepartments() {
        // Arrange
        Department secondDepartment = Department.builder()
                .id(2L)
                .code("RH")
                .name("Recursos Humanos")
                .description("Departamento de pessoas")
                .active(true)
                .build();

        when(departmentRepository.findAll())
                .thenReturn(List.of(department, secondDepartment));

        // Act
        List<DepartmentResponse> responses = departmentService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());

        verify(departmentRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoDepartmentsExist() {
        // Arrange
        when(departmentRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<DepartmentResponse> responses = departmentService.findAll();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(departmentRepository).findAll();
    }

    @Test
    void shouldFindDepartmentById() {
        // Arrange
        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        // Act
        DepartmentResponse response = departmentService.findById(1L);

        // Assert
        assertNotNull(response);

        verify(departmentRepository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundWhenFindingDepartmentById() {
        // Arrange
        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> departmentService.findById(99L));

        assertEquals(
                "Departamento não encontrado: 99",
                exception.getMessage());

        verify(departmentRepository, never())
                .save(any(Department.class));
    }

    @Test
    void shouldUpdateDepartmentPartially() {
        // Arrange
        DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                null,
                "Tecnologia e Sistemas",
                null,
                null);

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        DepartmentResponse response = departmentService.update(1L, request);

        // Assert
        assertNotNull(response);

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);

        verify(departmentRepository).save(captor.capture());

        Department updatedDepartment = captor.getValue();

        assertEquals("TI", updatedDepartment.getCode());
        assertEquals(
                "Tecnologia e Sistemas",
                updatedDepartment.getName());
        assertEquals(
                "Departamento de tecnologia",
                updatedDepartment.getDescription());
        assertTrue(updatedDepartment.getActive());
    }

    @Test
    void shouldUpdateDepartmentCodeWhenCodeIsAvailable() {
        // Arrange
        DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                "TEC",
                null,
                null,
                null);

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentRepository.existsByCode("TEC"))
                .thenReturn(false);

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        departmentService.update(1L, request);

        // Assert
        assertEquals("TEC", department.getCode());

        verify(departmentRepository).existsByCode("TEC");
        verify(departmentRepository).save(department);
    }

    @Test
    void shouldNotCheckDuplicateWhenCodeOnlyChangesCase() {
        // Arrange
        DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                "ti",
                null,
                null,
                null);

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        departmentService.update(1L, request);

        // Assert
        assertEquals("ti", department.getCode());

        verify(departmentRepository, never())
                .existsByCode("ti");

        verify(departmentRepository).save(department);
    }

    @Test
    void shouldThrowConflictWhenUpdatingToExistingCode() {
        // Arrange
        DepartmentUpdateRequest request = new DepartmentUpdateRequest(
                "RH",
                null,
                null,
                null);

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentRepository.existsByCode("RH"))
                .thenReturn(true);

        // Act & Assert
        ResourceConflictException exception = assertThrows(
                ResourceConflictException.class,
                () -> departmentService.update(1L, request));

        assertEquals(
                "Já existe um departamento com o código: RH",
                exception.getMessage());

        verify(departmentRepository, never())
                .save(any(Department.class));
    }

    @Test
    void shouldDeactivateDepartmentLogically() {
        // Arrange
        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        departmentService.delete(1L);

        // Assert
        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);

        verify(departmentRepository).save(captor.capture());

        Department deletedDepartment = captor.getValue();

        assertFalse(deletedDepartment.getActive());
        assertEquals(1L, deletedDepartment.getId());

        verify(departmentRepository, never())
                .deleteById(anyLong());

        verify(departmentRepository, never())
                .delete(any(Department.class));
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonexistentDepartment() {
        // Arrange
        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> departmentService.delete(99L));

        verify(departmentRepository, never())
                .save(any(Department.class));
    }
}
