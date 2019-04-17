package fund.jrj.com.xspider.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentHashMap;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import cn.edu.hfut.dmic.webcollector.plugin.rocks.RocksDBUtils;

public class RocksUtils {
	private static ConcurrentHashMap<String,RocksUtils> dbs=new ConcurrentHashMap<>();
	private RocksDB pDB=null;
	private RocksUtils(RocksDB db) {
		this.pDB=db;
	}
	static{
		RocksDB.loadLibrary();

	}
	public static RocksUtils  getInstance(String db) {
		RocksUtils rdb=dbs.get(db);
		if(rdb!=null) {
			return  rdb;
		}
		String path=new File("dbs", db).getAbsolutePath();
		try {
			rdb= new RocksUtils(RocksDBUtils.open(path));
		} catch (RocksDBException e) {
			e.printStackTrace();
		}
		dbs.putIfAbsent(db, rdb);
		return rdb;
	}
	public  String get(String key) {
		try {
			String value=RocksDBUtils.get(pDB, key);
			return value;
		} catch (UnsupportedEncodingException | RocksDBException e) {
			e.printStackTrace();
		}
		return null;
	}
	public  void put(String key,String value) {
		try {
			RocksDBUtils.put(pDB, key, value);
		} catch (UnsupportedEncodingException | RocksDBException e) {
			e.printStackTrace();
		}
	}
}
