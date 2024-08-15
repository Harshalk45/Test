package com.question1;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormulaEvaluator {

    public static void main(String[] args) throws IOException, ScriptException {
        String jsonInput = "[{\"calculateField\":\"customFloat1\",\"formula\":\"10+15\"}," +
                            "{\"calculateField\":\"customFloat2\",\"formula\":\"customFloat1*10\"}," +
                            "{\"calculateField\":\"customFloat3\",\"formula\":\"customFloat2-18\"}," +
                            "{\"calculateField\":\"customFloat4\",\"formula\":\"customFloat3+customFloat2\"}]";

        ObjectMapper objectMapper = new ObjectMapper();
        List<Calculation> calculations = objectMapper.readValue(jsonInput, new TypeReference<List<Calculation>>(){});

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        Map<String, Double> results = new HashMap<>();

        for (Calculation calc : calculations) {
            String formula = calc.getFormula();
            for (Map.Entry<String, Double> entry : results.entrySet()) {
                formula = formula.replace(entry.getKey(), entry.getValue().toString());
            }

            Double result = (Double) engine.eval(formula);
            results.put(calc.getCalculateField(), result);
        }

        results.forEach((key, value) -> System.out.println("FieldName: " + key + "\nValue: " + value));
    }
}

class Calculation {
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
	private String calculateField;
    private String formula;

    
}
