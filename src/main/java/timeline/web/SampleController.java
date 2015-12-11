package timeline.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {
	@Autowired
	private SampleService sampleService;
	
    @RequestMapping("/")
    String sample(Model model) throws Exception {
    	
        return "sample";
    }

    @RequestMapping("/{userid}")
    @ResponseBody
    public String user(@PathVariable String userid, Model model) throws Exception {    	
    	sampleService.registPost(userid, "こんにちは");
    	
        return "su";
    }
}
