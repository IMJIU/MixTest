package com.util.upload_to_linux.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class UploadOperate extends FindOperator {
	/**
	 * 读取文件中需要上传的文件  上传到linux
	 * @param classFilePath
	 */
	public static void doUploadClass(String classFilePath) {
		loopServer((channelSftp) -> {
			try {
				String tmpFile = classFilePath.substring(classFilePath.indexOf("classes") + 8);
				String tmpDir = tmpFile.substring(0, tmpFile.lastIndexOf("\\")).replaceAll("\\\\", "/");
				_uploadToLinux(linux_class_Path + tmpDir, classFilePath,channelSftp);
				System.out.println("finished");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		System.out.println("finished");
	}
	/**
	 * 上传war
	 * @param dir
	 * @param file
	 */
	public static void uploadWarToLinux(String dir,String file) {
		System.out.println(file);
		try {
			String war = Files.list(Paths.get(file)).filter(f -> f.getFileName().toString().endsWith("war")).findFirst().get().toString();
			if (war != null) {
				uploadToLinux(dir, war);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static void _mkDir(String dir) {
		loopServer((channelSftp) -> {
			try {
				if (!set.contains(dir)) {
					System.out.print("mkdir " + dir);
					executeCommand(channelSftp.getSession().getHost(),"mkdir " + dir);
					System.out.println("->mk finished");
					set.add(dir);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}
	/**
	 * 上传war
	 * @param dir
	 * @param file
	 */
	public static void uploadDirToLinux(String baseDir,File file) {
		String name1 = baseDir+file.getAbsolutePath().replace(Base.local_platform_webapp_Path, "").replaceAll("\\\\", "/");
		String path1 = name1.substring(0, name1.lastIndexOf("/"));
//		System.out.println("path1 : "+path1+" dir:"+file.isDirectory());
		if(file.isDirectory()){
			_mkDir(path1);
			for (File f : file.listFiles()) {
				String name = baseDir+f.getAbsolutePath().replace(Base.local_platform_webapp_Path, "").replaceAll("\\\\", "/");
				String path = name.substring(0, name.lastIndexOf("/"));
//				System.out.println("path : "+path+" dir:"+f.isDirectory());
				_mkDir(path);
				if(f.isDirectory()){
					uploadDirToLinux(baseDir, f);
				}else{
					uploadToLinux(path, f.getAbsolutePath());
				}
			}
		}else{
			uploadToLinux(path1, file.getAbsolutePath());
		}
	}
	/**
	 * 上传文件
	 * @param dir
	 * @param file
	 */
	public static void uploadToLinux(String dir,String file) {
		loopServer((channelSftp) -> {
			try {
				_uploadToLinux(dir,file,channelSftp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	/**
	 * 读取文件中需要上传的文件  上传到linux
	 */
	public static void doUploadHtmlFileListToLinux() {
		loopServer((session) -> {
			try {
				String path = null;
				for (String filePath : Files.readAllLines(Paths.get(uploadHtmlFilePath))) {
					path = filePath.substring(filePath.indexOf("webapp") + 6).replaceAll("\\\\", "/");
					path = path.substring(0, path.lastIndexOf("/"));
					System.out.println(linux_webapp_Path + path);
					_uploadToLinux(linux_webapp_Path + path, filePath,session);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		System.out.println("finished");
	}
	private static FileOutputStream uploadHtmlListOutput = null;
	/**
	 * 搜索需要上传的html
	 * @param file
	 * @return
	 */
	public static void findUploadHtmlWriteToFile(String file) {
		try {
			System.out.println(uploadHtmlFilePath);
			System.out.println("i get!"+file);
			try {
				if(uploadHtmlListOutput == null)
					uploadHtmlListOutput = new FileOutputStream(uploadHtmlFilePath);
				uploadHtmlListOutput.write((file+"\n").getBytes("utf-8"));
				uploadHtmlListOutput.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}