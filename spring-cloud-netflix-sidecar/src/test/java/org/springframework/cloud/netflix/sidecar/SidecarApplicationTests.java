/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.sidecar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SidecarApplicationTests {

	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes = SidecarApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT, value = {
			"spring.application.name=mytest", "spring.cloud.client.hostname=mhhost", "spring.application.instance_id=1",
			"eureka.instance.hostname=mhhost", "sidecar.port=7000", "sidecar.ipAddress=127.0.0.1" })
	public static class EurekaTestConfigBeanTest {
		@Autowired
		EurekaInstanceConfigBean config;

		@Test
		public void testEurekaConfigBean() {
			assertThat(this.config.getAppname(), equalTo("mytest"));
			assertThat(this.config.getHostname(), equalTo("mhhost"));
			assertThat(this.config.getInstanceId(), equalTo("mhhost:mytest:1"));
			assertThat(this.config.getNonSecurePort(), equalTo(7000));
		}
	}

	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes = SidecarApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT, value = {
			"spring.application.name=mytest", "spring.cloud.client.hostname=mhhost", "spring.application.instance_id=1",
			"sidecar.hostname=mhhost", "sidecar.port=7000", "sidecar.ipAddress=127.0.0.1" })
	public static class NewPropertyEurekaTestConfigBeanTest {
		@Autowired
		EurekaInstanceConfigBean config;

		@Test
		public void testEurekaConfigBean() {
			assertThat(this.config.getAppname(), equalTo("mytest"));
			assertThat(this.config.getHostname(), equalTo("mhhost"));
			assertThat(this.config.getInstanceId(), equalTo("mhhost:mytest:1"));
			assertThat(this.config.getNonSecurePort(), equalTo(7000));
		}
	}

	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes = SidecarApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT, value = {
			"spring.application.name=mytest", "spring.cloud.client.hostname=mhhost", "spring.application.instance_id=1",
			"eureka.instance.hostname=mhhost1", "sidecar.hostname=mhhost2", "sidecar.port=7000", "sidecar.ipAddress=127.0.0.1" })
	public static class BothPropertiesEurekaTestConfigBeanTest {
		@Autowired
		EurekaInstanceConfigBean config;

		@Test
		public void testEurekaConfigBeanEurekaInstanceHostnamePropertyShouldBeUsed() {
			assertThat(this.config.getAppname(), equalTo("mytest"));
			assertThat(this.config.getHostname(), equalTo("mhhost1"));
			assertThat(this.config.getInstanceId(), equalTo("mhhost:mytest:1"));
			assertThat(this.config.getNonSecurePort(), equalTo(7000));
		}
	}

	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes = SidecarApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT, value = {
			"spring.application.name=mytest", "spring.cloud.client.hostname=mhhost", "spring.application.instance_id=1",
			"eureka.instance.hostname=mhhost1", "sidecar.hostname=mhhost2", "sidecar.port=7000", "sidecar.ipAddress=127.0.0.1",
			"management.context-path=/foo"})
	public static class ManagementContextPathStatusAndHealthCheckUrls {
		@Autowired
		EurekaInstanceConfigBean config;

		@Test
		public void testStatusAndHealthCheckUrls() {
			assertThat(this.config.getStatusPageUrl(), equalTo("http://mhhost2:0/foo/info"));
			assertThat(this.config.getHealthCheckUrl(), equalTo("http://mhhost2:0/foo/health"));
		}
	}

	@RunWith(SpringJUnit4ClassRunner.class)
	@SpringBootTest(classes = SidecarApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT, value = {
			"spring.application.name=mytest", "spring.cloud.client.hostname=mhhost", "spring.application.instance_id=1",
			"eureka.instance.hostname=mhhost1", "sidecar.hostname=mhhost2", "sidecar.port=7000", "sidecar.ipAddress=127.0.0.1",
			"server.context-path=/foo"})
	public static class ServerContextPathStatusAndHealthCheckUrls {
		@Autowired
		EurekaInstanceConfigBean config;

		@Test
		public void testStatusAndHealthCheckUrls() {
			assertThat(this.config.getStatusPageUrl(), equalTo("http://mhhost2:0/foo/info"));
			assertThat(this.config.getHealthCheckUrl(), equalTo("http://mhhost2:0/foo/health"));
		}
	}
}
