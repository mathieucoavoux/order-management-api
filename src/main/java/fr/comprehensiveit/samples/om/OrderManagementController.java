package fr.comprehensiveit.samples.om;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;


@RestController
public class OrderManagementController {

    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/swagger-ui.html");
    }

}
