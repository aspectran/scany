/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.scany.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 파일 처리에 필요한 메쏘드의 모음입니다.
 * @author Jeong Ju Ho (http://labs.tistory.com)
 * @version <pre>
 * 2006/08/01 
 * 2005/04/13 
 * 2003/04/26 초기 버전</pre>
 */
public class FileUtils {

	/**
	 * 파일의 사이즈를 읽기 편한 포맷으로 변환한다.
	 * 파일 사이즈 단위는 자동으로 결정된다.
	 * @param fileSize 파일 사이즈
	 * @return String
	 */
	public static String formatFileSize(long fileSize) {
		return formatFileSize(fileSize, null, 0);
	}
	
	/**
	 * 파일의 사이즈를 읽기 편한 포맷으로 변환한다.
	 * @param fileSize 파일 사이즈
	 * @param sizeUnit 단위
	 * @return String
	 */
	public static String formatFileSize(long fileSize, SizeUnit sizeUnit) {
		return formatFileSize(fileSize, sizeUnit, 0);
	}

	/**
	 * 파일의 사이즈를 읽기 편한 포맷으로 변환한다.
	 * <pre>
	 * 단위: { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" }
	 * </pre>
	 * @param fileSize 파일 사이즈
	 * @param sizeUnit 단위
	 * @param decimalFigures 소수점 이하 자리수
	 * @return String
	 */
	public static String formatFileSize(long fileSize, SizeUnit sizeUnit, int decimalFigures) {
		final String[] units = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
		double dSize = fileSize;

		int unitIndex = -1;

		if(sizeUnit != null) {
			String unitString = sizeUnit.toString();

			for(int i = 0; i < units.length; i++) {
				if(units[i].equals(unitString)) {
					unitIndex = i;
					break;
				}
			}
		}

		if(unitIndex == -1) {
			unitIndex = 0;

			double dTemp = fileSize / 1024;

			while(dTemp > 1.0) {
				dTemp /= 1024;
				unitIndex++;
			}
		}

		for(int i = 0; i < unitIndex; i++)
			dSize /= 1024;

		StringBuffer pattern = new StringBuffer("0");

		if(decimalFigures > 0)
			pattern.append(".");

		for(int i = 0; i < decimalFigures; i++)
			pattern.append("#");

		DecimalFormat df = new DecimalFormat(pattern.toString());
		String result = df.format(dSize);

		return result + units[unitIndex];
	}

	/**
	 * 문자열 형식의 파일 사이즈 포맷을 바이트 크기의 숫자로 반환한다.
	 * <pre>
	 * 가능 수치단위 문자: K, KB, M, MB, G, GB, T, TB
	 * </pre>
	 * @param sizeString
	 * @param defaultSize
	 * @return
	 */
	public static long formatSizeToBytes(String sizeString, long defaultSize) {
		if(sizeString == null)
			return defaultSize;
		
		int multiplier = 1;

		if(sizeString.endsWith("K") || sizeString.endsWith("KB")) {
			multiplier = 1024;
		} else if(sizeString.endsWith("M") || sizeString.endsWith("MB")) {
			multiplier = 1024 * 1024;
		} else if(sizeString.endsWith("G") || sizeString.endsWith("GB")) {
			multiplier = 1024 * 1024 * 1024;
		} else if(sizeString.endsWith("T") || sizeString.endsWith("TB")) {
			multiplier = 1024 * 1024 * 1024 * 1024;
		}

		if(multiplier != 1) {
			int sizeUnitLen = sizeString.endsWith("B") ? 2 : 1;
			sizeString = sizeString.substring(0, sizeString.length() - sizeUnitLen);
		}

		long size = 0;

		try {
			size = Long.parseLong(sizeString);
		} catch(NumberFormatException nfe) {
			size = defaultSize;
			multiplier = 1;
		}

		return (size * multiplier);
	}

	/**
	 * 파일을 복사한다.
	 * @param f1 파일명1
	 * @param f2 파일명2
	 * @throws IOException
	 */
	public static void copy(String f1, String f2) throws IOException {
		File in = new File(f1);
		File out = new File(f2);

		copy(in, out);
	}

	/**
	 * 파일을 복사한다.
	 * @param f1 파일1
	 * @param f2 파일2
	 * @throws IOException
	 */
	public static void copy(File f1, File f2) throws IOException {
		InputStream input = new FileInputStream(f1);
		OutputStream output = new FileOutputStream(f2);

		final byte[] buffer = new byte[256 * 1024];
		int len;

		while((len = input.read(buffer)) != -1) {
			output.write(buffer, 0, len);
		}

		output.flush();

		input.close();
		output.close();
	}

	/**
	 * 원본 디렉토리내의 파일과 하위 디렉토리를 대상 디렉토리에 모두 복사한다.
	 * 대상 디렉토리 이미 존재하는 파일과 폴더는 덮어 씌어진다.
	 * @param f1 원본 디렉토리
	 * @param f2 대상 디렉토리
	 * @throws IOException
	 */
	public static void copyAll(String f1, String f2) throws IOException {
		File in = new File(f1);
		File out = new File(f2);

		copyAll(in, out);
	}

	/**
	 * 원본 디렉토리내의 파일과 하위 디렉토리를 대상 디렉토리에 모두 복사한다.
	 * 대상 디렉토리 이미 존재하는 파일과 폴더는 덮어 씌어진다.
	 * @param f1 원본 디렉토리
	 * @param f2 대상 디렉토리
	 * @throws IOException
	 */
	public static void copyAll(File f1, File f2) throws IOException {
		String[] files = f1.list();

		File f3, f4;

		for(int i = 0; i < files.length; i++) {
			f3 = new File(f1.toString(), files[i]);
			f4 = new File(f2.toString(), files[i]);

			if(f3.isDirectory()) {
				f4.mkdir();
				copyAll(f3, f4);
			} else
				copy(f3, f4);
		}
	}

	/**
	 * 파일을 옮긴다.
	 * @param f1 파일1
	 * @param f2 파일2
	 * @throws IOException
	 */
	public static void move(File f1, File f2) throws IOException {
		f1.renameTo(f2);
	}

	/**
	 * 지정한 디렉토리 내의 모든 파일 및 디렉토리를 삭제한다.
	 * 지정한 디렉토리는 삭제하지 않는다.
	 * @param dir target directory
	 */
	public static void deleteAllFile(File dir) {
		String[] files = dir.list();

		for(int i = 0; i < files.length; i++) {
			File delFile = new File(dir, files[i]);

			if(delFile.isDirectory()) {
				deleteAllFile(delFile);
				delFile.delete();
			} else {
				delFile.delete();
			}
		}
	}

	/**
	 * 파일 전체를 문자열로 읽어들인다.
	 * @param file File 오브젝트
	 * @return String
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		Reader reader = null;
		final char[] buffer = new char[1024];
		int len;

		StringBuffer sb = new StringBuffer();
		reader = new FileReader(file);
		
		while((len = reader.read(buffer)) != -1) {
			sb.append(buffer, 0, len);
		}
		
		reader.close();

		return sb.toString();
	}

	/**
	 * 텍스트 내용을 파일에 기록한다.
	 * @param file File
	 * @param data 기록할 텍스트 내용
	 * @param append 파일의 끝에 추가할지 여부, 아니면 신규 작성
	 * @throws IOException
	 */
	public static void writeFile(File file, String data, boolean append) throws IOException {
		Writer out = new FileWriter(file, append);
		out.write(data, 0, data.length());
		out.close();
	}

	/**
	 * 텍스트 내용을 파일에 기록한다.
	 * @param file File
	 * @param data 기록할 텍스트 내용
	 */
	public static void writeFile(File file, String data) throws IOException {
		writeFile(file, data, false);
	}

    /**
     * 텍스트 내용을 파일에 기록한다.
     * @param file File
     * @param data 기록할 텍스트 내용
     * @param append 파일의 끝에 추가할지 여부, 아니면 신규 작성
     * @param encoding 문자코드
     */
	public static void writeFile(File file, String data, String encoding) throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
		out.write(data);
        out.close();
	}
	
	/**
	 * 파일명을 반환한다.(확장자포함)
	 * @param filePath 경로를 포함한 파일명
	 * @return String
	 */
	public static String getFileName(String filePath) {
		int forwardSlash = filePath.lastIndexOf("/");
		int backwardSlash = filePath.lastIndexOf("\\");
		
		if(forwardSlash != -1 && forwardSlash > backwardSlash)
			filePath = filePath.substring(forwardSlash + 1, filePath.length());
		else if(backwardSlash != -1 && backwardSlash >= forwardSlash)
			filePath = filePath.substring(backwardSlash + 1, filePath.length());
		
		return filePath;
	}
	
	/**
	 * 파일명을 제외한 경로만 반환한다.
	 * @param filePath 파일의 전체 경로
	 * @return String
	 */
	public static String getPathWithoutFileName(String filePath) {
		int forwardSlash = filePath.lastIndexOf("/");
		int backwardSlash = filePath.lastIndexOf("\\");

		if(forwardSlash != -1 && forwardSlash > backwardSlash)
			filePath = filePath.substring(0, forwardSlash);
		else if(backwardSlash != -1 && backwardSlash >= forwardSlash)
			filePath = filePath.substring(0, backwardSlash);

		return filePath;
	}

	/**
	 * 파일명에서 확장자를 반환한다.(소문자)
	 * @param fileName 파일명
	 * @return String
	 */
	public static String getFileExtention(String fileName) {
		fileName = getFileName(fileName);

		int pointIndex = fileName.lastIndexOf(".");

		if(pointIndex != -1)
			return fileName.substring(pointIndex + 1).toLowerCase();

		return StringUtils.EMPTY;
	}

	/**
	 * 확장자를 제외한 파일명을 반환한다.
	 * @param filePath 경로를 포함한 파일명
	 * @return String
	 */
	public static String getFileNameWithoutExtention(String filePath) {
		String fileName = getFileName(filePath);

		int pointIndex = fileName.lastIndexOf(".");

		if(pointIndex != -1)
			fileName = fileName.substring(0, pointIndex);

		return fileName;
	}

	/**
	 * 특정 디렉토리내에서 중복되지 않는 파일명을 반환한다.
	 * 중복 파일명이 존재할 경우 파일명  뒤에 숫자가 붙는다.
	 * 지정한 디렉토리내에 "example.txt"라는 파일명을 가진 파일이 이미 존재할 경우
	 * "example1.txt"라는 새로운 파일명을 반환한다.
	 * @param path 디렉토리
	 * @param fileName 파일명
	 * @return String
	 */
	public static String makeUniqueFileName(String path, String fileName) {
		return makeUniqueFileName(path, fileName, null);
	}

	/**
	 * 특정 디렉토리내에서 중복되지 않는 파일명을 반환한다.
	 * 중복 파일명이 존재할 경우 파일명  뒤에 숫자가 붙는다.
	 * 지정한 디렉토리내에 "example.txt"라는 파일명을 가진 파일이 이미 존재할 경우
	 * "example1.txt"라는 새로운 파일명을 반환한다.
	 * @param path 디렉토리
	 * @param fileName 파일명
	 * @param extentionSeparator 파일명과 확장자간의 분리 문자열
	 * @return String
	 */
	public static String makeUniqueFileName(String path, String fileName, String extentionSeparator) {
		String separator = (extentionSeparator == null) ? "." : extentionSeparator;

		int fromIndex = fileName.lastIndexOf(separator);

		String name;
		String ext;

		if(fromIndex == -1) {
			name = fileName;
			ext = StringUtils.EMPTY;
		} else {
			name = fileName.substring(0, fromIndex);
			ext = fileName.substring(fromIndex + separator.length()).toLowerCase();
		}

		String newFileName = name + separator + ext;

		File f = new File(path, newFileName);
		int cnt = 0;

		while(f.exists() && cnt++ < 9999) {
			newFileName = name + cnt + separator + ext;
			f = new File(f.getParent(), newFileName);
		}

		return newFileName;
	}

	/**
	 * 특정 디렉토리내에서 중복되지 않는 시스템 파일명을 반환한다.
	 * 파일 확장자는 '_' 문자로 구분한다.
	 * 형식) 1111111111_txt
	 * @param path 디렉토리
	 * @param fileName 파일명
	 * @return String 새로운 파일명
	 */
	public static String makeUniqueSafetyFileName(String path, String fileName) {
		String time = new Long(System.currentTimeMillis()).toString();
		String ext = getFileExtention(fileName);
		String separator = "_";

		return makeUniqueFileName(path, time + separator + ext, separator);
	}

	/**
	 * Size unit of computer file
	 * 
	 * <p>Created: 2008. 04. 16 오후 6:42:23</p>
	 * 
	 * @author Gulendol
	 *
	 */
	public static class SizeUnit {
		/**
		 * Byte
		 */
		public static final SizeUnit B;
		/**
		 * Kilo Byte
		 */
		public static final SizeUnit KB;
		/**
		 * Mega Byte
		 */
		public static final SizeUnit MB;
		/**
		 * Giga Byte
		 */
		public static final SizeUnit GB;
		/**
		 * Tera Byte
		 */
		public static final SizeUnit TB;
		/**
		 * Peta Byte
		 */
		public static final SizeUnit PB;
		/**
		 * Exa Byte
		 */
		public static final SizeUnit EB;
		/**
		 * Zetta Byte
		 */
		public static final SizeUnit ZB;
		/**
		 * Yotta Byte
		 */
		public static final SizeUnit YB;

		private static final Map types;

		static {
			B = new SizeUnit("Byte");
			KB = new SizeUnit("Kilo Byte");
			MB = new SizeUnit("Mega Byte");
			GB = new SizeUnit("Giga Byte");
			TB = new SizeUnit("Tera Byte");
			PB = new SizeUnit("Peta Byte");
			EB = new SizeUnit("Exa Byte");
			ZB = new SizeUnit("Zetta Byte");
			YB = new SizeUnit("Yotta Byte");

			types = new HashMap();
			types.put(B.toString(), B);
			types.put(KB.toString(), KB);
			types.put(MB.toString(), MB);
			types.put(GB.toString(), GB);
			types.put(TB.toString(), TB);
			types.put(PB.toString(), PB);
			types.put(EB.toString(), EB);
			types.put(ZB.toString(), ZB);
			types.put(YB.toString(), YB);
		}

		private final String type;

		private SizeUnit(String type) {
			this.type = type;
		}

		public String toString() {
			return type;
		}

		public static SizeUnit valueOf(String type) {
			return (SizeUnit)types.get(type);
		}
	}
}
