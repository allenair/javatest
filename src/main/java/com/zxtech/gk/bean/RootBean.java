package com.zxtech.gk.bean;

import java.util.List;

public class RootBean {
	private String xmlnsns = "urn:sap-com:document:sap:rfc:functions";
	private String FFEHLERHANDLING;
	private String FVBELN_IN;
	private String FRCODE;
	private String FRTEXT;
	private SheaderBean SHEADER;
	private SlogicSwitch SLOGIC_SWITCH;
	private List<TpartnerItem> TPARTNER;
	private List<TkonditionenItem> TKONDITIONEN;
	private List<TpositionenItem> TPOSITIONEN;
	private List<TeinteilungenItem> TEINTEILUNGEN;
	private List<TmerkmalswerteItem> TMERKMALSWERTE;
	private List<TkonfigurationItem> TKONFIGURATION;
	private List<TkonfigurationrefItem> TKONFIGURATION_REF;
	private List<TkonfigurationinstItem> TKONFIGURATION_INST;
	private List<TextensioninItem> TEXTENSIONIN;

	public List<TextensioninItem> getTEXTENSIONIN() {
		return TEXTENSIONIN;
	}

	public void setTEXTENSIONIN(List<TextensioninItem> tEXTENSIONIN) {
		TEXTENSIONIN = tEXTENSIONIN;
	}

	public List<TkonfigurationinstItem> getTKONFIGURATION_INST() {
		return TKONFIGURATION_INST;
	}

	public void setTKONFIGURATION_INST(List<TkonfigurationinstItem> tKONFIGURATION_INST) {
		TKONFIGURATION_INST = tKONFIGURATION_INST;
	}

	public List<TkonfigurationrefItem> getTKONFIGURATION_REF() {
		return TKONFIGURATION_REF;
	}

	public void setTKONFIGURATION_REF(List<TkonfigurationrefItem> tKONFIGURATION_REF) {
		TKONFIGURATION_REF = tKONFIGURATION_REF;
	}

	public List<TkonfigurationItem> getTKONFIGURATION() {
		return TKONFIGURATION;
	}

	public void setTKONFIGURATION(List<TkonfigurationItem> tKONFIGURATION) {
		TKONFIGURATION = tKONFIGURATION;
	}

	public List<TmerkmalswerteItem> getTMERKMALSWERTE() {
		return TMERKMALSWERTE;
	}

	public void setTMERKMALSWERTE(List<TmerkmalswerteItem> tMERKMALSWERTE) {
		TMERKMALSWERTE = tMERKMALSWERTE;
	}

	public List<TeinteilungenItem> getTEINTEILUNGEN() {
		return TEINTEILUNGEN;
	}

	public void setTEINTEILUNGEN(List<TeinteilungenItem> tEINTEILUNGEN) {
		TEINTEILUNGEN = tEINTEILUNGEN;
	}

	public List<TpositionenItem> getTPOSITIONEN() {
		return TPOSITIONEN;
	}

	public void setTPOSITIONEN(List<TpositionenItem> tPOSITIONEN) {
		TPOSITIONEN = tPOSITIONEN;
	}

	public String getXmlnsns() {
		return xmlnsns;
	}

	public void setXmlnsns(String xmlnsns) {
		this.xmlnsns = xmlnsns;
	}

	public List<TkonditionenItem> getTKONDITIONEN() {
		return TKONDITIONEN;
	}

	public void setTKONDITIONEN(List<TkonditionenItem> tKONDITIONEN) {
		TKONDITIONEN = tKONDITIONEN;
	}

	public List<TpartnerItem> getTPARTNER() {
		return TPARTNER;
	}

	public void setTPARTNER(List<TpartnerItem> tPARTNER) {
		TPARTNER = tPARTNER;
	}

	public String getFFEHLERHANDLING() {
		return FFEHLERHANDLING;
	}

	public void setFFEHLERHANDLING(String fFEHLERHANDLING) {
		FFEHLERHANDLING = fFEHLERHANDLING;
	}

	public String getFVBELN_IN() {
		return FVBELN_IN;
	}

	public void setFVBELN_IN(String fVBELN_IN) {
		FVBELN_IN = fVBELN_IN;
	}

	public String getFRCODE() {
		return FRCODE;
	}

	public void setFRCODE(String fRCODE) {
		FRCODE = fRCODE;
	}

	public String getFRTEXT() {
		return FRTEXT;
	}

	public void setFRTEXT(String fRTEXT) {
		FRTEXT = fRTEXT;
	}

	public SheaderBean getSHEADER() {
		return SHEADER;
	}

	public void setSHEADER(SheaderBean sHEADER) {
		SHEADER = sHEADER;
	}

	public SlogicSwitch getSLOGIC_SWITCH() {
		return SLOGIC_SWITCH;
	}

	public void setSLOGIC_SWITCH(SlogicSwitch sLOGIC_SWITCH) {
		SLOGIC_SWITCH = sLOGIC_SWITCH;
	}

}
