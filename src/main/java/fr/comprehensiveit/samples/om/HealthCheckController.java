package fr.comprehensiveit.samples.om;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    private Logger logger = LoggerFactory.getLogger(HealthCheckController.class);
    @RequestMapping(value = "/health",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public HealthCheck info() {
        return new HealthCheck("running");
    }
}
