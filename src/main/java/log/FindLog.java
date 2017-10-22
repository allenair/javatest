package log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FindLog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FindLog test = new FindLog();

		if(args.length!=4){
			test.find("d:/sinyd_qasp_all.log", "d:/out.txt", "ERROR", "2013-09-01 17:00:00");
		}else{
			test.find(args[0],args[1],args[2],args[3]);
		}
	}

	public void find(String inFile, String outFile, String level, String timeStr) {
		long time = 0;
		if (timeStr == null || timeStr.length() == 0) {
			time = 0;
		} else {
			time = str2Long(timeStr);
		}

		try {
			BufferedReader inf = new BufferedReader(new FileReader(inFile));
			PrintWriter outf = new PrintWriter(new FileOutputStream(outFile),
					true);
			String line = "";
			boolean flag = false;
			while ((line = inf.readLine()) != null) {
				flag = isOut(line, level, time, flag);
				if (flag) {
					outf.println(line);
				}
			}

			outf.close();
			inf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean isOut(String lineStr, String level, long time,
			boolean nowFlag) {
		boolean flag = false;
		String tmp = lineStr.trim();
		if (tmp.length() == 0) {
			return nowFlag;
		}
		if (tmp.charAt(0) != '[') {
			return nowFlag;
		} else {
			tmp = tmp.substring(1, tmp.indexOf(']')).trim();
			String[] tmpArray = tmp.split("\\s");

			if (level.equalsIgnoreCase(tmpArray[2])) {
				flag = true;
			}

			if (time > 0) {
				long lineTime = str2Long(tmpArray[0] + " " + tmpArray[1]);
				if (lineTime > time) {
					flag = flag && true;
				} else {
					flag = flag && false;
				}
			}
		}

		return flag;
	}

	private long str2Long(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(time).getTime();
		} catch (ParseException e) {
			System.out.println("ERROR format: " + time);
			e.printStackTrace();
		}
		return 0;
	}
}
