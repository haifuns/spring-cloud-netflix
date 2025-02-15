/*
 * Copyright 2013-2018 the original author or authors.
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

package org.springframework.cloud.netflix.turbine.stream;

import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

/**
 * @author Yongsung Yoon
 */
public class TurbineStreamConfigurationTest {
	TurbineStreamConfiguration turbineStreamConfiguration;
	List<Map<String, Object>> testMetricList;

	@Before
	public void setUp() {
		turbineStreamConfiguration = new TurbineStreamConfiguration();
		testMetricList = createBasicTestMetricList();
	}

	private List<Map<String,Object>> createBasicTestMetricList() {
		List<Map<String, Object>> testDataList = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("instanceId", "abc:127.0.0.1:8080");
		map.put("type", "HystrixCommand");
		testDataList.add(map);

		map = new HashMap<>();
		map.put("instanceId", "def:127.0.0.1:8080");
		map.put("type", "HystrixCommand");
		testDataList.add(map);

		map = new HashMap<>();
		map.put("instanceId", "xyz:127.0.0.1:8080");
		map.put("type", "HystrixThreadPool");
		testDataList.add(map);

		map = new HashMap<>();
		map.put("type", "ping");
		testDataList.add(map);

		map = new HashMap<>();
		map.put("dummy", "data");
		testDataList.add(map);

		return testDataList;
	}

	@Test
	public void shouldReturnAlwaysTruePredicateWithEmptyQueryParam() {
		Func1<Map<String, Object>, Boolean> clusterPredicate = turbineStreamConfiguration.createClusterPredicate(Collections.emptyMap());

		assertThatGivenPredicateReturnsTrueAsExpectedCount(5, clusterPredicate); // all
	}

	@Test
	public void shouldReturnAlwaysTruePredicateIfQueryParamsContainDefault() {
		Map<String, List<String>> queryMap = new HashMap<>();
		queryMap.put("cluster", Arrays.asList("default", "garbage"));

		Func1<Map<String, Object>, Boolean> clusterPredicate = turbineStreamConfiguration.createClusterPredicate(queryMap);

		assertThatGivenPredicateReturnsTrueAsExpectedCount(5, clusterPredicate); // all
	}

	@Test
	public void shouldReturnPredicateForGivenClusterName() {
		Map<String, List<String>> queryMap = new HashMap<>();
		queryMap.put("cluster", Arrays.asList("abc"));

		Func1<Map<String, Object>, Boolean> clusterPredicate = turbineStreamConfiguration.createClusterPredicate(queryMap);

		assertThatGivenPredicateReturnsTrueAsExpectedCount(3, clusterPredicate); // abc + ping + dummy
	}

	@Test
	public void shouldReturnPredicateForGivenMultipleClusterNames() {
		Map<String, List<String>> queryMap = new HashMap<>();
		queryMap.put("cluster", Arrays.asList("abc", "xyz"));

		Func1<Map<String, Object>, Boolean> clusterPredicate = turbineStreamConfiguration.createClusterPredicate(queryMap);

		assertThatGivenPredicateReturnsTrueAsExpectedCount(4, clusterPredicate); // abc + xyz + ping + dummy
	}

	@Test
	public void shouldReturnPredicateForUnknownClusterName() {
		Map<String, List<String>> queryMap = new HashMap<>();
		queryMap.put("cluster", Arrays.asList("ttt", "eee"));

		Func1<Map<String, Object>, Boolean> clusterPredicate = turbineStreamConfiguration.createClusterPredicate(queryMap);

		assertThatGivenPredicateReturnsTrueAsExpectedCount(2, clusterPredicate); // ping + dummy
	}

	void assertThatGivenPredicateReturnsTrueAsExpectedCount(int expectedCount, Func1<Map<String, Object>, Boolean> predicate) {
		assertEquals(expectedCount, countEmittedMetricsWithPredicate(predicate));
	}

	int countEmittedMetricsWithPredicate(Func1<Map<String, Object>, Boolean> predicate) {
		try {
			return Observable.from(this.testMetricList)
							 .filter(predicate)
							 .count()
							 .toBlocking()
							 .single();

		} catch (NoSuchElementException ex) {
			return 0;
		}
	}
}
