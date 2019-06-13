package com.easysign.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.easysign.service.ConfigData;
import com.easysign.service.Configuration;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class ConfigController {

	private HttpHeaders headers;

	@Autowired
	private Configuration configuration;

	private RestTemplate restTemplate = new RestTemplate();

	private Map config = new HashMap<>();

	public void refreshConfig() {

		restTemplate.postForObject("http://localhost:8888/actuator/bus-refresh/", null, String.class);

	}

	@GetMapping("/get-config/{serviceName}")
	public @ResponseBody Map listConfig(@PathVariable String serviceName) throws ConfigurationException {
		config = configuration.getConfiguration(serviceName);
		log.info("List configuration from service [{}] : {} ", serviceName, config);
		return config;
	}

	@PostMapping("/add-config")
	public @ResponseBody void addConfig(@RequestBody ConfigData conf) {
		log.info("Add configuration to service [{}] : {}", conf.getServiceName(), conf);
		configuration.saveConfig(conf);
	}

	@PostMapping("/update-config")
	public @ResponseBody void updatingConfig(@RequestBody ConfigData conf) {
		log.info("Update configuration from service [{}]  with : {}", conf.getServiceName(), config.toString());
		configuration.updateConfig(conf);
	}

	@PostMapping("/remove-config")
	public @ResponseBody void removeConfig(@RequestBody ConfigData conf) {
		log.info("remove configuration line {} from service [{}]", conf.getKey(), conf.getServiceName());
		configuration.removeConfig(conf);
	}

}