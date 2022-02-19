package ubb.soa.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@SpringBootApplication
@EnableEurekaClient
class GatewayApplication {
    @Bean
    @LoadBalanced
    fun restTemplate() = RestTemplate()

    @Bean
    fun routeLocator(
        routeLocatorBuilder: RouteLocatorBuilder,
        authGatewayFilter: AuthGatewayFilter
    ): RouteLocator {
        return routeLocatorBuilder.routes {
            route("items") {
                path("/items/**").filters {
                    it.stripPrefix(1).filter(authGatewayFilter)
                }.uri("lb://items")
            }
            route("users") {
                path("/users/**").filters {
                    it.stripPrefix(1).filter(authGatewayFilter)
                }.uri("lb://users")
            }
        }
    }

    @Bean
    fun cors(routePredicateHandlerMapping: RoutePredicateHandlerMapping): CorsConfiguration {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = listOf("*")
        corsConfiguration.allowedMethods = listOf(
            HttpMethod.POST.name,
            HttpMethod.GET.name,
            HttpMethod.OPTIONS.name,
            HttpMethod.DELETE.name,
            HttpMethod.PUT.name,
            HttpMethod.PATCH.name
        )
        corsConfiguration.allowedHeaders = listOf(
            HttpHeaders.ORIGIN,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.ACCEPT,
            HttpHeaders.AUTHORIZATION
        )
        corsConfiguration.maxAge = 3600L
        routePredicateHandlerMapping.setCorsConfigurations(
            object : HashMap<String?, CorsConfiguration?>() {
                init {
                    put("/**", corsConfiguration)
                }
            })

        return corsConfiguration
    }
}

@Component
class AuthGatewayFilter(
    val restTemplate: RestTemplate
) : GatewayFilter {
    override fun filter(exchange: ServerWebExchange?, chain: GatewayFilterChain?): Mono<Void> {
        return chain!!.filter(exchange)
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}