package br.com.brunov.controla_ativos.controla_ativos.maintenance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.brunov.controla_ativos.controla_ativos.maintenance.entities.MaintenanceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public class MaintenanceUpdateRequest {

    private MaintenanceType type;

    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String title;

    private String description;

    private LocalDate scheduledDate;

    private Long technicianId;

    @DecimalMin(value = "0.0", message = "O custo não pode ser negativo")
    private BigDecimal cost;

    private String notes;

    public MaintenanceType getType() {
        return type;
    }

    public void setType(MaintenanceType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

