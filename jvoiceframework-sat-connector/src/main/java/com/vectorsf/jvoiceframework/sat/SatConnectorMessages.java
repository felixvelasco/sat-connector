package com.vectorsf.jvoiceframework.sat;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("com.vectorsf.jvoiceframework.sat.SatConnectorMessages")
@LocaleData(value = { @Locale(value = "es_ES", charset = "ISO8859_1") })
public enum SatConnectorMessages {
	// DEBUG
	DEBUG_START_GET_SAT_MSG,
	DEBUG_END_GET_SAT_MSG,
	DEBUG_START_DISPATCH,
	DEBUG_END_DISPATCH,
	// WARN
	WARN_SAT_CONFIG_NOT_FOUND,
	// ERROR
	ERROR_START_SAT_SERVICE,
	ERROR_STOP_SAT_SERVICE
}
