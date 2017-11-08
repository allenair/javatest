package allen.iotplatform.testtool;

public class TestUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String genMockData(UpHardAnalysis bean) {
		String baseStr = "";
		StringBuilder buf = new StringBuilder();
		
		// 0~7
		buf.append("00000000");  // P0~P7
		
		// 8~15
		buf.append(getValueDefaultZero(bean.getDz())); // 8 <--> P15
		buf.append(getValueDefaultZero(bean.getDcl())); // 9 <--> P14
		buf.append(getValueDefaultZero(bean.getDw())); // 10 <--> P13
		buf.append(getValueDefaultZero(bean.getDol())); // 11 <--> P12
		buf.append(getValueDefaultZero(bean.getDo_p())); // 12 <--> P11
		buf.append(getValueDefaultZero(bean.getRun())); // 13 <--> P10
		buf.append(getValueDefaultZero(bean.getIns())); // 14 <--> P9
		buf.append(getValueDefaultZero(bean.getNav())); // 15 <--> P8
		
		// 16~23
		buf.append("0000"); // 16 ~ 19 
		buf.append(getValueDefaultZero(bean.getDown())); // 20 <--> P19
		buf.append(getValueDefaultZero(bean.getUp())); // 21 <--> P18
		buf.append(getValueDefaultZero(bean.getCb())); // 22 <--> P17
		buf.append(getValueDefaultZero(bean.getEfo())); // 23 <--> P16
		
		// 24~39
		buf.append(getStringByInt(bean.getFl(), 2)); // FL 2byte
		
		// 40~71
		buf.append(getStringByInt(bean.getCnt(), 4)); // CNT 4byte
		
		// 72~103
		buf.append(getStringByInt(bean.getDdfw(), 4)); // DDFW 4byte
		
		// 104~135
		buf.append(getStringByInt(bean.getHxxh(), 4)); // HXXH 4byte
		
		// 136~143
		buf.append(getValueDefaultZero(bean.getLs1())); // 136 <--> P143
		buf.append("0"); // 136 
		buf.append(getValueDefaultZero(bean.getLv1())); // 138 <--> P141
		buf.append(getValueDefaultZero(bean.getEro())); // 139 <--> P140
		buf.append(getValueDefaultZero(bean.getTci())); // 140 <--> P139
		buf.append(getValueDefaultZero(bean.getDfc())); // 141 <--> P138
		buf.append(getValueDefaultZero(bean.getSe())); // 142 <--> P137
		buf.append(getValueDefaultZero(bean.getEs())); // 143 <--> P136
		
		// 144~151
		buf.append(getValueDefaultZero(bean.getRdol())); // 144 <--> P151
		buf.append(getValueDefaultZero(bean.getPks())); // 145 <--> P150
		buf.append(getValueDefaultZero(bean.getEfk())); // 146 <--> P149
		buf.append(getValueDefaultZero(bean.getDos())); // 147 <--> P148
		buf.append(getValueDefaultZero(bean.getLrd())); // 148 <--> P147
		buf.append(getValueDefaultZero(bean.getDcb())); // 149 <--> P146
		buf.append(getValueDefaultZero(bean.getDob())); // 150 <--> P145
		buf.append(getValueDefaultZero(bean.getLs2())); // 151 <--> P144
		
		// 152~159
		buf.append("000"); // 152 ~ 154 
		buf.append(getValueDefaultZero(bean.getRdoo())); // 155 <--> P156	
		buf.append(getValueDefaultZero(bean.getRear_en())); // 156 <--> P155	
		buf.append(getValueDefaultZero(bean.getRdcb())); // 157 <--> P154	
		buf.append(getValueDefaultZero(bean.getRdob())); // 158 <--> P153
		buf.append(getValueDefaultZero(bean.getRdcl())); // 159 <--> P152
		
		// 160~167
		buf.append(getStringByInt(bean.getLogic_err(), 1)); // Logic_err 1byte
		
		// 168~175
		buf.append(getStringByInt(bean.getShow_left(), 1)); // L 1byte
		
