package org.sfm.jdbc;

import org.sfm.jdbc.impl.getter.*;
import org.sfm.map.getter.EnumUnspecifiedTypeGetter;
import org.sfm.map.getter.OrdinalEnumGetter;
import org.sfm.map.getter.StringEnumGetter;
import org.sfm.map.getter.joda.JodaTimeGetterFactory;
import org.sfm.map.MapperBuildingException;
import org.sfm.map.GetterFactory;
import org.sfm.map.mapper.ColumnDefinition;
import org.sfm.reflect.Getter;
import org.sfm.reflect.TypeHelper;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


//IFJAVA8_START
import java.time.*;
import org.sfm.map.getter.time.JavaTimeGetterFactory;
//IFJAVA8_END
public final class ResultSetGetterFactory implements GetterFactory<ResultSet, JdbcColumnKey>{

	public static final GetterFactory<ResultSet, JdbcColumnKey> DATE_GETTER_FACTORY = new GetterFactory<ResultSet, JdbcColumnKey>() {
		@SuppressWarnings("unchecked")
		@Override
		public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
			switch (key.getSqlType()) {
				case JdbcColumnKey.UNDEFINED_TYPE:
					return (Getter<ResultSet, P>) new UndefinedDateResultSetGetter(key.getIndex());
				case Types.TIMESTAMP:
					return (Getter<ResultSet, P>) new TimestampResultSetGetter(key.getIndex());
				case Types.DATE:
					return (Getter<ResultSet, P>) new DateResultSetGetter(key.getIndex());
				case Types.TIME:
					return (Getter<ResultSet, P>) new TimeResultSetGetter(key.getIndex());
				default:
					return null;
			}
		}
	};
	public static final class StringResultSetGetterFactory implements
			GetterFactory<ResultSet, JdbcColumnKey> {
		@SuppressWarnings("unchecked")
		@Override
		public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
			switch(key.getSqlType() ) {
			case Types.NCHAR:
			case Types.NVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.NCLOB:
				return (Getter<ResultSet, P>) new NStringResultSetGetter(key.getIndex());
			default:
				return (Getter<ResultSet, P>) new StringResultSetGetter(key.getIndex());
			}
		}
	}

	public static final GetterFactory<ResultSet, JdbcColumnKey> ENUM_GETTER_FACTORY = new GetterFactory<ResultSet, JdbcColumnKey>() {
		@SuppressWarnings("unchecked")
		@Override
		public <P> Getter<ResultSet, P> newGetter(Type type, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
			@SuppressWarnings("rawtypes")
			Class<? extends Enum> enumClass = TypeHelper.toClass(type);
			return (Getter<ResultSet, P>) newEnumGetter(enumClass, key);
		}

		private <P extends Enum<P>> Getter<ResultSet, P> newEnumGetter(Class<P> type, JdbcColumnKey key) {
			int column = key.getIndex();
			switch (key.getSqlType()) {
				case JdbcColumnKey.UNDEFINED_TYPE:
					return new EnumUnspecifiedTypeGetter<ResultSet, P>(new ObjectResultSetGetter(column), type);
				case Types.BIGINT:
				case Types.INTEGER:
				case Types.NUMERIC:
				case Types.SMALLINT:
				case Types.TINYINT:
					return new OrdinalEnumGetter<ResultSet, P>(new IntResultSetGetter(column), type);
				case Types.CHAR:
				case Types.LONGVARCHAR:
				case Types.VARCHAR:
				case Types.CLOB:
					return new StringEnumGetter<ResultSet, P>(new StringResultSetGetter(column), type);
				case Types.LONGNVARCHAR:
				case Types.NCHAR:
				case Types.NVARCHAR:
				case Types.NCLOB:
					return new StringEnumGetter<ResultSet, P>(new NStringResultSetGetter(column), type);
				default:
					throw new MapperBuildingException("Incompatible type " + key.getSqlType() + " with enum");
			}
		}
	};

	public static final int UNDEFINED = -99999;

	private static final Map<Class<?>, GetterFactory<ResultSet, JdbcColumnKey>> factoryPerType =
		new HashMap<Class<?>, GetterFactory<ResultSet, JdbcColumnKey>>();
	private static final JodaTimeGetterFactory<ResultSet, JdbcColumnKey> jodaTimeGetterFactory;

	static {
		factoryPerType.put(String.class, new StringResultSetGetterFactory());
		factoryPerType.put(Date.class, DATE_GETTER_FACTORY);


		factoryPerType.put(java.util.Calendar.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				Getter<ResultSet, Date> dateGetter = DATE_GETTER_FACTORY.newGetter(Date.class, key, columnDefinition);
				if (dateGetter != null) {
					return (Getter<ResultSet, P>) new CalendarResultSetGetter(dateGetter);
				}
				return null;
			}
		});

		factoryPerType.put(java.sql.Date.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new DateResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(java.sql.Timestamp.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new TimestampResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(java.sql.Time.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new TimeResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(Boolean.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new BooleanResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Byte.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new ByteResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Character.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new CharacterResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Short.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new ShortResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Integer.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new IntResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Long.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new LongResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Float.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new FloatResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Double.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new DoubleResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(BigInteger.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new BigIntegerResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(BigDecimal.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new BigDecimalResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(URL.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				if (key.getSqlType() == Types.DATALINK) {
					return (Getter<ResultSet, P>) new UrlResultSetGetter(key.getIndex());
				} else {
					return (Getter<ResultSet, P>) new UrlFromStringResultSetGetter(key.getIndex());
				}
			}
		});

		factoryPerType.put(byte[].class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new ByteArrayResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(InputStream.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new InputStreamResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(Blob.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new BlobResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(Reader.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				switch (key.getSqlType()) {
					case Types.LONGNVARCHAR:
					case Types.NCHAR:
					case Types.NVARCHAR:
					case Types.NCLOB:
						return (Getter<ResultSet, P>) new NReaderResultSetGetter(key.getIndex());
					default:
						return (Getter<ResultSet, P>) new ReaderResultSetGetter(key.getIndex());
				}
			}
		});

		factoryPerType.put(Clob.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new ClobResultSetGetter(key.getIndex());
			}
		});

		factoryPerType.put(NClob.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new NClobResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Ref.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new RefResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(RowId.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new RowIdResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(SQLXML.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new SQLXMLResultSetGetter(key.getIndex());
			}
		});
		factoryPerType.put(Array.class, new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type genericType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new SqlArrayResultSetGetter(key.getIndex());
			}
		});

		//IFJAVA8_START
		GetterFactory<ResultSet, JdbcColumnKey> objectGetterFactory = new GetterFactory<ResultSet, JdbcColumnKey>() {
			@SuppressWarnings("unchecked")
			@Override
			public <P> Getter<ResultSet, P> newGetter(Type target, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
				return (Getter<ResultSet, P>) new ObjectResultSetGetter(key.getIndex());
			}
		};
		JavaTimeGetterFactory<ResultSet, JdbcColumnKey> javaTimeGetterFactory =
				new JavaTimeGetterFactory<ResultSet, JdbcColumnKey>(objectGetterFactory);
		factoryPerType.put(LocalDate.class, javaTimeGetterFactory);
		factoryPerType.put(LocalDateTime.class, javaTimeGetterFactory);
		factoryPerType.put(LocalTime.class, javaTimeGetterFactory);
		factoryPerType.put(OffsetDateTime.class, javaTimeGetterFactory);
		factoryPerType.put(OffsetTime.class, javaTimeGetterFactory);
		factoryPerType.put(ZonedDateTime.class, javaTimeGetterFactory);
		factoryPerType.put(Instant.class, javaTimeGetterFactory);
		factoryPerType.put(Year.class, javaTimeGetterFactory);
		factoryPerType.put(YearMonth.class, javaTimeGetterFactory);
		//IFJAVA8_END

		jodaTimeGetterFactory = new JodaTimeGetterFactory<ResultSet, JdbcColumnKey>(factoryPerType.get(Date.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P> Getter<ResultSet, P> newGetter(Type genericType,
											  JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
		
		Class<?> clazz = TypeHelper.wrap(TypeHelper.toClass(genericType));

		if (Object.class.equals(clazz)) {
			return (Getter<ResultSet, P>) new ObjectResultSetGetter(key.getIndex());
		}

		if (key.getSqlType() == Types.ARRAY) {
			if (clazz.isArray()) {
				Class<?> elementType = clazz.getComponentType();
				return (Getter<ResultSet, P>) newArrayGetter(elementType, key, columnDefinition);
			} else if (TypeHelper.isAssignable(List.class, genericType)) {
				Type elementType = TypeHelper.getComponentTypeOfListOrArray(genericType);
				return (Getter<ResultSet, P>) newArrayListGetter(elementType, key, columnDefinition);
			}
		}
		
		GetterFactory<ResultSet, JdbcColumnKey> getterFactory;

		if (clazz.isEnum()) {
			getterFactory = ENUM_GETTER_FACTORY;
		} else {
			getterFactory = factoryPerType.get(clazz);
		}


		if (getterFactory == null) {
			for(Entry<Class<?>, GetterFactory<ResultSet, JdbcColumnKey>> e : factoryPerType.entrySet()) {
				if (clazz.isAssignableFrom(e.getKey())) {
					getterFactory = e.getValue();
					break;
				}
			}
		}
		
		Getter<ResultSet, P> getter;
		if (getterFactory != null) {
			getter = (Getter<ResultSet, P>) getterFactory.newGetter(genericType, key, columnDefinition);
		} else {
			getter = (Getter<ResultSet, P>) jodaTimeGetterFactory.newGetter(genericType, key, columnDefinition);
		}

		if (getter == null) {
			if (SQLData.class.isAssignableFrom(clazz) || key.getSqlType() == Types.JAVA_OBJECT) {
				return (Getter<ResultSet, P>) new ObjectResultSetGetter(key.getIndex());
			}
		}

		return getter;
	}

	private <E> Getter<ResultSet, E[]> newArrayGetter(Class<E> elementType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
		Getter<ResultSet, E> elementGetter = newGetter(elementType, new JdbcColumnKey("elt", 2), columnDefinition);
		
		if (elementGetter != null) {
			return new ArrayResultSetGetter<E>(key.getIndex(), elementType, elementGetter);
		}
		
		return null;
	}
	
	private <E> Getter<ResultSet, List<E>> newArrayListGetter(Type elementType, JdbcColumnKey key, ColumnDefinition<?, ?> columnDefinition) {
		Getter<ResultSet, E> elementGetter = newGetter(elementType, new JdbcColumnKey("elt", 2), columnDefinition);
		
		if (elementGetter != null) {
			return new ArrayToListResultSetGetter<E>(key.getIndex(), elementGetter);
		}
		
		return null;
	}
}
