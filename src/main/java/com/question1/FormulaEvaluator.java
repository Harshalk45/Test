package com.question1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mvel2.MVEL;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormulaEvaluator {

	public static void main(String[] args) {
		String jsonInput = "[{\"calculateField\":\"customFloat1\",\"formula\":\"10+15\"},"
				+ "{\"calculateField\":\"customFloat2\",\"formula\":\"customFloat1*10\"},"
				+ "{\"calculateField\":\"customFloat3\",\"formula\":\"customFloat2-18\"},"
				+ "{\"calculateField\":\"customFloat4\",\"formula\":\"customFloat3+customFloat2\"}]";

		ObjectMapper objectMapper = new ObjectMapper();
		List<Calculation> calculations;

		try {
			calculations = objectMapper.readValue(jsonInput, new TypeReference<List<Calculation>>() {
			});
		} catch (IOException e) {
			System.err.println("Failed to parse JSON input: " + e.getMessage());
			return;
		}

		Map<String, Object> context = new HashMap<>();
		Map<String, Double> results = new HashMap<>();

		for (Calculation calc : calculations) {
			String formula = calc.getFormula();

			for (Map.Entry<String, Double> entry : results.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}

			try {
				Object evalResult = MVEL.eval(formula, context);
				if (evalResult instanceof Number) {
					results.put(calc.getCalculateField(), ((Number) evalResult).doubleValue());
				} else {
					System.err.println(
							"The result of the formula is not a number for field '" + calc.getCalculateField() + "'");
				}
			} catch (Exception e) {
				System.err.println("Failed to evaluate formula '" + formula + "' for field '" + calc.getCalculateField()
						+ "': " + e.getMessage());
			}
		}

		results.forEach((key, value) -> System.out.println("FieldName: " + key + "\nValue: " + value));
	}
}

class Calculation {
	private String calculateField;
	private String formula;

	public String getCalculateField() {
		return calculateField;
	}

	public void setCalculateField(String calculateField) {
		this.calculateField = calculateField;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
}
