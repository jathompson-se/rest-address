package jathompson.randomizer.restaddress;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class to verify basic functionality of REST call <code>/randomizer/address</code>
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestAddressApplicationTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	public void noParamAddressShouldReturnRandomAddress() throws Exception {
		this.mockMvc.perform(get("/randomizer/address")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void addressShouldReturnUSAAddress() throws Exception {
		this.mockMvc.perform(get("/randomizer/address?country=USA")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.countryCode").value("USA"));
	}

	@Test
	public void addressShouldReturnCANAddress() throws Exception {
		this.mockMvc.perform(get("/randomizer/address?country=can")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.countryCode").value("CAN"));
	}

	@Test
	public void addressShouldReturnMEXAddress() throws Exception {
		this.mockMvc.perform(get("/randomizer/address?country=Mex")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.countryCode").value("MEX"));
	}

	@Test
	public void addressShouldReturnNLDAddress() throws Exception {
		this.mockMvc.perform(get("/randomizer/address?country=nlD")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.countryCode").value("NLD"));
	}

	@Test
	public void invalidCountryShouldReturnNotFoundError() throws Exception {
		this.mockMvc.perform(get("/randomizer/address?country=123")).andDo(print()).andExpect(status().isNotFound());
	}
}
