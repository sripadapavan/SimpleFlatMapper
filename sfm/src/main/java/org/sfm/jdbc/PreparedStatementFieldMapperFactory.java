package org.sfm.jdbc;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.sfm.jdbc.impl.convert.CalendarToTimestampConverter;
import org.sfm.jdbc.impl.convert.UtilDateToTimestampConverter;
import org.sfm.jdbc.impl.convert.joda.JodaDateTimeToTimestampConverter;
import org.sfm.jdbc.impl.convert.joda.JodaLocalDateTimeToTimestampConverter;
import org.sfm.jdbc.impl.convert.joda.JodaLocalDateToDateConverter;
import org.sfm.jdbc.impl.convert.joda.JodaLocalTimeToTimeConverter;
import org.sfm.jdbc.impl.setter.*;
import org.sfm.map.FieldMapper;
import org.sfm.map.FieldMapperToSourceFactory;
import org.sfm.map.column.joda.JodaHelper;
import org.sfm.map.context.MappingContextFactoryBuilder;
import org.sfm.map.impl.fieldmapper.*;
import org.sfm.map.mapper.ColumnDefinition;
import org.sfm.map.mapper.PropertyMapping;
import org.sfm.reflect.Getter;
import org.sfm.reflect.GetterWithConverter;
import org.sfm.reflect.TypeHelper;
import org.sfm.reflect.primitive.*;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PreparedStatementFieldMapperFactory implements FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey> {

    private static final PreparedStatementFieldMapperFactory INSTANCE = new PreparedStatementFieldMapperFactory();

    private final Map<Class<?>, FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>> factoryPerClass =
            new HashMap<Class<?>, FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>>();

    {
        factoryPerClass.put(boolean.class,
                new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                        Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                        BooleanPreparedStatementSetter preparedStatementSetter = new BooleanPreparedStatementSetter(pm.getColumnKey().getIndex());

                        if ((getter instanceof BooleanGetter)) {
                            return new BooleanFieldMapper<T, PreparedStatement>((BooleanGetter<T>) getter, preparedStatementSetter);
                        } else {
                            return new FieldMapperImpl<T, PreparedStatement, Boolean>((Getter<? super T, ? extends Boolean>) getter, preparedStatementSetter);
                        }
                    }
                });
        factoryPerClass.put(Boolean.class, factoryPerClass.get(boolean.class));

        factoryPerClass.put(byte.class,
                new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                        Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                        BytePreparedStatementSetter preparedStatementSetter = new BytePreparedStatementSetter(pm.getColumnKey().getIndex());

                        if ((getter instanceof ByteGetter)) {
                            return new ByteFieldMapper<T, PreparedStatement>((ByteGetter<T>) getter, preparedStatementSetter);
                        } else {
                            return new FieldMapperImpl<T, PreparedStatement, Byte>((Getter<? super T, ? extends Byte>) getter, preparedStatementSetter);
                        }
                    }
                });
        factoryPerClass.put(Byte.class, factoryPerClass.get(byte.class));

        factoryPerClass.put(char.class,
                new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                        Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                        CharacterPreparedStatementSetter preparedStatementSetter = new CharacterPreparedStatementSetter(pm.getColumnKey().getIndex());

                        if ((getter instanceof CharacterGetter)) {
                            return new CharacterFieldMapper<T, PreparedStatement>((CharacterGetter<T>) getter, preparedStatementSetter);
                        } else {
                            return new FieldMapperImpl<T, PreparedStatement, Character>((Getter<? super T, ? extends Character>) getter, preparedStatementSetter);
                        }
                    }
                });
        factoryPerClass.put(Character.class, factoryPerClass.get(char.class));

        factoryPerClass.put(short.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                    ShortPreparedStatementSetter preparedStatementSetter = new ShortPreparedStatementSetter(pm.getColumnKey().getIndex());

                    if ((getter instanceof ShortGetter)) {
                        return new ShortFieldMapper<T, PreparedStatement>((ShortGetter<T>) getter, preparedStatementSetter);
                    } else {
                        return new FieldMapperImpl<T, PreparedStatement, Short>((Getter<? super T, ? extends Short>) getter, preparedStatementSetter);
                    }
                }
            });
        factoryPerClass.put(Short.class, factoryPerClass.get(short.class));

        factoryPerClass.put(int.class,
                new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                        Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                        IntegerPreparedStatementSetter preparedStatementSetter = new IntegerPreparedStatementSetter(pm.getColumnKey().getIndex());

                        if ((getter instanceof IntGetter)) {
                            return new IntFieldMapper<T, PreparedStatement>((IntGetter<T>) getter, preparedStatementSetter);
                        } else {
                            return new FieldMapperImpl<T, PreparedStatement, Integer>((Getter<? super T, ? extends Integer>) getter, preparedStatementSetter);
                        }
                    }
                });
        factoryPerClass.put(Integer.class, factoryPerClass.get(int.class));

        factoryPerClass.put(long.class,
                new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                        Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                        LongPreparedStatementSetter preparedStatementSetter = new LongPreparedStatementSetter(pm.getColumnKey().getIndex());

                        if ((getter instanceof LongGetter)) {
                            return new LongFieldMapper<T, PreparedStatement>((LongGetter<T>) getter, preparedStatementSetter);
                        } else {
                            return new FieldMapperImpl<T, PreparedStatement, Long>((Getter<? super T, ? extends Long>) getter, preparedStatementSetter);
                        }
                    }
                });
        factoryPerClass.put(Long.class, factoryPerClass.get(long.class));

        factoryPerClass.put(float.class,
                new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                        Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                        FloatPreparedStatementSetter preparedStatementSetter = new FloatPreparedStatementSetter(pm.getColumnKey().getIndex());

                        if ((getter instanceof FloatGetter)) {
                            return new FloatFieldMapper<T, PreparedStatement>((FloatGetter<T>) getter, preparedStatementSetter);
                        } else {
                            return new FieldMapperImpl<T, PreparedStatement, Float>((Getter<? super T, ? extends Float>) getter, preparedStatementSetter);
                        }
                    }
                });
        factoryPerClass.put(Float.class, factoryPerClass.get(float.class));

        factoryPerClass.put(double.class,
                new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                        Getter<T, P> getter = pm.getPropertyMeta().getGetter();
                        DoublePreparedStatementSetter preparedStatementSetter = new DoublePreparedStatementSetter(pm.getColumnKey().getIndex());

                        if ((getter instanceof DoubleGetter)) {
                            return new DoubleFieldMapper<T, PreparedStatement>((DoubleGetter<T>) getter, preparedStatementSetter);
                        } else {
                            return new FieldMapperImpl<T, PreparedStatement, Double>((Getter<? super T, ? extends Double>) getter, preparedStatementSetter);
                        }
                    }
                });
        factoryPerClass.put(Double.class, factoryPerClass.get(double.class));

        factoryPerClass.put(String.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, String>((Getter<? super T, ? extends String>) pm.getPropertyMeta().getGetter(),
                            new StringPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Date.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Timestamp>(
                            new GetterWithConverter<T, Date, Timestamp>(
                                    new UtilDateToTimestampConverter(),
                                    (Getter<T, Date>) pm.getPropertyMeta().getGetter()),
                            new TimestampPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Timestamp.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Timestamp>(
                            (Getter<T, ? extends Timestamp>) pm.getPropertyMeta().getGetter(),
                            new TimestampPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(java.sql.Date.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, java.sql.Date>(
                            (Getter<T, ? extends java.sql.Date>) pm.getPropertyMeta().getGetter(),
                            new DatePreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Time.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Time>(
                            (Getter<T, ? extends Time>) pm.getPropertyMeta().getGetter(),
                            new TimePreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Calendar.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Timestamp>(
                            new GetterWithConverter<T, Calendar, Timestamp>(
                                    new CalendarToTimestampConverter(),
                                    (Getter<T, Calendar>) pm.getPropertyMeta().getGetter()),
                            new TimestampPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(URL.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, URL>((Getter<? super T, ? extends URL>) pm.getPropertyMeta().getGetter(),
                            new URLPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Ref.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Ref>((Getter<? super T, ? extends Ref>) pm.getPropertyMeta().getGetter(),
                            new RefPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(BigDecimal.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, BigDecimal>((Getter<? super T, ? extends BigDecimal>) pm.getPropertyMeta().getGetter(),
                            new BigDecimalPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Array.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Array>((Getter<? super T, ? extends Array>) pm.getPropertyMeta().getGetter(),
                            new ArrayPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(byte[].class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, byte[]>((Getter<? super T, byte[]>) pm.getPropertyMeta().getGetter(),
                            new BytesPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(NClob.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, NClob>((Getter<? super T, ? extends NClob>) pm.getPropertyMeta().getGetter(),
                            new NClobPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(RowId.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, RowId>((Getter<? super T, ? extends RowId>) pm.getPropertyMeta().getGetter(),
                            new RowIdPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Blob.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Blob>((Getter<? super T, ? extends Blob>) pm.getPropertyMeta().getGetter(),
                            new BlobPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Clob.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Clob>((Getter<? super T, ? extends Clob>) pm.getPropertyMeta().getGetter(),
                            new ClobPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(InputStream.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, InputStream>((Getter<? super T, ? extends InputStream>) pm.getPropertyMeta().getGetter(),
                            new InputStreamPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(Reader.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, Reader>((Getter<? super T, ? extends Reader>) pm.getPropertyMeta().getGetter(),
                            new ReaderPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
        factoryPerClass.put(SQLXML.class,
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    return new FieldMapperImpl<T, PreparedStatement, SQLXML>((Getter<? super T, ? extends SQLXML>) pm.getPropertyMeta().getGetter(),
                            new SQLXMLPreparedStatementSetter(pm.getColumnKey().getIndex()));
                }
            });
    }

    private static final FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey> jodaTimeFieldMapperToSourceFactory =
            new FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey>() {
                @SuppressWarnings("unchecked")
                @Override
                public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm, MappingContextFactoryBuilder builder) {
                    if (TypeHelper.isClass(pm.getPropertyMeta().getPropertyType(), org.joda.time.DateTime.class)) {
                        TimestampPreparedStatementSetter setter = new TimestampPreparedStatementSetter(pm.getColumnKey().getIndex());
                        Getter<T, Timestamp> getter =
                                new GetterWithConverter<T, DateTime, Timestamp>(
                                        new JodaDateTimeToTimestampConverter(),
                                        (Getter<? super T, ? extends DateTime>) pm.getPropertyMeta().getGetter()
                                );
                        return new FieldMapperImpl<T, PreparedStatement, Timestamp>(getter, setter);
                    } else if (TypeHelper.isClass(pm.getPropertyMeta().getPropertyType(), org.joda.time.LocalDateTime.class)) {
                        TimestampPreparedStatementSetter setter = new TimestampPreparedStatementSetter(pm.getColumnKey().getIndex());
                        Getter<T, Timestamp> getter =
                                new GetterWithConverter<T, LocalDateTime, Timestamp>(
                                        new JodaLocalDateTimeToTimestampConverter(JodaHelper.getDateTimeZone(pm.getColumnDefinition())),
                                        (Getter<? super T, ? extends LocalDateTime>) pm.getPropertyMeta().getGetter()
                                );
                        return new FieldMapperImpl<T, PreparedStatement, Timestamp>(getter, setter);
                    } else if (TypeHelper.isClass(pm.getPropertyMeta().getPropertyType(), org.joda.time.LocalDate.class)) {
                        DatePreparedStatementSetter setter = new DatePreparedStatementSetter(pm.getColumnKey().getIndex());
                        Getter<T, java.sql.Date> getter =
                                new GetterWithConverter<T, LocalDate, java.sql.Date>(
                                        new JodaLocalDateToDateConverter(),
                                        (Getter<? super T, ? extends LocalDate>) pm.getPropertyMeta().getGetter()
                                );
                        return new FieldMapperImpl<T, PreparedStatement, java.sql.Date>(getter, setter);
                    } else if (TypeHelper.isClass(pm.getPropertyMeta().getPropertyType(), org.joda.time.LocalTime.class)) {
                        TimePreparedStatementSetter setter = new TimePreparedStatementSetter(pm.getColumnKey().getIndex());
                        Getter<T, Time> getter =
                                new GetterWithConverter<T, LocalTime, Time>(
                                        new JodaLocalTimeToTimeConverter(JodaHelper.getDateTimeZone(pm.getColumnDefinition())),
                                        (Getter<? super T, ? extends LocalTime>) pm.getPropertyMeta().getGetter()
                                );
                        return new FieldMapperImpl<T, PreparedStatement, Time>(getter, setter);
                    }

                    return null;
                }
            };

    @SuppressWarnings("unchecked")
    public <T, P> FieldMapper<T, PreparedStatement> newFieldMapperToSource(
            PropertyMapping<T, P, JdbcColumnKey, ? extends ColumnDefinition<JdbcColumnKey, ?>> pm,
            MappingContextFactoryBuilder builder) {
        FieldMapper<T, PreparedStatement> fieldMapper = null;

        Type propertyType = pm.getPropertyMeta().getPropertyType();

        FieldMapperToSourceFactory<PreparedStatement, JdbcColumnKey> fieldMapperToSourceFactory = factoryPerClass.get(TypeHelper.toClass(propertyType));

        if (fieldMapperToSourceFactory != null) {
            fieldMapper = fieldMapperToSourceFactory.newFieldMapperToSource(pm, builder);
        }

        if (fieldMapper == null) {
            fieldMapper = jodaTimeFieldMapperToSourceFactory.newFieldMapperToSource(pm, builder);
        }

        return fieldMapper;
    }

    public static PreparedStatementFieldMapperFactory instance() {
        return INSTANCE;
    }
}
