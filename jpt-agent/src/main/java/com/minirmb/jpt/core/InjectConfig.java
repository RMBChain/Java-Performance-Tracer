package com.minirmb.jpt.core;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 
 * the priority of exclude is higher than include.
 * 
 * @author Spooner
 *
 */
public class InjectConfig {

	private final static String InjectConfigPrefix = "++InjectConfig ";
	private Set<String> exclude= new HashSet<>();// priority
	private Set<String> include = new HashSet<>();// priority 2

	public Set<String> getExclude() {
		return exclude;
	}

	public Set<String> getInclude() {
		return include;
	}


	public static InjectConfig GetInjectConfig(String ip, int port) throws IOException {
		byte[] configBytes = GetConfigFromRemote(ip, port);
		String injectConfigStr = new String(configBytes);
		JPTLogger.log("*****injectConfigStr : " + injectConfigStr + "\n");
		return InjectConfig.Parse(injectConfigStr);
	}

	public static byte[] GetConfigFromRemote(String ip, int port) throws IOException {
		Map<String, String> env = System.getenv();
		ByteBuffer icBuffer = null;
		try (SocketChannel clientChannel = SocketChannel.open()) {
			boolean connected = clientChannel.connect(new InetSocketAddress(ip, port));
			if (connected) {
				clientChannel.write(ByteBuffer.wrap(InjectConfigPrefix.getBytes()));
				icBuffer = ByteBuffer.allocate(2000);
				int serverReceivedLength = clientChannel.read(icBuffer);
				JPTLogger.log("***** getInjectConfig length : " + serverReceivedLength + "\n");
			}
		} catch (IOException e) {
			JPTLogger.log("*** Error when get config from ", ip, ":", port);
			throw e;
		}

		return icBuffer.array();
	}

	private static InjectConfig Parse(String ic) {
		InjectConfig instance = new InjectConfig();
		// injectRangeConfigFile
		Stream.of(ic.split("\n")).filter(line -> line.trim().length() > 0).forEach(line -> {
			String[] lineEle = line.split("=");
			if (lineEle.length > 1) {
				String content = lineEle[1].trim().replace(".", "/");
				switch (lineEle[0].trim()) {
				case "exclude":
					instance.exclude.add(content);
					break;
				case "include":
					instance.include.add(content);
					break;
				default:

				}
			}
		});

		instance.exclude.addAll(Arrays.asList(new String[] { "java", "jdk", "sun", "javax" }));
		instance.exclude.add(TransformEntry.class.getPackage().getName().replace(".", "/"));
		return instance;
	}

	public boolean shouldInject(String className) {
		boolean result = false;
		// 忽略包
		if (!isInPackages(className, exclude)) {
				if (isInPackages(className, include)) {
					result = true;
				}
				JPTLogger.log("className:", className, ", shouldInject:", String.valueOf(result) + "\n");
		}
		return result;
	}

	private boolean isInPackages(String className, Set<String> packages) {
		return packages.stream().filter(p -> className.startsWith(p)).findAny().isPresent();
	}

	@Override
	public String toString() {
		return "*InjectRangeConfig [exclude=" + exclude + ", include=" + include + "]";
	}
}
