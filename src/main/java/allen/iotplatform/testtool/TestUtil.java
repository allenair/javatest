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
