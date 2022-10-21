package fr.comprehensiveit.samples.om;


public class HealthCheck {

    private String status;

    HealthCheck(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
