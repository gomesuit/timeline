package timeline.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleController {
	
    @RequestMapping("/")
    String sample(Model model) throws Exception {
    	
        return "sample";
    }
}
