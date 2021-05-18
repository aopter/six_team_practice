package com.sixpoints.config;

import com.sixpoints.interceptor.MyLoginHandlerInterceptor;
import com.sixpoints.utils.PathUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MyMVCConfig {
    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("html/ui-login");
                registry.addViewController("/index.html").setViewName("html/index-dashboard");
                registry.addViewController("/main.html").setViewName("html/index-dashboard");
                registry.addViewController("/index-dashboard.html").setViewName("html/index-dashboard.html");
                registry.addViewController("/ui-login.html").setViewName("html/ui-login.html");
                registry.addViewController("/ui-register.html").setViewName("html/ui-register.html");
                registry.addViewController("/ui-404.html").setViewName("html/ui-404.html");
                registry.addViewController("/layout-default.html").setViewName("html/layout-default.html");
                registry.addViewController("/layout-collapsed.html").setViewName("html/layout-collapsed.html");
                registry.addViewController("/layout-chat.html").setViewName("html/layout-chat.html");
                registry.addViewController("/layout-boxed.html").setViewName("html/layout-boxed.html");
                registry.addViewController("/layout-boxed-collapsed.html").setViewName("html/layout-boxed-collapsed.html");
                registry.addViewController("/layout-boxed-chat.html").setViewName("html/layout-boxed-chat.html");
                registry.addViewController("/calendar.html").setViewName("html/calendar.html");
                registry.addViewController("/mail-inbox.html").setViewName("html/mail-inbox.html");
                registry.addViewController("/mail-compose.html").setViewName("html/mail-compose.html");
                registry.addViewController("/mail-view.html").setViewName("html/mail-view.html");
                registry.addViewController("/chat-api.html").setViewName("html/chat-api.html");
                registry.addViewController("/chat-windows.html").setViewName("html/chat-windows.html");
                registry.addViewController("/charts-echart-line.html").setViewName("html/charts-echart-line.html");
                registry.addViewController("/charts-echart-bar.html").setViewName("html/charts-echart-bar.html");
                registry.addViewController("/charts-morris-line.html").setViewName("html/charts-morris-line.html");
                registry.addViewController("/charts-morris-bar.html").setViewName("html/charts-morris-bar.html");
                registry.addViewController("/charts-morris-area.html").setViewName("html/charts-morris-area.html");
                registry.addViewController("/charts-chartjs-line.html").setViewName("html/charts-chartjs-line.html");
                registry.addViewController("/charts-chartjs-bar.html").setViewName("html/charts-chartjs-bar.html");
                registry.addViewController("/charts-chartjs-pie-donut.html").setViewName("html/charts-chartjs-pie-donut.html");
                registry.addViewController("/charts-flot-area.html").setViewName("html/charts-flot-area.html");
                registry.addViewController("/charts-flot-line.html").setViewName("html/charts-flot-line.html");
                registry.addViewController("/charts-sparkline-line.html").setViewName("html/charts-sparkline-line.html");
                registry.addViewController("/charts-sparkline-bar.html").setViewName("html/charts-sparkline-bar.html");
                registry.addViewController("/charts-sparkline-composite.html").setViewName("html/charts-sparkline-composite.html");
                registry.addViewController("/ui-timeline-centered.html").setViewName("html/ui-timeline-centered.html");
                registry.addViewController("/ui-timeline-left.html").setViewName("html/ui-timeline-left.html");
                registry.addViewController("/ui-pricing-expanded.html").setViewName("html/ui-pricing-expanded.html");
                registry.addViewController("/ui-pricing-narrow.html").setViewName("html/ui-pricing-narrow.html");
                registry.addViewController("/ui-icons.html").setViewName("html/ui-icons.html");
                registry.addViewController("/ui-fontawesome.html").setViewName("html/ui-fontawesome.html");
                registry.addViewController("/ui-glyphicons.html").setViewName("html/ui-glyphicons.html");
                registry.addViewController("/form-elements.html").setViewName("html/form-elements.html");
                registry.addViewController("/form-elements-premade.html").setViewName("html/form-elements-premade.html");
                registry.addViewController("/form-elements-icheck.html").setViewName("html/form-elements-icheck.html");
                registry.addViewController("/form-elements-grid.html").setViewName("html/form-elements-grid.html");
                registry.addViewController("/form-wizard.html").setViewName("html/form-wizard.html");
                registry.addViewController("/form-validation.html").setViewName("html/form-validation.html");
                registry.addViewController("/ui-tabs.html").setViewName("html/ui-tabs.html");
                registry.addViewController("/ui-accordion.html").setViewName("html/ui-accordion.html");
                registry.addViewController("/ui-progress.html").setViewName("html/ui-progress.html");
                registry.addViewController("/ui-buttons.html").setViewName("html/ui-buttons.html");
                registry.addViewController("/ui-modals.html").setViewName("html/ui-modals.html");
                registry.addViewController("/ui-alerts.html").setViewName("html/ui-alerts.html");
                registry.addViewController("/ui-notifications.html").setViewName("html/ui-notifications.html");
                registry.addViewController("/ui-tooltips.html").setViewName("html/ui-tooltips.html");
                registry.addViewController("/ui-popovers.html").setViewName("html/ui-popovers.html");
                registry.addViewController("/ui-navbars.html").setViewName("html/ui-navbars.html");
                registry.addViewController("/ui-dropdowns.html").setViewName("html/ui-dropdowns.html");
                registry.addViewController("/ui-breadcrumbs.html").setViewName("html/ui-breadcrumbs.html");
                registry.addViewController("/ui-pagination.html").setViewName("html/ui-pagination.html");
                registry.addViewController("/ui-labels-badges.html").setViewName("html/ui-labels-badges.html");
                registry.addViewController("/ui-typography.html").setViewName("html/ui-typography.html");
                registry.addViewController("/ui-grids.html").setViewName("html/ui-grids.html");
                registry.addViewController("/ui-panels.html").setViewName("html/ui-panels.html");
                registry.addViewController("/ui-group-list.html").setViewName("html/ui-group-list.html");
                registry.addViewController("/charts-morris-pie.html").setViewName("html/charts-morris-pie.html");
                registry.addViewController("/mail-sent.html").setViewName("html/mail-sent.html");
                registry.addViewController("/mail-drafts.html").setViewName("html/mail-drafts.html");
                registry.addViewController("/mail-important.html").setViewName("html/mail-important.html");
                registry.addViewController("/mail-trash.html").setViewName("html/mail-trash.html");
                registry.addViewController("/ui-profile.html").setViewName("html/ui-profile.html");
                registry.addViewController("/account-confirmation.html").setViewName("html/account-confirmation.html");
                registry.addViewController("/ui-support.html").setViewName("html/ui-support.html");
                registry.addViewController("/modify-dynasty.html").setViewName("html/modify-dynasty.html");
                registry.addViewController("/modify-incident.html").setViewName("html/modify-incident.html");
                registry.addViewController("/add-incident.html").setViewName("html/add-incident.html");
                registry.addViewController("/modify-card.html").setViewName("html/modify-card.html");
                registry.addViewController("/problem-home.html").setViewName("html/problem-home.html");
                registry.addViewController("/add-problem.html").setViewName("html/add-problem.html");
                registry.addViewController("/modify-problem.html").setViewName("html/modify-problem.html");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new MyLoginHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/", "/ui-register.html", "/ui-login.html", "/admin/login", "/card/**", "/dynasty/**", "/incident/**", "/picture/**", "/book/**",
                                "/problem/**", "/pricing/**", "/rule", "/status/**", "/team", "/user/**", "/usercard/**", "/userbook/**",
                                "/userdetails/**", "/userproblem/**", "/recharge/**", "/userunlockdynasty/**", "/userincident/**", "/static/**", "/img/**");

            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/img/**").addResourceLocations("file:" + (PathUtil.getRootPath() + "\\img\\").replace("\\", "/"));
                super.addResourceHandlers(registry);
            }
        };
        return adapter;
    }
}
