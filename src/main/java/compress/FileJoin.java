package compress;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileJoin {

	private String path = "";

	public FileJoin(String path) {
		this.path = path;
	}

	public void join(List<String> fileNameList, String outFileName)
			throws Exception {
		PrintWriter pw = new PrintWriter(this.path + outFileName, "UTF-8");
		BufferedReader fin;
		String tmp;
		boolean fileStartFlag = true;
		for (String inFile : fileNameList) {
			fin = new BufferedReader(new FileReader(this.path + inFile));
			fileStartFlag = true;
			while ((tmp = fin.readLine()) != null) {
				if (fileStartFlag) {
					tmp = getWithoutBom(tmp);
					fileStartFlag = false;
				}
				if (tmp.trim().length() > 0) {
					pw.println(tmp);
				}
			}

			fin.close();
			System.out.println("=====" + inFile + "===FIN===");
		}
		pw.close();
	}

	public void wipeComment(String fileName, String wipedFile) throws Exception {
		StringBuilder sb = new StringBuilder();
		String tmp;
		BufferedReader fin = new BufferedReader(new FileReader(this.path + fileName));
		while ((tmp = fin.readLine()) != null) {
			sb.append(tmp).append(" \n ");
		}
		fin.close();

		String content = sb.toString();
		int startIndex = 0, endIndex = 0, totallen = content.length();
		while ((startIndex = content.indexOf("/*", startIndex)) >= 0) {
			endIndex = content.indexOf("*/", startIndex + 2);
			if (endIndex < 0) {
				endIndex = totallen;
				content = content.substring(0, startIndex);
			} else {
				content = content.substring(0, startIndex) + content.substring(endIndex + 2);
			}
		}

		PrintWriter pw = new PrintWriter(this.path + wipedFile, "UTF-8");
		pw.println(content);
		pw.close();
	}

	private String getWithoutBom(String str) throws Exception {
		byte[] allbytes = str.getBytes("UTF-8");
		if (allbytes.length > 0 && Integer.toHexString(0xFF & allbytes[0]).equalsIgnoreCase("ef")) {
			return new String(allbytes, 3, allbytes.length - 3, "UTF-8");
		}
		return str;
	}

	public static void main(String[] args) throws Exception {
		String path = FileJoin.class.getResource("/").getPath();

		String postfix = "";
		if (args.length >= 1) {
			postfix = args[0];
		}
		postfix = postfix.equals("") ? "" : "." + postfix;

		File fpath = new File(path);
		fpath = fpath.getParentFile();

		boolean flag = false;
		for (File tmpFile : fpath.listFiles()) {
			if (tmpFile.isDirectory() && "srcfiles".equals(tmpFile.getName())) {
				fpath = tmpFile;
				path = fpath.getAbsolutePath()+"/";
				flag = true;
			}
		}

		if (!flag) {
			throw new Exception("There is NOT SRC FILE directory!!");
		}

		List<String> fileNameList = new ArrayList<String>();
		for (File tmpFile : fpath.listFiles()) {
			if (tmpFile.isFile()) {
				fileNameList.add(tmpFile.getName());
			}
		}

		FileJoin test = new FileJoin(path);
		test.join(fileNameList, "join_all" + postfix);
		test.wipeComment("join_all" + postfix, "join_all_wipe" + postfix);

	}
}
