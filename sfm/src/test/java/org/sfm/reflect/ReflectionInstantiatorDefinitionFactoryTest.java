package org.sfm.reflect;

import org.junit.Test;
import org.sfm.beans.DbFinalObject;
import org.sfm.beans.DbObject;
import org.sfm.beans.DbObject.Type;
import org.sfm.reflect.asm.AsmInstantiatorDefinitionFactory;
import org.sfm.tuples.Tuple2;
import org.sfm.tuples.Tuples;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectionInstantiatorDefinitionFactoryTest {


	public static class ObjectWithFactoryMethod {
		public static ObjectWithFactoryMethod valueOf(String value) {
			return null;
		}

		public static Object valueOf(int value) {
			return null;
		}

		public static void doNothing(ObjectWithFactoryMethod value) {
		}

		private ObjectWithFactoryMethod(){
		}
	}

	@Test
	public void testExtractStaticFactoryMethod() throws NoSuchMethodException {
		List<InstantiatorDefinition> factoryMethod = ReflectionInstantiatorDefinitionFactory.extractDefinitions(ObjectWithFactoryMethod.class);
		assertEquals(1, factoryMethod.size());

		InstantiatorDefinition id = factoryMethod.get(0);

		assertEquals(ObjectWithFactoryMethod.class.getMethod("valueOf", String.class), id.getExecutable());
		assertEquals(1, id.getParameters().length);
		assertEquals(new Parameter(0, null, String.class), id.getParameters()[0]);
	}

	@Test
	public void testExtractStaticFactoryMethodAsm() throws NoSuchMethodException, IOException {
		List<InstantiatorDefinition> factoryMethod = AsmInstantiatorDefinitionFactory.extractDefinitions(ObjectWithFactoryMethod.class);
		assertEquals(1, factoryMethod.size());

		InstantiatorDefinition id = factoryMethod.get(0);

		assertEquals(ObjectWithFactoryMethod.class.getMethod("valueOf", String.class), id.getExecutable());
		assertEquals(1, id.getParameters().length);
		assertEquals(new Parameter(0, "value", String.class), id.getParameters()[0]);
	}

	@Test
	public void testExtractConstructorsDbObject() throws IOException, NoSuchMethodException, SecurityException {
		List<InstantiatorDefinition> dbObjectConstructors = ReflectionInstantiatorDefinitionFactory.extractDefinitions(DbObject.class);
		assertEquals(3, dbObjectConstructors.size());
		assertEquals(0, dbObjectConstructors.get(0).getParameters().length);
		assertEquals(DbObject.class.getConstructor(), dbObjectConstructors.get(0).getExecutable());
		
	}
	
	@Test
	public void testExtractConstructorsFinalDbObject() throws IOException, NoSuchMethodException, SecurityException {

		List<InstantiatorDefinition> finalDbObjectConstructors = ReflectionInstantiatorDefinitionFactory.extractDefinitions(DbFinalObject.class);
		assertEquals(1, finalDbObjectConstructors.size());
		assertEquals(6, finalDbObjectConstructors.get(0).getParameters().length);
		
		assertEquals(long.class, finalDbObjectConstructors.get(0).getParameters()[0].getType());
		assertEquals(String.class, finalDbObjectConstructors.get(0).getParameters()[1].getType());
		assertEquals(String.class, finalDbObjectConstructors.get(0).getParameters()[2].getType());
		assertEquals(Date.class, finalDbObjectConstructors.get(0).getParameters()[3].getType());
		assertEquals(Type.class, finalDbObjectConstructors.get(0).getParameters()[4].getType());
		assertEquals(Type.class, finalDbObjectConstructors.get(0).getParameters()[5].getType());

		assertNull(finalDbObjectConstructors.get(0).getParameters()[0].getName());
		assertNull(finalDbObjectConstructors.get(0).getParameters()[1].getName());
		assertNull(finalDbObjectConstructors.get(0).getParameters()[2].getName());
		assertNull(finalDbObjectConstructors.get(0).getParameters()[3].getName());
		assertNull(finalDbObjectConstructors.get(0).getParameters()[4].getName());
		assertNull(finalDbObjectConstructors.get(0).getParameters()[5].getName());

		
		assertEquals(DbFinalObject.class.getConstructor(long.class, String.class, String.class, Date.class, Type.class, Type.class), finalDbObjectConstructors.get(0).getExecutable());

	}


	@Test
	public void testExtractConstructorsTuple2() throws IOException, NoSuchMethodException, SecurityException {

		List<InstantiatorDefinition> finalDbObjectConstructors = ReflectionInstantiatorDefinitionFactory.extractDefinitions(Tuples.typeDef(String.class, DbObject.class));
		assertEquals(1, finalDbObjectConstructors.size());
		assertEquals(2, finalDbObjectConstructors.get(0).getParameters().length);

		assertEquals(Object.class, finalDbObjectConstructors.get(0).getParameters()[0].getType());
		assertEquals(Object.class, finalDbObjectConstructors.get(0).getParameters()[1].getType());

		assertEquals(String.class, finalDbObjectConstructors.get(0).getParameters()[0].getGenericType());
		assertEquals(DbObject.class, finalDbObjectConstructors.get(0).getParameters()[1].getGenericType());

		assertNull(finalDbObjectConstructors.get(0).getParameters()[0].getName());
		assertNull(finalDbObjectConstructors.get(0).getParameters()[1].getName());

		assertEquals(Tuple2.class.getConstructor(Object.class, Object.class), finalDbObjectConstructors.get(0).getExecutable());

	}


	//IFJAVA8_START
	@Test
	public void testClassWithParamName() throws ClassNotFoundException, IOException {
		final ClassLoader original = getClass().getClassLoader();
		ClassLoader cl = new ClassLoader(ClassLoader.getSystemClassLoader().getParent()) {
			@Override
			protected Class<?> findClass(String name) throws ClassNotFoundException {
				InputStream resourceAsStream = original.getResourceAsStream(name.replace(".", "/") + ".class");
				if (resourceAsStream == null) {
					throw new ClassNotFoundException(name);
				}
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					int i;
					while((i = resourceAsStream.read()) != -1) {
						bos.write(i);
					}
					byte[] bytes = bos.toByteArray();
					return defineClass(name, bytes, 0, bytes.length);
				} catch (IOException e) {
					throw new ClassNotFoundException(e.getMessage(), e);
				} finally {
					try {
						resourceAsStream.close();
					} catch (IOException e) {
					}
				}

			}
		};

		final Class<?> classWithParameter = cl.loadClass("p.ClassParameter");
		final Class<?> classWithoutParameter = cl.loadClass("p.ClassNoNameParameter");

		List<InstantiatorDefinition> instantiatorDefinitions = ReflectionService.newInstance().extractConstructors(classWithParameter);

		assertEquals(1, instantiatorDefinitions.size());
		assertEquals(2, instantiatorDefinitions.get(0).getParameters().length);
		assertEquals("name", instantiatorDefinitions.get(0).getParameters()[0].getName());
		assertEquals("value", instantiatorDefinitions.get(0).getParameters()[1].getName());


		instantiatorDefinitions = ReflectionService.newInstance().extractConstructors(classWithoutParameter);

		assertEquals(1, instantiatorDefinitions.size());
		assertEquals(2, instantiatorDefinitions.get(0).getParameters().length);
		assertNull(instantiatorDefinitions.get(0).getParameters()[0].getName());
		assertNull(instantiatorDefinitions.get(0).getParameters()[1].getName());
	}
	//IFJAVA8_END

}
