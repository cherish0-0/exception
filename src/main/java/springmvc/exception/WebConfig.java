package springmvc.exception;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springmvc.exception.filter.LogFilter;
import springmvc.exception.interceptor.LogInterceptor;
import springmvc.exception.resolver.MyHandlerExceptionResolver;
import springmvc.exception.resolver.UserHandlerExceptionResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/error-page/**");
        // dispatcherType 설정은 인터셉터에는 적용되지 않지만 excludePathPatterns를 통해 제어 가능
        // 즉, 경로 정보로 중복 호출 제거 => WAS가 오류 페이지 확인 후 뷰에 렌더링할 때 인터셉터를 거치지 않음
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        // 예외 처리 핸들러 추가
        resolvers.add(new MyHandlerExceptionResolver());
        // 이 핸들러는 IllegalArgumentException을 400으로 처리하고, 다른 예외는 처리하지 않음
        // 따라서, IllegalArgumentException이 발생하면 이 핸들러가 호출되어 400 상태 코드를 반환함
        resolvers.add(new UserHandlerExceptionResolver());
    }

    // @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // 필터 순서 설정
        filterRegistrationBean.addUrlPatterns("/*"); // 필터 적용 URL 패턴 설정
        // 필터는 dispatcherType=REQUEST로 중복 호출 제거 => WAS가 오류 페이지 확인 후 뷰에 렌더링할 때 필터를 거치지 않음
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        return filterRegistrationBean;
    }
}
