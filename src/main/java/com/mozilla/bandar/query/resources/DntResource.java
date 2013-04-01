package com.mozilla.bandar.query.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozilla.bandar.constant.Constants;
import com.mozilla.bandar.dnt.Data;
import com.mozilla.bandar.enumeration.DeviceType;
import com.mozilla.bandar.enumeration.GeoType;
import com.mozilla.bandar.enumeration.TimeFrame;
import com.mozilla.bandar.json.ErrorJson;

@Path("/dnt")
@Produces(MediaType.APPLICATION_JSON)
public class DntResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DntResource.class);
    private ErrorJson ej;
	public Data dddd;
	public DntResource (Data dddd) {
		this.ej = new ErrorJson();
		this.dddd = dddd;
	}
	
	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getAggreggateInstallData(@QueryParam("device") String deviceType, @QueryParam("duration") String duration, @QueryParam("geo") String geo) {
		
		if (!EnumUtils.isValidEnum(DeviceType.class, deviceType)) {
			LOGGER.error("invalid param for device: " + deviceType);
			return ej.invalidArgumentResponse("device", deviceType, Constants.MESSAGE_ERROR_INVALID_ARGUMENT);
		}
		if (!EnumUtils.isValidEnum(TimeFrame.class, duration)) {
			LOGGER.error("invalid param for duration: " + duration);
			return ej.invalidArgumentResponse("duration", duration, Constants.MESSAGE_ERROR_INVALID_ARGUMENT);
			
		}

		if (!EnumUtils.isValidEnum(GeoType.class, geo)) {
			LOGGER.error("invalid param for geo: " + geo);
			return ej.invalidArgumentResponse("geo", geo, Constants.MESSAGE_ERROR_INVALID_ARGUMENT);
			
		}

		
		return dddd.displayNumbers(deviceType, duration);
	}

}
