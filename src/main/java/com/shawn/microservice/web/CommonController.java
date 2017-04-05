package com.shawn.microservice.web;

import com.shawn.microservice.core.SmzdmHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shawn on 2017/3/9.
 */
@Controller
@RequestMapping("/")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    private final SpelView indexView = new SpelView(
            "<html><body><script src='/js/jquery-2.1.4.min.js'></script><script src='/js/index.js'></script></body></html>");
    @RequestMapping(value ="/")
    public View index(){

        return indexView;
    }

    @RequestMapping(value ="/execute")
    @ResponseBody
    public String execute(){
        SmzdmHandler.getArticleNow();
        return "success";
    }

    @RequestMapping(value ="{name}/price/asc")
    @ResponseBody
    public List<SmzdmHandler.Article> priceAsc(@PathVariable String name) {
        List<SmzdmHandler.Article> articleList = SmzdmHandler.getArticle(name);
        Collections.sort(articleList);
        return articleList;
    }
    @RequestMapping(value ="{name}/price/desc")
    @ResponseBody
    public List<SmzdmHandler.Article> desc(@PathVariable String name){
        List<SmzdmHandler.Article> articleList = SmzdmHandler.getArticle(name);
        Collections.sort(articleList, new Comparator<SmzdmHandler.Article>(){
            public int compare(SmzdmHandler.Article arg0, SmzdmHandler.Article arg1) {
                return arg1.getPrice().compareTo(arg0.getPrice());
            }
        });
        return articleList;
    }

    @RequestMapping(value ="{name}/time/desc")
    @ResponseBody
    public List<SmzdmHandler.Article> time(@PathVariable String name){
        List<SmzdmHandler.Article> articleList = SmzdmHandler.getArticle(name);
        Collections.sort(articleList, new Comparator<SmzdmHandler.Article>(){
            public int compare(SmzdmHandler.Article arg0, SmzdmHandler.Article arg1) {
                return compareConvertLong(arg1.getTimesort(), arg0.getTimesort());
            }
        });
        return articleList;
    }

    @RequestMapping(value ="{name}/comment/desc")
    @ResponseBody
    public List<SmzdmHandler.Article> comment(@PathVariable String name){
        List<SmzdmHandler.Article> articleList = SmzdmHandler.getArticle(name);
        Collections.sort(articleList, new Comparator<SmzdmHandler.Article>(){
            public int compare(SmzdmHandler.Article arg0, SmzdmHandler.Article arg1) {
                return compareConvertLong(arg1.getArticle_comment(), arg0.getArticle_comment());
            }
        });
        return articleList;
    }

    @RequestMapping(value ="{name}/worthy/desc")
    @ResponseBody
    public List<SmzdmHandler.Article> worthy(@PathVariable String name){
        List<SmzdmHandler.Article> articleList = SmzdmHandler.getArticle(name);
        Collections.sort(articleList, new Comparator<SmzdmHandler.Article>(){
            public int compare(SmzdmHandler.Article arg0, SmzdmHandler.Article arg1) {
                return compareConvertLong(arg1.getArticle_worthy(), arg0.getArticle_worthy());
            }
        });
        return articleList;
    }


    private int compareConvertLong(String key1, String key2) {
        Long key1Long = Long.parseLong(key1);
        Long key2Long = Long.parseLong(key2);
        return key1Long.compareTo(key2Long);
    }

    private static class SpelView implements View {

        private final String template;

        private final StandardEvaluationContext context = new StandardEvaluationContext();

        private PropertyPlaceholderHelper helper;

        private PropertyPlaceholderHelper.PlaceholderResolver resolver;

        public SpelView(String template) {
            this.template = template;
            this.context.addPropertyAccessor(new MapAccessor());
            this.helper = new PropertyPlaceholderHelper("${", "}");
            this.resolver = new SpelPlaceholderResolver(this.context);
        }

        @Override
        public String getContentType() {
            return "text/html";
        }

        @Override
        public void render(Map<String, ?> model, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
            if (response.getContentType() == null) {
                response.setContentType(getContentType());
            }
            Map<String, Object> map = new HashMap<String, Object>(model);
            map.put("path", request.getContextPath());
            this.context.setRootObject(map);
            String result = this.helper.replacePlaceholders(this.template, this.resolver);
            response.getWriter().append(result);
        }

    }

    /**
     * SpEL based {@link PropertyPlaceholderHelper.PlaceholderResolver}.
     */
    private static class SpelPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        private final SpelExpressionParser parser = new SpelExpressionParser();

        private final StandardEvaluationContext context;

        public SpelPlaceholderResolver(StandardEvaluationContext context) {
            this.context = context;
        }

        @Override
        public String resolvePlaceholder(String name) {
            Expression expression = this.parser.parseExpression(name);
            try {
                Object value = expression.getValue(this.context);
                return HtmlUtils.htmlEscape(value == null ? null : value.toString());
            }
            catch (Exception ex) {
                return null;
            }
        }

    }

}
