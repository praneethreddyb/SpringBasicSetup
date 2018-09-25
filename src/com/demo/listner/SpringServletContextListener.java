package com.demo.listner;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;


@WebListener
public class SpringServletContextListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
	    ServletContext context = event.getServletContext();
	    System.setProperty("rootPath", context.getRealPath("/"));
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
		// clear drivers
		while (drivers.hasMoreElements()) {
			try {
				DriverManager.deregisterDriver((Driver)drivers.nextElement());
			} catch (SQLException ex) {
				// deregistration failed, might want to do something, log at the very least
				ex.printStackTrace();
			}
		}		
		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public static void main(String[] args)throws Exception {
		Date date=new Date();
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		String dt=date.toInstant().toString();
		Instant i=Instant.parse(dt);
		System.out.println(date);
		System.out.println(dt);
		System.out.println(i);
		System.out.println(Date.from(i));
		//
		System.out.println(new Date());
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Date d=new Date(); 
		System.out.println(d);
		System.out.println(convertTimeZone(d, "IST"));
		System.out.println(new Date());
	}
	public static Date convertTimeZone(Date date, String timeZoneId){
		TimeZone fromTZ=TimeZone.getTimeZone("UTC");
		TimeZone toTZ=TimeZone.getTimeZone(timeZoneId);
	    long fromTZDst = 0;
	    if(fromTZ.inDaylightTime(date)) fromTZDst = fromTZ.getDSTSavings();
	    long fromTZOffset = fromTZ.getRawOffset() + fromTZDst;
	    long toTZDst = 0;
	    if(toTZ.inDaylightTime(date)) toTZDst = toTZ.getDSTSavings();
	    long toTZOffset = toTZ.getRawOffset() + toTZDst;
	    return new Date(date.getTime() + (toTZOffset - fromTZOffset));
	}*/ 
	
}