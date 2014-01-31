package com.vectorsf.jvoiceframework.sat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isb.rigel.gaia.services.SimpleServiceManager;
import com.isb.rigel.services.sat.SatException;
import com.isb.rigel.services.sat.SatMsg;
import com.isb.rigel.services.sat.SatService;
import com.isb.rigel.srvcimpl.sat.SatServiceImpl;
import com.vectorsf.jvoiceframework.core.admin.AppConfig;
import com.vectorsf.jvoiceframework.core.log.Log;
import com.vectorsf.jvoiceframework.core.log.Logger;

/**
 * Service to use banksphere sat connector
 * @author mvinuesa
 *
 */
 @Service
public class SatConnector {

	@Log
	private Logger logger;
	
	/** banksphere satService */
	private SatService satService = null;
	
	private SimpleServiceManager ssm = null;
	
	/** sat file configuration */
	private String satCfg = "sat.xml";

	/** sat log file configuration */
	private String satLog = "";
	
	/** sat log level */
	private String satLogLevel = "INFO";
	
	@Resource(name="satConfig")
	private AppConfig satConfig;
	
	/**
	 * Initialize bankshpere sat service
	 */
	@PostConstruct
	public void initSatService() {
		satService = new SatServiceImpl();
		try {
			readConfig();
			String satURL = this.getClass().getClassLoader().getResource(satCfg).toExternalForm();
			if (!System.getProperty("file.separator").equals("/")) {
				satURL  = satURL.replace('/', '\\');
			}
			ssm = new SimpleServiceManager((com.isb.rigel.core.Service) satService, 
											"satService", 
											satURL,
											satLog + "%name-%date-%order.log", satLogLevel);
			ssm.init();
		} catch (Exception e) {
			logger.error(SatConnectorMessages.ERROR_START_SAT_SERVICE, e);
		}
	}

	/**
	 * 
	 */
	private void readConfig() {
		
		if (satConfig == null) {
			logger.warn(SatConnectorMessages.WARN_SAT_CONFIG_NOT_FOUND, satCfg, satLog, satLogLevel);
		} else {
			if (satConfig.getValue("satCfg") != null) {
				this.satCfg = satConfig.getValue("satCfg");
			}
			if (satConfig.getValue("satLog") != null) {
				this.satLog = satConfig.getValue("satLog");
			}
			if (satConfig.getValue("satLogLevel") != null) {
				this.satLogLevel = satConfig.getValue("satLogLevel");
			}
		}
	}
	
	/**
	 * Release bankshpere sat service
	 */
	@PreDestroy
	public void stopSatService() {
		try {
			if (ssm != null) {
				ssm.destroy();
			}
		} catch (Exception e) {
			logger.error(SatConnectorMessages.ERROR_STOP_SAT_SERVICE, e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param version
	 * @return
	 * @throws SatException
	 */
	public SatMsg getSatMsg(String name, String version) throws SatException {
		logger.debug(SatConnectorMessages.DEBUG_START_GET_SAT_MSG, name, version);
		return satService.getMessage(name, version);
	}
	
	/**
	 * 
	 * @param mode
	 * @param alias
	 * @param msg
	 * @param timeout
	 * @throws SatException
	 */
	public void dispatch(String mode, String alias, SatMsg msg, int timeout) throws SatException {
		logger.debug(SatConnectorMessages.DEBUG_START_DISPATCH, msg.getName(), msg.getVersion(), mode, alias);
		satService.dispatch(mode, alias, msg, timeout);
		logger.debug(SatConnectorMessages.DEBUG_END_DISPATCH,  msg.getName(), msg.getVersion(), mode, alias);
	}
	
	public String getSatCfg() {
		return satCfg;
	}

	public String getSatLog() {
		return satLog;
	}

	public String getSatLogLevel() {
		return satLogLevel;
	}
}
