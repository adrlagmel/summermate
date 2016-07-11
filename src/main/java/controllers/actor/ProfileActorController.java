/* ProfileController.java
 *
 * Copyright (C) 2013 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers.actor;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import controllers.AbstractController;

@Controller
@RequestMapping("/profile")
public class ProfileActorController extends AbstractController {
	

	// Services -------------------------------------------------------------------
	
//	@Autowired
//	private ActorService actorService;
//	
//	@Autowired
//	private AthleteService athleteService;
//	
//	@Autowired
//	private TeamService teamService;
//	
//	// Profile ---------------------------------------------------------------		
//		
//	
//	@RequestMapping(value="/actor", method = RequestMethod.GET)
//	public ModelAndView profile(){
//		ModelAndView result;
//		
//		Actor actor = actorService.findByPrincipal();
//	
//		result = new ModelAndView("actor");
//		
//		result.addObject("actor",actor);
//		
//		if(actor instanceof Customer)
//			result.addObject("isCustomer", true);
//		
//		if(actor instanceof Athlete)
//			result.addObject("isAthlete", true);
//			
//		result.addObject("requestURI", "profile/actor.do");
//			
//		return result;
//		
//	}
//	
//	@RequestMapping(value="/athlete", method = RequestMethod.GET)
//	public ModelAndView profileAthlete(@RequestParam int athleteId){
//		ModelAndView result;
//		
//		Athlete athlete = athleteService.findOne(athleteId);
//		
//		Assert.isTrue(teamService.shareTheSameGroup(athleteId));
//		result = new ModelAndView("athlete");
//		
//		result.addObject("actor",athlete);
//		result.addObject("isCustomer", true);
//		result.addObject("isAthlete", true);
//			
//		result.addObject("requestURI", "profile/athlete.do");
//			
//		return result;
//		
//	}
	

}