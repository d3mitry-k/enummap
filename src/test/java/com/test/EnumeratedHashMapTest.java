package com.test;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EnumeratedHashMapTest {

    @Test
    public void shouldEvaluateEnumPublicJsonValueMethod() {
        String result = EnumeratedHashMap.getEnumValue(PrivateJsonValueMethod.class, PrivateJsonValueMethod.VAL);
        assertEquals("Public", result);
    }

    @Test
    public void shouldEvaluateEnumNoArgumentsJsonValueMethod() {
        String result = EnumeratedHashMap.getEnumValue(MultipleArgsJsonValueMethod.class, MultipleArgsJsonValueMethod.VAL);
        assertEquals("NoArgsVal", result);
    }

    @Test
    public void shouldEvaluateEnumMethodWithJsonValueAnnotation() {
        String result = EnumeratedHashMap.getEnumValue(NoJsonValueMethod.class, NoJsonValueMethod.VAL);
        assertEquals("JsonValue", result);
    }

    @Test
    public void shouldSuccessfullyPutValue() {
        Map<Values, String> map = new EnumeratedHashMap<>(Values.class);
        map.put(Values.VAL1, null);
        map.put(Values.VAL2, null);

        String val1 = map.get(Values.VAL1);
        String val2 = map.get(Values.VAL2);
        assertEquals("VAL1", val1);
        assertEquals("VAL2", val2);
    }

    public enum PrivateJsonValueMethod {
        VAL;

        @EnumeratedHashMap.JsonValue
        private String getPrivateMethod() {
            return "Private";
        }
        @EnumeratedHashMap.JsonValue
        protected String getProtectedMethod() {
            return "Protected";
        }

        @EnumeratedHashMap.JsonValue
        String getPackagePrivateMethod() {
            return "PackagePrivate";
        }
        @EnumeratedHashMap.JsonValue
        public String getPublicMethod() {
            return "Public";
        }
    }

    public enum MultipleArgsJsonValueMethod {
        VAL;
        @EnumeratedHashMap.JsonValue
        public String getMultiArgsMethod(Object val) {
            return "MultiArgs";
        }
        @EnumeratedHashMap.JsonValue
        public String getNoArgsMethod() {
            return "NoArgsVal";
        }
    }

    public enum NoJsonValueMethod {
        VAL;

        public String getNoJsonValueMethod() {
            return "NoJsonValue";
        }

        @EnumeratedHashMap.JsonValue
        public String getJsonValueMethod() {
            return "JsonValue";
        }
    }

    public enum Values {
        VAL1, VAL2;

        @EnumeratedHashMap.JsonValue
        public String getStringValue() {
            return name().toString();
        }
    }
}