package ru.creditnet.schemaless;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author antivoland
 */
public class SchemalessTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static class Entry extends Schemaless {
        @Override
        @JsonAnySetter
        protected void set(String key, Object value) {
            super.set(key, value);
        }

        String serialize() throws IOException {
            return MAPPER.writeValueAsString(data);
        }

        static Entry deserialize(String data) throws IOException {
            return MAPPER.readValue(data, Entry.class);
        }
    }

    private static final String UNSORTED = ("" +
            "{" +
            "  'y':{" +
            "    'z':[3,2,1]," +
            "    'xyz':1" +
            "  }," +
            "  'x':['c','b','a']" +
            "}").replace("'", "\"").replace(" ", "");

    private static final String SORTED = ("" +
            "{" +
            "  'x':['a','b','c']," +
            "  'y':{" +
            "    'xyz':1," +
            "    'z':[1,2,3]" +
            "  }" +
            "}").replace("'", "\"").replace(" ", "");

    private static final String NESTED = ("" +
            "{" +
            "  'regular': '-_-'," +
            "  'nested':[" +
            "    {'a':1,'b':true,'c':[1,2,3]}," +
            "    {'x':['q','w','e'],'y':'zero'}" +
            "  ]" +
            "}").replace("'", "\"").replace(" ", "");

    private static final String NESTED_REORDERED = ("" +
            "{" +
            "  'nested':[" +
            "    {'y':'zero','x':['e','w','q']}," +
            "    {'c':[3,2,1],'b':true,'a':1}" +
            "  ]," +
            "  'regular': '-_-'" +
            "}").replace("'", "\"").replace(" ", "");

    @Test
    public void testSortedData() throws IOException {
        Entry entry = Entry.deserialize(UNSORTED);
        Assert.assertEquals(SORTED, entry.serialize());
    }

    @Test
    public void testEquality() throws IOException {
        testEquality(Entry.deserialize(UNSORTED), Entry.deserialize(SORTED));
        testEquality(Entry.deserialize(NESTED), Entry.deserialize(NESTED_REORDERED));
    }

    private void testEquality(Entry entry1, Entry entry2) {
        Assert.assertTrue(entry1.hashCode() == entry2.hashCode());
        Assert.assertTrue(entry1.equals(entry2));
        Assert.assertTrue(entry2.equals(entry1));
    }
}
