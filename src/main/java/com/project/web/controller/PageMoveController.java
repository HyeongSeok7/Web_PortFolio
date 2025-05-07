package com.project.web.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.web.model.FestivalResponse;
import com.project.web.service.FestivalService;


@Controller
public class PageMoveController {

	private final FestivalService festivalService;
	
	@Autowired
    public PageMoveController(FestivalService festivalService) {
        this.festivalService = festivalService;
    }
	
	  @GetMapping("/culture")	//HTTP GET요청을 "/culture" URL로 매핑
	    public String moveCulturePage(Model model) {
		// FestivalService를 사용해 축제 데이터를 가져온다.
		  FestivalResponse festivalResponse = festivalService.getFestivalData(); 	// 축제 데이터를 조회 (API 또는 DB에서 가져옴 지금 프로젝트는 API

		  //가져온 축제 데이터를 모델에 추가
          model.addAttribute("festivalData", festivalResponse);	//"festivalData"라는 이름으로 축제 데이터를 모델에 저장하여 뷰에 전달
          return "culture";  // "culture.html" 이라는 뷰를 렌더링함
	    }
	  
	  @GetMapping("/education")
	    public String moveEducationPage(Model model) {
		  FestivalResponse festivalResponse = festivalService.getFestivalData();

          model.addAttribute("festivalData", festivalResponse);
          return "education";  // "education.html"이라는 뷰를 렌더링함	    }
	    }
	  
	  @GetMapping("/exhibition")
	    public String moveExhibitionPage(Model model) {
		  FestivalResponse festivalResponse = festivalService.getFestivalData();

          model.addAttribute("festivalData", festivalResponse);
          return "exhibition";  // "exhibition.html"이라는 뷰를 렌더링함	    }
	    }
	  
	  @GetMapping("/performance")
	    public String movePerformancePage(Model model) {
		  FestivalResponse festivalResponse = festivalService.getFestivalData();

          model.addAttribute("festivalData", festivalResponse);
          return "performance";  // "performance.html"이라는 뷰를 렌더링함	    }
	  }



	  @GetMapping("/customer")
	public String moveCustomerPage(Model model) {
		return "customer";
	  }





}
