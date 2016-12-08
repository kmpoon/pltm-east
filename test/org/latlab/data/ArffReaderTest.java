package org.latlab.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.latlab.data.io.arff.ArffParser;
import org.latlab.data.io.arff.ParseException;
import org.latlab.util.DiscreteVariable;

public class ArffReaderTest {

	@Test
	public void testReadIris() throws IOException, ParseException {
		MixedDataSet data =
				ArffParser.parse(getClass().getResourceAsStream("iris.arff"));
		assertEquals("iris", data.name());

		assertEquals(5, data.variables().size());

		assertEquals("sepallength", data.variables().get(0).getName());
		assertEquals("sepalwidth", data.variables().get(1).getName());
		assertEquals("petallength", data.variables().get(2).getName());
		assertEquals("petalwidth", data.variables().get(3).getName());
		assertEquals("class", data.variables().get(4).getName());

		assertArrayEquals(
				new String[] { "Iris-setosa", "Iris-versicolor",
						"Iris-virginica" },
				((DiscreteVariable) data.variables().get(4)).getStates().toArray());

		Instance instance;

		instance = data.get(0);
		assertEquals(5.1, instance.value(0), 0);
		assertEquals(3.5, instance.value(1), 0);
		assertEquals(1.4, instance.value(2), 0);
		assertEquals(0.2, instance.value(3), 0);
		assertEquals(0, instance.value(4), 0);
		assertEquals(1, instance.weight(), 0);

		instance = data.get(50);
		assertEquals(7.0, instance.value(0), 0);
		assertEquals(3.2, instance.value(1), 0);
		assertEquals(4.7, instance.value(2), 0);
		assertEquals(1.4, instance.value(3), 0);
		assertEquals(1, instance.value(4), 0);
		assertEquals(1, instance.weight(), 0);

		instance = data.get(100);
		assertEquals(6.3, instance.value(0), 0);
		assertEquals(3.3, instance.value(1), 0);
		assertEquals(6.0, instance.value(2), 0);
		assertEquals(2.5, instance.value(3), 0);
		assertEquals(2, instance.value(4), 0);
		assertEquals(1, instance.weight(), 0);

		assertEquals(150, data.totalWeight(), 0);
	}

	@Test
	public void testLoadOthers() throws IOException, ParseException {
		testLoadArff("breast-w.arff", "wisconsin-breast-cancer", 10, 699);
		testLoadArff("communities.arff", "crimepredict", 124, 1994);
		testLoadArff("credit-g.arff", "german_credit", 21, 1000);
		testLoadArff("glass.arff", "Glass", 10, 214);
	}

	private void testLoadArff(String file, String name, int variables,
			double size) throws ParseException {
		MixedDataSet data =
				ArffParser.parse(getClass().getResourceAsStream(file));
		assertEquals(name, data.name());

		assertEquals(variables, data.variables().size());
		assertEquals(size, data.totalWeight(), 0);
	}
}
