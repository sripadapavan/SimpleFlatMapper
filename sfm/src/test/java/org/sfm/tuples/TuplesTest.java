package org.sfm.tuples;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class TuplesTest {
    @Test
    public void testIsTuple() {
        assertTrue(Tuples.isTuple(Tuple2.class));
        assertTrue(Tuples.isTuple(Tuple3.class));
        assertTrue(Tuples.isTuple(Tuple4.class));
        assertTrue(Tuples.isTuple(Tuple5.class));
        assertFalse(Tuples.isTuple(Tuples.class));
    }

    @Test
    public void typeDefRejectInvalidTuples() {
        try {
            Tuples.tupleImplementationTypeDef(Tuple2.class, String.class);
            fail("Expected Exception");
        } catch(Exception e) {
            // expected
        }
    }

    Tuple2<String, String> tuple2 = new Tuple2<String, String>("aa", "bb");
    Tuple3<String, String, String> tuple3 = tuple2.tuple3("cc");
    Tuple4<String, String, String, String> tuple4 = tuple3.tuple4("dd");
    Tuple5<String, String, String, String, String> tuple5 = tuple4.tuple5("ee");
    @Test
    public void testTuple2() {
        assertEquals(new Tuple2<String, String>("aa", "bb"), tuple2);
        assertEquals(new Tuple2<String, String>("aa", "bb").hashCode(), tuple2.hashCode());
        assertNotEquals(new Tuple2<String, String>("aa", "bbb"), tuple2);
        assertNotEquals(new Tuple2<String, String>("aa", "bbb").hashCode(), tuple2.hashCode());
        assertNotEquals(new Tuple2<String, String>("aaa", "bb"), tuple2);
        assertEquals("Tuple2{element0=aa, element1=bb}", tuple2.toString());
    }

    @Test
    public void testTuple3() {
        assertEquals(tuple2.tuple3("cc"), tuple3);
        assertNotEquals(tuple2.tuple3("ccc"), tuple3);
        assertEquals(tuple2.tuple3("cc").hashCode(), tuple3.hashCode());
        assertNotEquals(tuple2.tuple3("ccc").hashCode(), tuple3.hashCode());
        assertNotEquals(tuple2, tuple3);
        assertEquals("Tuple3{element0=aa, element1=bb, element2=cc}", tuple3.toString());
    }

    @Test
    public void testTuple4() {
        assertEquals(tuple3.tuple4("dd"), tuple4);
        assertNotEquals(tuple3.tuple4("ddd"), tuple4);
        assertEquals(tuple3.tuple4("dd").hashCode(), tuple4.hashCode());
        assertNotEquals(tuple3.tuple4("ddd").hashCode(), tuple4.hashCode());
        assertNotEquals(tuple3, tuple4);
        assertEquals("Tuple4{element0=aa, element1=bb, element2=cc, element3=dd}", tuple4.toString());
    }

    @Test
    public void testTuple5() {
        assertEquals(tuple4.tuple5("ee"), tuple5);
        assertNotEquals(tuple4.tuple5("eee"), tuple5);
        assertEquals(tuple4.tuple5("ee").hashCode(), tuple5.hashCode());
        assertNotEquals(tuple4.tuple5("eee").hashCode(), tuple5.hashCode());
        assertNotEquals(tuple4, tuple5);
        assertEquals("Tuple5{element0=aa, element1=bb, element2=cc, element3=dd, element4=ee}", tuple5.toString());
    }

    @Test
    public void testTuples() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Tuple2<?, ?> currentTuple = new Tuple2<Object, Object>("v1", "v2");
        for(int i = 3; i <= 32; i++) {
            Method m = currentTuple.getClass().getDeclaredMethod("tuple" + i, Object.class);

            Tuple2<?, ?> nextTuple = (Tuple2<?, ?>) m.invoke(currentTuple, "v" + i);
            Tuple2<?, ?> sameTuple = (Tuple2<?, ?>) m.invoke(currentTuple, "v" + i);
            Tuple2<?, ?> diffTuple = (Tuple2<?, ?>) m.invoke(currentTuple, "d" + i);


            assertEquals(nextTuple, sameTuple);
            assertEquals(nextTuple.hashCode(), sameTuple.hashCode());
            assertEquals(nextTuple.toString(), sameTuple.toString());

            assertNotEquals(nextTuple, diffTuple);
            assertNotEquals(nextTuple.hashCode(), diffTuple.hashCode());
            assertNotEquals(nextTuple.toString(), diffTuple.hashCode());

            assertTrue(Tuples.isTuple(nextTuple.getClass()));

            currentTuple = nextTuple;
        }
    }
}
