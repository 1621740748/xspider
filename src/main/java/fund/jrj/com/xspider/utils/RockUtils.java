package fund.jrj.com.xspider.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import cn.edu.hfut.dmic.webcollector.crawldb.DBManager;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.RocksDBManager;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.RocksDBUtils;

public class RockUtils {
    private static DBManager jscss = new RocksDBManager("jscss");
    private static  RocksDB jscssDB =null;
    static {
    	try {
			jscss.clear();
			String path=new File("jscss", "jscss").getAbsolutePath();
			jscssDB= RocksDBUtils.open(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    public static String get(String key) {
    	try {
			String value=RocksDBUtils.get(jscssDB, key);
			return value;
		} catch (UnsupportedEncodingException | RocksDBException e) {
			e.printStackTrace();
		}
    	return null;
    }
    public static void put(String key,String value) {
    	try {
			RocksDBUtils.put(jscssDB, key, value);
		} catch (UnsupportedEncodingException | RocksDBException e) {
			e.printStackTrace();
		}
    }
}
