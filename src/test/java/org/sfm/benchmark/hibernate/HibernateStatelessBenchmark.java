package org.sfm.benchmark.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.sfm.beans.DbObject;
import org.sfm.benchmark.BenchmarkRunner;
import org.sfm.benchmark.ForEachListener;
import org.sfm.benchmark.QueryExecutor;
import org.sfm.benchmark.SysOutBenchmarkListener;
import org.sfm.jdbc.DbHelper;

public class HibernateStatelessBenchmark implements QueryExecutor {

	private SessionFactory sf;
	public HibernateStatelessBenchmark(Connection conn) {
		this.sf = HibernateHelper.getSessionFactory(conn, false);
	}

	@Override
	public void forEach(ForEachListener ql, int limit) throws Exception {
		StatelessSession session = sf.openStatelessSession();
		try {
			Query query = session.createQuery("from DbObject");
			if (limit >= 0) {
				query.setMaxResults(limit);
			}
			ScrollableResults sr = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			try {
				while(sr.next()) {
					DbObject o = (DbObject) sr.get(0);
					ql.object(o);
				}
			} finally {
				sr.close();
			}
		} finally {
			session.close();
		}
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, SQLException, Exception {
		new BenchmarkRunner(-1, new HibernateStatelessBenchmark(DbHelper.benchmarkDb())).run(new SysOutBenchmarkListener(HibernateStatelessBenchmark.class, "BigQuery"));
		new BenchmarkRunner(1, new HibernateStatelessBenchmark(DbHelper.benchmarkDb())).run(new SysOutBenchmarkListener(HibernateStatelessBenchmark.class, "SmallQuery"));
	}
}