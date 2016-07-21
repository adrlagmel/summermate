package controllers;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import services.PlayaService;

@Controller
@RequestMapping("/foto")
public class FotoController extends AbstractController {
	
	@Autowired
	private PlayaService playaService;

	// Constructor
	public FotoController() {
		super();
	}
				

//	@RequestMapping(value = "/incidence")
//	public void renderPhoto(HttpServletResponse response, @RequestParam int incidenceId) throws IOException, SQLException {
//
//		Incidence incidence = incidenceService.findOne(incidenceId);
//		if (incidence.getPhoto() != null) {
//			byte[] photo = incidence.getPhoto().getBytes(1, (int) incidence.getPhoto().length());
//			response.setContentType("image/jpeg");
//			response.setContentLength(photo.length);
//			response.getOutputStream().write(photo);
//			response.getOutputStream().flush();
//		}
//	}
	@RequestMapping(value = "/displayImage")
	public void displayImage(HttpServletResponse response,@RequestParam int playaId) throws IOException{

	    byte[] image = playaService.findOne(playaId).getFoto();
	    
	    response.setContentType("image/jpeg");
	    OutputStream o = response.getOutputStream();
	       o.write(image);
	       o.flush(); 
	       o.close();
	    
	    
	}
	
	
	
}
