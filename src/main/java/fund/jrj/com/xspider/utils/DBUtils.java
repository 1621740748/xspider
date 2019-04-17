package fund.jrj.com.xspider.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jfaster.mango.datasource.DriverManagerDataSource;
import org.jfaster.mango.operator.Mango;

import fund.jrj.com.xspider.constants.DBConfig;

public class DBUtils {
	private static Mango mango = null;

	public static Mango getInstance() {
		if (mango != null) {
			return mango;
		}
		DataSource ds = new DriverManagerDataSource(DBConfig.driverClassName, DBConfig.url, DBConfig.username,
				DBConfig.password);
		mango = Mango.newInstance(ds); // 使用数据源初始化m
		return mango;
	}

	public static void createTable(String host) {
		String sql = "create table if not exists page_link1_" + host + " like page_link1";
		DataSource ds = new DriverManagerDataSource(DBConfig.driverClassName, DBConfig.url, DBConfig.username,
				DBConfig.password);
		Connection conn=null;
		try {
			conn = ds.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			st.close();
			conn.close();
		} catch (SQLException e) {
			try {
				if(conn!=null&&!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}
}
