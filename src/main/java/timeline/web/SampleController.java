package timeline.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SampleController {
	@Autowired
	private SampleService sampleService;
	
    @RequestMapping("/")
    String sample(Model model) throws Exception {
    	
        return "sample";
    }

    @RequestMapping("/{userid}")
    public String user(@PathVariable String userid, Model model) throws Exception {
    	model.addAttribute("userid", userid);
    	model.addAttribute("postForm", new PostForm());
    	model.addAttribute("postList", sampleService.getPostList(userid));
    	
        return "sample";
    }

    @RequestMapping(value="/post", method=RequestMethod.POST)
    public String user(
    		@ModelAttribute PostForm form,
    		HttpServletRequest request) throws Exception {    	
    	sampleService.registPost(form);

		String referer = request.getHeader("Referer");
		return "redirect:" + referer;
    }
}
