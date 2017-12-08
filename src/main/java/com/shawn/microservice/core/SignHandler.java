package com.shawn.microservice.core;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import com.shawn.microservice.util.WebClientUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by shawn on 2017/5/22.
 */
public class SignHandler {
    public static void main(String[] args) throws IOException {
        //https://zhiyou.smzdm.com/user/login/?redirect_to=
        WebClient webClient = WebClientUtil.getWebClient();



        final HtmlPage page = webClient.getPage("https://zhiyou.smzdm.com/user/login/?redirect_to=");
        //System.out.println(page.asText());
        System.out.println(webClient.getCookieManager().getCookies());
        HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
        HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
        HtmlButtonInput loginSubmit = (HtmlButtonInput) page.getElementById("login_submit");
        username.setValueAttribute("wx5223@21cn.com");
        password.setValueAttribute("******");
        final HtmlPage page2 = loginSubmit.click();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //webClient.waitForBackgroundJavaScript(10000);
        System.out.println(webClient.getCookieManager().getCookies());
        List<?> list = page2.getByXPath("//p[contains(@class,'notice_error')]");
        if(list != null) {
            HtmlParagraph p = (HtmlParagraph) list.get(0);
            System.out.println(p.asText());
        }
        System.out.println("————————————————————————————————————————————————————");
        final HtmlPage page3 = webClient.getPage("http://zhiyou.smzdm.com/user");
        //webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        System.out.println("————————————————————————————————————————————————————");
        System.out.println(page3.getWebResponse().getContentAsString());

/*
        final HtmlPage page3 = webClient.getPage("https://www.smzdm.com");
        //webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        System.out.println("————————————————————————————————————————————————————");
        //System.out.println(page3.getWebResponse().getContentAsString());

        HtmlAnchor link = page3.getFirstByXPath("//a[@class='J_punch']");
        final HtmlPage page4 = link.click();
        //webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        System.out.println(page4.getWebResponse().getContentAsString());*/
        //System.out.println(page2.getWebResponse().getContentAsString());


    }


    private static class JsonResponseWebWrapper extends WebConnectionWrapper {

        public JsonResponseWebWrapper(WebClient webClient){
            super(webClient);
        }

        String jsonResponse;

        @Override
        public WebResponse getResponse(WebRequest request) throws IOException {
            System.out.println(request.getUrl());
            WebResponse response = super.getResponse(request);
            //extract JSON from response
            jsonResponse = response.getContentAsString();
            return response;
        }

        public String getJsonResponse() {
            return jsonResponse;
        }
    }
}