		// 176~183
		buf.append(getStringByInt(bean.getShow_right(), 1)); // R 1byte
		
		// 184~191
		buf.append(getStringByInt(bean.getBoard_type(), 1)); // Board_type 1byte
		
		// 192~223
		buf.append(getStringByInt(bean.getLast_count(), 4)); // Last_count 4byte
		
		// 224~255
		buf.append(getStringByInt(bean.getTotal_time(), 4)); // Total_time 4byte
		
		// 256~263
		buf.append(getStringByInt(bean.getDriver_err(), 1)); // Driver_err 1byte
		
		// 264~271
		buf.append(getStringByInt(bean.getLogic_lock(), 1)); // Logic_lock 1byte
		
		// 272~279
		buf.append(getStringByInt(bean.getSys_model(), 1)); // Sys_model 1byte
		
		// 280~311
		buf.append(getStringByInt(bean.getXh_time(), 4)); // Xh_time 4byte
		
		// 312~343
		buf.append(getStringByInt(bean.getArm_code(), 4)); // Arm_code 4byte
		
		// 344~375
		buf.append(getStringByInt(bean.getDsp_code(), 4)); // Dsp_code 4byte
		
		// 376~383
		buf.append("0"); // 376 <--> 
		buf.append(getValueDefaultZero(bean.getLock_broken())); // 377 <--> P382
		buf.append(getValueDefaultZero(bean.getStop_fault())); // 378 <--> P381
		buf.append(getValueDefaultZero(bean.getDown_switch())); // 379 <--> P380
		buf.append(getValueDefaultZero(bean.getUp_switch())); // 380 <--> P379
		buf.append(getValueDefaultZero(bean.getClose_fault())); // 381 <--> P378
		buf.append(getValueDefaultZero(bean.getOpen_fault())); // 382 <--> P377
		buf.append(getValueDefaultZero(bean.getSafe_circle())); // 383 <--> P376
		
		// 384~391
		buf.append("0000"); // 384~387
		buf.append(getValueDefaultZero(bean.getGo_down())); // 388 <--> P387
		buf.append(getValueDefaultZero(bean.getGo_top())); // 389 <--> P386
		buf.append("0"); // 390 <--> 
		buf.append(getValueDefaultZero(bean.getSpeed_fault())); // 391 <--> P384
		
		// 392~407
		for(int i=392; i<=407; i++) {
			buf.append("0");
		}
		
		// 408~415
		buf.append(getStringByInt(bean.getDriver_fault(), 1)); // driver_fault 1byte
		
		// 416~423
		buf.append(getStringByInt(bean.getLogic_fault(), 1)); // logic_fault 1byte
		
		// 424~431
		buf.append(getStringByInt(bean.getLogic_status(), 1)); // logic_status 1byte
		
		// 432~503
		for(int i=432; i<=503; i++) {
			buf.append("0");
		}
		
		// 504~511
		buf.append(getStringByInt(bean.getVer_code(), 1)); // Ver_code 1byte
		
		return baseStr;
	}
	
	private static String getValueDefaultZero(String val) {
		if(val==null || "".equals(val)) {
			return "0";
		}
		return val;
	}
	
	// 按照小端法处理,只有获取整数(涉及多个字节问题)时候需要这样处理
	// 序列是，。。。0x78 0x56 0x34 0x12。。。，按照0x12345678来解析，反之亦然
	private static String getStringByInt(int num, int len) {
		StringBuilder res = new StringBuilder();
		if (num < 0) {
			num = 0;
		}

		String numStr = Integer.toBinaryString(num);
		int strLen = numStr.length();
		for (int i = 0; i < len * 8 - strLen; i++) {
			numStr = "0" + numStr;
		}

		for (int i = len; i > 0; i--) {
			for (int k = 0; k < 8; k++) {
				int pose = (len - 1) * 8 + k;
				res.append(numStr.substring(pose, pose + 1));
			}
		}

		return res.toString();
	}
}
