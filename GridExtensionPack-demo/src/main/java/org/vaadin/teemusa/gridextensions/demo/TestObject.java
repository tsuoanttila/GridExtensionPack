package org.vaadin.teemusa.gridextensions.demo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestObject {

	private String foo = "foo";
	private Integer bar = 0;
	private Double km = 0.0d;

	public TestObject() {
	}

	// Constructor used with the data generator
	protected TestObject(int i) {
		setFoo("foo");
		setBar(i);
		setKm(i / 5.0d);
	}

	public String getFoo() {
		return foo;
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

	public Integer getBar() {
		return bar;
	}

	public void setBar(Integer bar) {
		this.bar = bar;
	}

	public Double getKm() {
		return km;
	}

	public void setKm(Double km) {
		this.km = km;
	}

	public static List<TestObject> generateTestData(int max) {
		return IntStream.range(0, max).boxed().map(TestObject::new).collect(Collectors.toList());
	}
}
