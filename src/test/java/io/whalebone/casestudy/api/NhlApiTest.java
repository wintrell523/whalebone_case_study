package io.whalebone.casestudy.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class NhlApiTest {

    // Base URL for API endpoint
    private static final String BASE_URL = "https://qa-assignment.dev1.whalebone.io/api/teams";

    @Test
    public void testTeamCount() {
        // Make a GET request to the API endpoint
        Response response = RestAssured.get(BASE_URL);
        // Extract the list of teams from the response
        List<Object> teams = response.jsonPath().getList("teams");
        // Assert that the total number of teams is 32
        Assert.assertEquals(teams.size(), 32, "Expected 32 teams in total");
    }

    @Test
    public void testOldestTeam() {
        // Make a GET request to the API endpoint
        Response response = RestAssured.get(BASE_URL);
        // Extract the list of teams as maps
        List<Map<String, Object>> teams = response.jsonPath().getList("teams");
        // Find the oldest team by comparing the 'founded' year
        Map<String, Object> oldestTeam = teams.stream()
            .min((t1, t2) -> Integer.compare((int) t1.get("founded"), (int) t2.get("founded")))
            .orElseThrow();
        // Assert that the oldest team is Montreal Canadiens
        Assert.assertEquals(oldestTeam.get("name"), "Montreal Canadiens", "Oldest team is not Montreal Canadiens");
    }

    @Test
    public void testCityWithMultipleTeams() {
        // Make a GET request to the API endpoint
        Response response = RestAssured.get(BASE_URL);
        // Extract the list of teams as maps
        List<Map<String, Object>> teams = response.jsonPath().getList("teams");
        // Group teams by city
        Map<String, List<String>> cityTeams = new HashMap<>();
        teams.forEach(t -> {
            String city = (String) t.get("location");
            String name = (String) t.get("name");
            cityTeams.computeIfAbsent(city, k -> new ArrayList<>()).add(name);
        });
        // Find cities with more than one team
        List<String> citiesWithMultipleTeams = cityTeams.entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        // Assert that at least one city has more than one team
        Assert.assertTrue(!citiesWithMultipleTeams.isEmpty(), "No city has more than one team");
        // Print the city and its teams
        citiesWithMultipleTeams.forEach(city -> {
            System.out.println("City: " + city + ", teams: " + cityTeams.get(city));
        });
    }

    @Test
    public void testMetropolitanDivisionTeams() {
        // Make a GET request to the API endpoint
        Response response = RestAssured.get(BASE_URL);
        // Extract the list of teams as maps
        List<Map<String, Object>> teams = response.jsonPath().getList("teams");
        // Filter teams in the Metropolitan division
        List<String> metroTeams = teams.stream()
            .filter(t -> ((Map<String, Object>) t.get("division")).get("name").equals("Metropolitan"))
            .map(t -> (String) t.get("name"))
            .collect(Collectors.toList());
        // Assert that there are 8 teams in the Metropolitan division
        Assert.assertEquals(metroTeams.size(), 8, "Expected 8 teams in the Metropolitan division");
        // Print the names of the Metropolitan teams
        System.out.println("Metropolitan teams: " + metroTeams);
    }

    @Test
    public void testOldestTeamRosterNationality() throws Exception {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();

            page.navigate("https://www.nhl.com/fr/canadiens/roster");

            // Wait for the roster content to load
            Locator roster = page.locator(".nhl-container");
            roster.waitFor();

            // Count Canadian and US players with flexible selectors
            int canadianPlayers = page.locator("text=CAN").count();
            int usPlayers = page.locator("text=USA").count();

            Assert.assertTrue(canadianPlayers > usPlayers, "There are fewer Canadian players than US players");
        }
    }
}
