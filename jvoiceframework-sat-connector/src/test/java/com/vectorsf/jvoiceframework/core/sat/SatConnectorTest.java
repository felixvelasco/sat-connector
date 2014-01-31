package com.vectorsf.jvoiceframework.core.sat;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.isb.rigel.services.sat.SatException;
import com.isb.rigel.services.sat.SatMsg;
import com.vectorsf.jvoiceframework.sat.SatConnector;

public class SatConnectorTest {

	private static SatConnector sat = null;
	
	private static ClassPathXmlApplicationContext applicationContext = null;
	

	@BeforeClass
	public static void startContext() {
		applicationContext = new ClassPathXmlApplicationContext("com/vectorsf/jvoiceframework/core/sat/sat-config-context.xml");
		//applicationContext.refresh();
		sat = (SatConnector) applicationContext.getBean("satConnector");
	}
	
	@Test
	public void TestSatMsg() {
		SatMsg sm;
		try {
			System.out.println("TestSatMsg " + System.getProperty("file.separator"));
			applicationContext.refresh();
			sat = (SatConnector)applicationContext.getBean("satConnector");
			sm = sat.getSatMsg( "001610", "00000001");
			sat.dispatch("SATDEF", "TCPDEF", sm, 400);
			System.out.println(sm);
		} catch (SatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestSatMsgUnixSeparator() {
		try {
			System.setProperty("file.separator","/");
			System.out.println("TestSatMsg " + System.getProperty("file.separator"));
			applicationContext.refresh();
			sat = (SatConnector)applicationContext.getBean("satConnector");
			SatMsg sm = sat.getSatMsg( "001610", "00000001");
			sat.dispatch("SATDEF", "TCPDEF", sm, 400);
			System.setProperty("file.separator","\\");
			assertEquals("Must be equals: ", "1116", sm.getRcveValue("IDCENT", 0));
		} catch (SatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestSatMsgEmptyConfiguration() {
		ClassPathXmlApplicationContext applicationContext = null;
		try {
			applicationContext = new ClassPathXmlApplicationContext("com/vectorsf/jvoiceframework/core/sat/sat-config-context-empty.xml");
			//applicationContext.refresh();
			SatConnector sat = (SatConnector) applicationContext.getBean("satConnector");
			sat = (SatConnector)applicationContext.getBean("satConnector");
			SatMsg sm = sat.getSatMsg( "001610", "00000001");
			sat.dispatch("SATDEF", "TCPDEF", sm, 400);
			assertEquals("Must be equals: ", "1116", sm.getRcveValue("IDCENT", 0));
		} catch (SatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			applicationContext.close();
		}
	}
	
	@Test(expected=BeanCreationException.class)
	public void TestSatMsgEmptyWithoutSatConfig() throws Exception{
		ClassPathXmlApplicationContext applicationContext = null;
		try {
			applicationContext = new ClassPathXmlApplicationContext("com/vectorsf/jvoiceframework/core/sat/sat-config-context-without-satConfig.xml");
			//applicationContext.refresh();
			SatConnector sat = (SatConnector) applicationContext.getBean("satConnector");
			sat = (SatConnector)applicationContext.getBean("satConnector");
			SatMsg sm = sat.getSatMsg( "001610", "00000001");
			sat.dispatch("SATDEF", "TCPDEF", sm, 400);
			assertEquals("Must be equals: ", "1116", sm.getRcveValue("IDCENT", 0));
		} catch (SatException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
			applicationContext.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	@Test
	public void TestSatMsgSatCfg() {
		assertEquals("Must be equals", sat.getSatCfg(), "com/vectorsf/jvoiceframework/core/sat/sat.xml");
	}
	
	@Test
	public void TestSatMsgSatLog() {
		assertEquals("Must be equals", sat.getSatLog(), "C:/DATOS/");
	}
	
	@Test
	public void TestSatMsgSatLogLevel() {
		assertEquals("Must be equals", sat.getSatLogLevel(), "DEBUG");
	}
	
	@AfterClass
	public static void stopContext() {
		applicationContext.close();
	}
}
