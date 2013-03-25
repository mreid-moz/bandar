package com.mozilla.bandar.query.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.mozilla.bandar.dnt.Data;

@Path("/dnt")
@Produces(MediaType.APPLICATION_JSON)
public class DntResource {
	public Data DailyDesktopDntData;
	
	public DntResource (Data dddd) {
		this.DailyDesktopDntData = dddd;
	}
	
	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getAggreggateInstallData(@QueryParam("device") String deviceType, @QueryParam("duration") String duration) {
		return DailyDesktopDntData.displayDailyNumbers("DESKTOP");
		//return "Hello Jersey";
	}

}
