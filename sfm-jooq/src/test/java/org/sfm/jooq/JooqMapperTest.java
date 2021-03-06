package org.sfm.jooq;

import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.junit.Test;
import org.sfm.beans.DbExtendedType;
import org.sfm.beans.DbObject;
import org.sfm.test.jdbc.DbHelper;

import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JooqMapperTest {

	@Test
	@SuppressWarnings("unchecked")
	public void testCacheMapper() {
		SfmRecordMapperProvider recordMapperProvider = new SfmRecordMapperProvider();
		RecordType rt = mock(RecordType.class);
		Field field1 = mock(Field.class);
		when(field1.getName()).thenReturn("id");
		when(field1.getType()).thenReturn(long.class);
		when(rt.size()).thenReturn(1);
		when(rt.fields()).thenReturn(new Field[] {field1});

		JooqRecordMapperWrapper provider1 =
				(JooqRecordMapperWrapper) recordMapperProvider.<Record, DbObject>provide(rt, DbObject.class);
		JooqRecordMapperWrapper provider2 =
				(JooqRecordMapperWrapper) recordMapperProvider.<Record, DbObject>provide(rt, DbObject.class);
		assertSame(provider1.getMapper(), provider2.getMapper());
	}

	@Test
	public void testMapperDbObject() throws Exception {
		Connection conn = DbHelper.objectDb();

		DSLContext dsl = DSL
				.using(new DefaultConfiguration().set(conn)
						.set(SQLDialect.HSQLDB)
						.set(new SfmRecordMapperProvider()));
		
		List<DbObject> list = dsl.select()
				.from("TEST_DB_OBJECT").fetchInto(DbObject.class);
		
		assertEquals(1, list.size());
		DbHelper.assertDbObjectMapping(list.get(0));
	}
	
	@Test
	public void testMapperDbExtendedType() throws Exception {
		Connection conn = DbHelper.objectDb();

		DSLContext dsl = DSL
				.using(new DefaultConfiguration().set(conn)
						.set(SQLDialect.HSQLDB)
						.set(new SfmRecordMapperProvider()));
		
		List<DbExtendedType> list = dsl.select()
				.from("db_extended_type").fetchInto(DbExtendedType.class);
		
		
		assertEquals(1, list.size());
		DbExtendedType o = list.get(0);
		
		DbExtendedType.assertDbExtended(o);
		
	}
}
