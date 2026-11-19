package br.com.dev.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutes {

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				// GITHUB Service
				.route("GITHUB_SERVICE", r -> r.path("/github/**")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("githubServiceCircuitBreaker")
								.setFallbackUri("forward:/fallback/github")
								.setRouteId("GITHUB_SERVICE")
						))
						.uri("lb://GITHUB-SERVICE")
				)
				// MISSAO Service
				.route("MISSAO_SERVICE", r -> r.path("/missao/**")
						.filters(f -> f.circuitBreaker(config -> config
								.setName("missaoServiceCircuitBreaker")
								.setFallbackUri("forward:/fallback/missao")
								.setRouteId("MISSAO_SERVICE")
						))
						.uri("lb://MISSAO-SERVICE")
				)
				.build();
	}
}
