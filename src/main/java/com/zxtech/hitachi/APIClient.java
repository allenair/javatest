package com.zxtech.hitachi;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class APIClient {
	public static final int IMAPI_SUCC = 0;
	public static final int IMAPI_CONN_ERR = -1;
	public static final int IMAPI_CONN_CLOSE_ERR = -2;
	public static final int IMAPI_INS_ERR = -3;
	public static final int IMAPI_DEL_ERR = -4;
	public static final int IMAPI_QUERY_ERR = -5;
	public static final int IMAPI_DATA_ERR = -6;
	public static final int IMAPI_API_ERR = -7;
	public static final int IMAPI_DATA_TOOLONG = -8;
	public static final int IMAPI_INIT_ERR = -9;
	public static final int IMAPI_IFSTATUS_INVALID = -10;
	public static final int IMAPI_GATEWAY_CONN_ERR = -11;
	public static final int SM_TYPE_NORMAL = 0;
	public static final int SM_TYPE_PDU = 2;
	private String dbUser = null;

	private String dbPwd = null;

	private String apiCode_ = null;

	private String dbUrl = null;

	private Connection conn = null;
	
	private Statement st = null;

	public int init(String dbIP, String dbUser, String dbPwd, String apiCode) {
		release();

		this.dbUser = dbUser;
		this.dbPwd = dbPwd;
		this.apiCode_ = apiCode;
		this.dbUrl = "jdbc:mysql://" + dbIP + "/im";

		return testConnect();
	}

	public int init(String dbIP, String dbUser, String dbPwd, String apiCode, String dbName) {
		release();

		this.dbUser = dbUser;
		this.dbPwd = dbPwd;
		this.apiCode_ = apiCode;
		this.dbUrl = "jdbc:mysql://" + dbIP + "/" + dbName;

		return testConnect();
	}

	public int sendSM(String mobile, String content, long smID) {
		return sendSM(new String[] { mobile }, content, smID, smID);
	}

	public int sendSM(String[] mobiles, String content, long smID) {
		return sendSM(mobiles, content, smID, smID);
	}

	public int sendSM(String[] mobiles, String content, long smID, long srcID) {
		return sendSM(mobiles, content, smID, srcID, "");
	}

	public int sendSM(String[] mobiles, String content, String sendTime, long smID, long srcID) {
		return sendSM(mobiles, content, smID, srcID, "", sendTime);
	}

	public int sendSM(String mobile, String content, long smID, String url) {
		return sendSM(new String[] { mobile }, content, smID, url);
	}

	public int sendSM(String[] mobiles, String content, long smID, String url) {
		return sendSM(mobiles, content, smID, smID, url);
	}

	public int sendSM(String[] mobiles, String content, long smID, long srcID, String url) {
		return sendSM(mobiles, content, smID, srcID, url, null);
	}

	public int sendSM(String[] mobiles, String content, long smID, long srcID, String url, String sendTime) {
		return sendSmAndPdu(mobiles, content, smID, srcID, url, sendTime, 0, 0, 0, "", "", "", 0, 0);
	}

	public int sendPDU(String[] mobiles, byte[] content, long smID, int msgFmt, int tpPID, int tpUdhi,
			String feeTerminalID, String feeType, String feeCode, int feeUserType) {
		return sendPDU(mobiles, content, smID, smID, msgFmt, tpPID, tpUdhi, feeTerminalID, feeType, feeCode,
				feeUserType);
	}

	public int sendPDU(String[] mobiles, byte[] content, long smID, long srcID, int msgFmt, int tpPID, int tpUdhi,
			String feeTerminalID, String feeType, String feeCode, int feeUserType) {
		String contentStr = binary2Hex(content);
		return sendSmAndPdu(mobiles, contentStr, smID, srcID, "", null, msgFmt, tpPID, tpUdhi, feeTerminalID, feeType,
				feeCode, feeUserType, 2);
	}

	private int sendSmAndPdu(String[] mobiles, String content, long smID, long srcID, String url, String sendTime,
			int msgFmt, int tpPID, int tpUdhi, String feeTerminalID, String feeType, String feeCode, int feeUserType,
			int smType) {
		if (this.dbUrl == null)
			return -9;
		if (mobiles == null || mobiles.length == 0) {
			return -6;
		}

		StringBuffer mobileBuf = new StringBuffer();
		for (int i = 0; i < mobiles.length; i++) {
			mobileBuf.append(",").append(mobiles[i]);
		}
		if (mobileBuf.length() > 1) {
			mobileBuf.delete(0, 1);
		} else {
			return -6;
		}

		String contenttmp = replaceSpecilAlhpa(content);
		if (contenttmp.length() < 1) {
			return -6;
		}

		if (contenttmp.length() > 2000) {
			contenttmp = contenttmp.substring(0, 2000);
		}

		if (!checkSmID(smID) || !checkSmID(srcID)) {
			return -6;
		}

		url = nullConvert(url).trim();
		if (url.length() > 0) {
			if (url.length() > 85) {
				return -8;
			}
			if ((url + contenttmp).length() > 120) {
				return -8;
			}
		}

		if (sendTime != null && !"".equals(sendTime) && isDateTime(sendTime) == null) {
			return -6;
		}

		int ret = checkGatConn();
		if (ret != 1) {

			if (ret == -7) {
				testConnect();
				ret = checkGatConn();
			}
			if (ret != 1) {
				return ret;
			}
		}

		if (2 == smType) {
			feeTerminalID = nullConvert(feeTerminalID).trim();
			feeType = nullConvert(feeType).trim();
			feeCode = nullConvert(feeCode).trim();
		}

		return mTInsert(mobileBuf.toString(), contenttmp, smID, srcID, url, sendTime, msgFmt, tpPID, tpUdhi,
				feeTerminalID, feeType, feeCode, feeUserType, smType);
	}

	public MOItem[] receiveSM() {
		return receiveSM(-1);
	}

	public MOItem[] receiveSM(int amount) {
		return receiveSM(-1L, amount);
	}

	public MOItem[] receiveSM(long srcID, int amount) {
		if (this.dbUrl == null) {
			return null;
		}
		if (this.conn == null) {
			int state = initConnect();
			if (state != 0) {
				return null;
			}
		}
		st = null;
		ResultSet rs = null;

		String getMoSql = "select * from api_mo_" + this.apiCode_;
		if (srcID != -1L) {
			getMoSql = getMoSql + " where SM_ID=" + srcID;
		}
		if (amount != -1) {
			getMoSql = getMoSql + " limit " + amount;
		} else {
			getMoSql = getMoSql + " limit 5000";
		}
		String delMoSql = "delete from api_mo_" + this.apiCode_ + " where AUTO_SN in (";

		ArrayList moList = new ArrayList();
		StringBuffer snBuf = new StringBuffer("-1");
		try {
			st = this.conn.createStatement();
			rs = st.executeQuery(getMoSql);
			while (rs.next()) {
				MOItem mItemtmp = new MOItem();
				mItemtmp.setSmID(rs.getLong("SM_ID"));
				mItemtmp.setContent(iso2gbk(rs.getString("CONTENT")));
				mItemtmp.setMobile(rs.getString("MOBILE"));
				mItemtmp.setMoTime(rs.getString("MO_TIME"));
				mItemtmp.setMsgFmt(rs.getInt("MSG_FMT"));

				snBuf.append(",").append(rs.getString("AUTO_SN"));
				moList.add(mItemtmp);
			}
			rs.close();
			st.executeUpdate(delMoSql + snBuf.toString() + ")");
		} catch (Exception e) {
			releaseConn();
			return null;
		} finally {
			closeStatment(st);
		}

		MOItem[] moItem = new MOItem[0];
		return (MOItem[]) moList.toArray(moItem);
	}

	public RPTItem[] receiveRPT() {
		return receiveRPT(-1);
	}

	public RPTItem[] receiveRPT(int amount) {
		return receiveRPT(-1L, amount);
	}

	public RPTItem[] receiveRPT(long smID, int amount) {
		if (this.dbUrl == null) {
			return null;
		}
		ResultSet rs = null;
		st = null;
		if (this.conn == null) {
			int state = initConnect();
			if (state != 0) {
				return null;
			}
		}
		String getRPTSql = "select * from api_rpt_" + this.apiCode_;
		if (smID != -1L) {
			getRPTSql = getRPTSql + " where SM_ID=" + smID;
		}
		if (amount != -1) {
			getRPTSql = getRPTSql + " limit " + amount;
		} else {
			getRPTSql = getRPTSql + " limit 5000";
		}
		String delRPTSql = "delete from api_rpt_" + this.apiCode_ + " where AUTO_SN in (";

		RPTItem[] rptItem = null;
		ArrayList rptList = new ArrayList();

		StringBuffer snBuf = new StringBuffer("-1");
		try {
			st = this.conn.createStatement();
			rs = st.executeQuery(getRPTSql);
			while (rs.next()) {
				RPTItem rptItemtmp = new RPTItem();
				rptItemtmp.setSmID(rs.getLong("SM_ID"));
				rptItemtmp.setCode(rs.getInt("RPT_CODE"));
				rptItemtmp.setMobile(rs.getString("MOBILE"));
				rptItemtmp.setDesc(iso2gbk(rs.getString("RPT_DESC")));
				rptItemtmp.setRptTime(rs.getString("RPT_TIME"));

				snBuf.append(",").append(rs.getString("AUTO_SN"));

				rptList.add(rptItemtmp);
			}
			rs.close();

			st.executeUpdate(delRPTSql + snBuf.toString() + ")");
		} catch (SQLException e) {
			releaseConn();

			return null;
		} catch (Exception ex) {
			return null;
		} finally {
			closeStatment(st);
		}

		rptItem = new RPTItem[0];
		return (RPTItem[]) rptList.toArray(rptItem);
	}

	public void release() {
		this.dbUser = null;
		this.dbPwd = null;
		this.apiCode_ = null;
		this.dbUrl = null;
		releaseConn();
	}

	private int testConnect() {
		st = null;
		ResultSet rs = null;

		try {
			if (this.conn != null) {
				releaseConn();
			}

			getConn();

			st = this.conn.createStatement();
		} catch (Exception e) {
			return -1;
		}
		try {
			String tableName = "api_mo_" + this.apiCode_;
			rs = st.executeQuery("select * from " + tableName + " limit 1");
			rs.close();
		} catch (SQLException e) {
			return -7;
		} finally {
			try {
				st.close();
			} catch (Exception ex) {
			}
		}

		return 0;
	}

	private int initConnect() {
		try {
			getConn();
		} catch (Exception e) {
			return -1;
		}

		return 0;
	}

	private void getConn() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
			this.conn = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void releaseConn() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (SQLException e) {
			}
		}

		this.conn = null;
	}

	private int mTInsert(String mobile, String content, long smID, long srcID, String url, String sendTime, int msgFmt,
			int tpPID, int tpUdhi, String feeTerminalID, String feeType, String feeCode, int feeUserType, int smType) {
		if (this.conn == null) {
			int state = initConnect();
			if (state != 0) {
				return -1;
			}
		}
		String sendMTSql = "";

		sendMTSql = "insert into api_mt_" + this.apiCode_
				+ " (SM_ID,SRC_ID,MOBILES,CONTENT,IS_WAP,URL,SEND_TIME,MSG_FMT,TP_PID,TP_UDHI,FEE_TERMINAL_ID,FEE_TYPE,FEE_CODE,FEE_USER_TYPE,SM_TYPE) values ("
				+ smID + "," + srcID + ",'" + mobile + "','" + content + "', ";

		if (url != null && url.trim().length() != 0) {
			sendMTSql = sendMTSql + "1,'" + url + "',";
		} else {
			sendMTSql = sendMTSql + "0,'',";
		}
		if (sendTime == null || "".equals(sendTime.trim())) {
			sendMTSql = sendMTSql + " null ,";
		} else {
			sendMTSql = sendMTSql + "'" + sendTime + "',";
		}

		sendMTSql = sendMTSql + msgFmt + "," + tpPID + "," + tpUdhi + ",'" + feeTerminalID + "','" + feeType + "','"
				+ feeCode + "'," + feeUserType + "," + smType + ")";

		st = null;
		try {
			st = this.conn.createStatement();
			st.executeUpdate(gb2Iso(sendMTSql));
		} catch (Exception e) {
			releaseConn();
			return -3;
		} finally {
			closeStatment(st);
		}
		return 0;
	}

	private void closeStatment(Statement st) {
		try {
			st.close();
		} catch (Exception e) {
		}
	}

	private String replaceSpecilAlhpa(String content) {
		if (content == null || content.trim().length() == 0) {
			return "";
		}
		String spec_char = "\\'";
		String retStr = "";
		for (int i = 0; i < content.length(); i++) {
			if (spec_char.indexOf(content.charAt(i)) >= 0) {
				retStr = retStr + "\\";
			}
			retStr = retStr + content.charAt(i);
		}
		return retStr;
	}

	private boolean checkSmID(long smID) {
		if (smID < 0L || smID > 99999999L) {
			return false;
		}
		return true;
	}

	private String gb2Iso(String str) {
		if (str == null) {
			return "";
		}
		String temp = "";
		try {
			byte[] buf = str.trim().getBytes("GBK");
			temp = new String(buf, "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			temp = str;
		}
		return temp;
	}

	private int checkGatConn() {
		int ret = 1;
		ResultSet rs = null;
		st = null;
		if (this.conn == null) {
			initConnect();
		}

		String sql = "select if_status,conn_succ_status from tbl_api_info as api where api.if_code='" + this.apiCode_
				+ "' limit 1";

		try {
			st = this.conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				if ("2".equals(rs.getString("if_status"))) {
					ret = -10;
				}
				if ("0".equals(rs.getString("conn_succ_status"))) {
					ret = -11;
				}
			}
			rs.close();
		} catch (SQLException e) {
			return -7;
		} finally {
			try {
				st.close();
			} catch (Exception ex) {
			}
		}

		return ret;
	}

	public static String isDateTime(String str) {
		if (str == null)
			return null;
		if (str.length() != 19) {
			return null;
		}
		int temp = Integer.parseInt(str.substring(5, 7));
		if (12 < temp || temp < 1) {
			return null;
		}
		temp = Integer.parseInt(str.substring(8, 10));
		if (31 < temp || temp < 1) {
			return null;
		}
		temp = Integer.parseInt(str.substring(11, 13));
		if (23 < temp || temp < 0)
			return null;
		temp = Integer.parseInt(str.substring(14, 16));
		if (59 < temp || temp < 0)
			return null;
		temp = Integer.parseInt(str.substring(17, 19));
		if (59 < temp || temp < 0)
			return null;
		Date returnDate = null;
		DateFormat df = DateFormat.getDateInstance();

		try {
			returnDate = df.parse(str);
			return returnDate.toString();
		} catch (Exception e) {
			return null;
		}
	}

	private String binary2Hex(byte[] bys) {
		if (bys == null || bys.length < 1) {
			return null;
		}
		StringBuffer sb = new StringBuffer(100);

		for (int i = 0; i < bys.length; i++) {
			if (bys[i] >= 16) {
				sb.append(Integer.toHexString(bys[i]));
			} else if (bys[i] >= 0) {
				sb.append("0" + Integer.toHexString(bys[i]));
			} else {
				sb.append(Integer.toHexString(bys[i]).substring(6, 8));
			}
		}
		return sb.toString();
	}

	private String iso2gbk(String str) {
		if (str == null) {
			return "";
		}
		String temp = "";
		try {
			byte[] buf = str.trim().getBytes("iso8859-1");
			temp = new String(buf, "GBK");
		} catch (UnsupportedEncodingException e) {
			temp = str;
		}
		return temp;
	}

	private String nullConvert(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}
}