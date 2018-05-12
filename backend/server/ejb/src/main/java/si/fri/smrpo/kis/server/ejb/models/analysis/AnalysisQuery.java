package si.fri.smrpo.kis.server.ejb.models.analysis;

import si.fri.smrpo.kis.server.jpa.entities.Project;

import java.util.Date;

public class AnalysisQuery {

    private Project project;

    private Date createdFrom;
    private Date createdTo;

    private Date finishedFrom;
    private Date finishedTo;

    private Date devStartFrom;
    private Date devStartTo;

    private Integer workloadFrom;
    private Integer workloadTo;

    private Boolean isSilverBullet;
    private Boolean isRejected;



    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Date getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(Date createdTo) {
        this.createdTo = createdTo;
    }

    public Date getFinishedFrom() {
        return finishedFrom;
    }

    public void setFinishedFrom(Date finishedFrom) {
        this.finishedFrom = finishedFrom;
    }

    public Date getFinishedTo() {
        return finishedTo;
    }

    public void setFinishedTo(Date finishedTo) {
        this.finishedTo = finishedTo;
    }

    public Date getDevStartFrom() {
        return devStartFrom;
    }

    public void setDevStartFrom(Date devStartFrom) {
        this.devStartFrom = devStartFrom;
    }

    public Date getDevStartTo() {
        return devStartTo;
    }

    public void setDevStartTo(Date devStartTo) {
        this.devStartTo = devStartTo;
    }

    public Integer getWorkloadFrom() {
        return workloadFrom;
    }

    public void setWorkloadFrom(Integer workloadFrom) {
        this.workloadFrom = workloadFrom;
    }

    public Integer getWorkloadTo() {
        return workloadTo;
    }

    public void setWorkloadTo(Integer workloadTo) {
        this.workloadTo = workloadTo;
    }

    public Boolean getSilverBullet() {
        return isSilverBullet;
    }

    public void setSilverBullet(Boolean silverBullet) {
        isSilverBullet = silverBullet;
    }

    public Boolean getRejected() {
        return isRejected;
    }

    public void setRejected(Boolean rejected) {
        isRejected = rejected;
    }
}
