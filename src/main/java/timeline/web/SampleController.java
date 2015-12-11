package timeline.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import timeline.sample.Account;

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
    public Account user(@PathVariable String userid, Model model) throws Exception {
    	sampleService.sampleRegist(userid);
    	Account account = sampleService.get(userid);
    	
        return account;
    }
}
