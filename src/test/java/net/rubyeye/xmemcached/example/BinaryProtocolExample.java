package net.rubyeye.xmemcached.example;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple example for xmemcached,use binary protocol
 * 
 * @author boyan
 * 
 */
public class BinaryProtocolExample {

	private static final Logger log = LoggerFactory
			.getLogger(BinaryProtocolExample.class);

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Useage:java BinaryProtocolExample [servers]");
			System.exit(1);
		}
		MemcachedClient memcachedClient = getMemcachedClient(args[0]);
		if (memcachedClient == null) {
			throw new NullPointerException(
					"Null MemcachedClient,please check memcached has been started");
		}
		try {
			// add a
			System.out.println("Add a,b,c");
			memcachedClient.set("a", 0, "Hello,xmemcached");
			// get a
			String value = memcachedClient.get("a");
			System.out.println("get a=" + value);
			System.out.println("delete a");
			// delete a
			memcachedClient.delete("a");
			value = memcachedClient.get("a");
			System.out.println("after delete,a=" + value);
		} catch (MemcachedException e) {
			log.error("MemcachedClient operation fail", e);
		} catch (TimeoutException e) {
			log.error("MemcachedClient operation timeout", e);
		} catch (InterruptedException e) {
			// ignore
			log.info(e.getMessage());
		}
		try {
			memcachedClient.shutdown();
		} catch (Exception e) {
			log.error("Shutdown MemcachedClient fail", e);
		}
	}

	public static MemcachedClient getMemcachedClient(String servers) {
		try {
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
					AddrUtil.getAddresses(servers));
			// use binary protocol
			builder.setCommandFactory(new BinaryCommandFactory());
			return builder.build();
		} catch (IOException e) {
			System.err.println("Create MemcachedClient fail");
			e.printStackTrace();
		}
		return null;
	}
}
