package com.test.framwork.cucumber.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomReportGenerator {

    private static final String JSON_PATH = "target/cucumber.json";
    private static final String REPORT_DIR = "target/CustomReports";
    private static final String REPORT_FILE = REPORT_DIR + "/AutomationReport.html";

    public static void generateCustomReport() {
        try {
            // 1. Ensure JSON exists before reading
            if (!Files.exists(Paths.get(JSON_PATH))) {
                System.err.println("Error: cucumber.json not found at " + JSON_PATH);
                return;
            }

            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_PATH)));
            JSONArray features = new JSONArray(jsonContent);
            Files.createDirectories(Paths.get(REPORT_DIR));

            int passed = 0, failed = 0, skipped = 0;
            StringBuilder featureHtml = new StringBuilder();

            // 2. Parse Features and Scenarios
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                featureHtml.append("<div class='feature'>");
                featureHtml.append("<div class='feature-header'>📋 Feature: ").append(feature.getString("name")).append("</div>");

                JSONArray scenarios = feature.optJSONArray("elements");
                if (scenarios != null) {
                    for (int j = 0; j < scenarios.length(); j++) {
                        JSONObject scenario = scenarios.getJSONObject(j);
                        String status = getScenarioStatus(scenario);
                        
                        // Count totals
                        if (status.equals("passed")) passed++;
                        else if (status.equals("failed")) failed++;
                        else skipped++;

                        featureHtml.append(buildScenarioHtml(scenario, status));
                    }
                }
                featureHtml.append("</div>");
            }

            // 3. Assemble Final HTML
            String finalHtml = getHeader() + featureHtml.toString() + getFooter(passed, failed, skipped);
            Files.write(Paths.get(REPORT_FILE), finalHtml.getBytes());
            System.out.println("🚀 Custom Report successfully generated at: " + REPORT_FILE);

        } catch (Exception e) {
            System.err.println("Fatal Error during Report Generation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String buildScenarioHtml(JSONObject scenario, String status) {
        StringBuilder sb = new StringBuilder();
        String icon = status.equals("passed") ? "✓" : "✗";
        
        sb.append("<div class='scenario ").append(status).append("'>");
        sb.append("<div class='scenario-title'>");
        sb.append("<span class='status-badge ").append(status).append("'>").append(icon).append(" ").append(status.toUpperCase()).append("</span>");
        sb.append(" ").append(escapeHtml(scenario.getString("name"))).append("</div>");

        JSONArray steps = scenario.optJSONArray("steps");
        if (steps != null) {
            for (int k = 0; k < steps.length(); k++) {
                JSONObject step = steps.getJSONObject(k);
                JSONObject result = step.getJSONObject("result");
                String stepStatus = result.getString("status");
                
                sb.append("<div class='step ").append(stepStatus).append("'>");
                sb.append("<b>").append(step.optString("keyword", "")).append("</b> ").append(escapeHtml(step.getString("name")));
                
                // Attach Embeddings (Screenshots)
                JSONArray embeddings = step.optJSONArray("embeddings");
                if (embeddings != null) {
                    for (int m = 0; m < embeddings.length(); m++) {
                        String base64Data = embeddings.getJSONObject(m).getString("data");
                        sb.append("<details><summary>📷 View Screenshot</summary>");
                        sb.append("<img src='data:image/png;base64,").append(base64Data).append("'/></details>");
                    }
                }

                if (stepStatus.equals("failed")) {
                    sb.append("<pre class='error'>").append(escapeHtml(result.optString("error_message", "No error message provided."))).append("</pre>");
                }
                sb.append("</div>");
            }
        }
        sb.append("</div>");
        return sb.toString();
    }

    private static String getScenarioStatus(JSONObject scenario) {
        JSONArray steps = scenario.optJSONArray("steps");
        if (steps == null) return "skipped";
        for (int i = 0; i < steps.length(); i++) {
            if (steps.getJSONObject(i).getJSONObject("result").getString("status").equals("failed")) {
                return "failed";
            }
        }
        return "passed";
    }

    private static String getHeader() {
        return "<html><head><title>Automation Results</title>" + getStyles() + "</head><body>" +
               "<div class='container'><div class='header'><h1>🧪 Automation Test Report</h1>" +
               "<p>Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")) + "</p></div>";
    }

    private static String getFooter(int p, int f, int s) {
        int total = p + f + s;
        return "<div class='stats-bar'>" +
               "<div class='card pass'><h3>" + p + "</h3><p>Passed</p></div>" +
               "<div class='card fail'><h3>" + f + "</h3><p>Failed</p></div>" +
               "<div class='card total'><h3>" + total + "</h3><p>Total</p></div>" +
               "</div></div></body></html>";
    }

    private static String getStyles() {
        return "<style>" +
               "body { font-family: sans-serif; background: #f4f7f6; padding: 20px; }" +
               ".container { max-width: 1000px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }" +
               ".header { text-align: center; border-bottom: 2px solid #eee; padding-bottom: 20px; margin-bottom: 20px; }" +
               ".stats-bar { display: flex; justify-content: space-around; margin-bottom: 30px; }" +
               ".card { padding: 15px; border-radius: 8px; width: 30%; text-align: center; color: white; }" +
               ".pass { background: #2ecc71; } .fail { background: #e74c3c; } .total { background: #3498db; }" +
               ".feature-header { background: #2c3e50; color: white; padding: 10px; border-radius: 5px; margin-top: 20px; }" +
               ".scenario { border-left: 5px solid #ccc; padding: 15px; margin: 10px 0; border-radius: 4px; }" +
               ".scenario.passed { border-left-color: #2ecc71; background: #f0fff4; }" +
               ".scenario.failed { border-left-color: #e74c3c; background: #fff5f5; }" +
               ".step { margin: 5px 0; padding: 8px; border-radius: 3px; font-size: 14px; }" +
               ".step.failed { color: #e74c3c; font-weight: bold; }" +
               ".error { background: #fee; padding: 10px; border: 1px solid #ecc; color: #a00; font-size: 12px; white-space: pre-wrap; }" +
               "img { width: 100%; border: 1px solid #ddd; border-radius: 5px; margin-top: 10px; }" +
               "summary { font-size: 12px; cursor: pointer; color: #3498db; }" +
               "</style>";
    }

    private static String escapeHtml(String input) {
        return input == null ? "" : input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}