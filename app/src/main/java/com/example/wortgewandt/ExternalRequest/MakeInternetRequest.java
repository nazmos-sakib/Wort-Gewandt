package com.example.wortgewandt.ExternalRequest;

import com.example.wortgewandt.Interface.Callback;
import com.example.wortgewandt.Model.Response;
import com.example.wortgewandt.Model.ResponseCodes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.MessageFormat;

public class MakeInternetRequest {

    public static void makeInternetRequestToGetEnglishMeaning(String word, Callback<Response> responseCallback){
        if (!word.isEmpty()){
            //String gWord = word.replace(" ","+");
            //https://translate.google.com/?sl=de&tl=en&text=gut+und+dir&op=translate
            //String url = String.format("https://translate.google.com/?sl=de&tl=en&text=%s&op=translate",gWord);
            String url = MessageFormat.format("https://m.dict.cc/deutsch-englisch/{0}.html", word);

            try {
                VolleyNetworkRequest.getInstance().makeWebRequest(
                        url, (res)->{
                            if (res!=null){
                                //shoUrl.setText(url);
                                findEnglishMeaning(res,responseCallback);
                            } else {
                                responseCallback.onCallback(
                                        new Response(ResponseCodes.PAGE_NOT_FOUND, "content not found",false)
                                );
                            }
                        }
                );
            } catch (NullPointerException e){
                responseCallback.onCallback(
                        new Response(ResponseCodes.VOLLEY_ERROR,"Volley is not initiated",false)
                );
            }


        }
    }


    private static void findEnglishMeaning(String response, Callback<Response> responseCallback){
        Document doc = Jsoup.parse(response);
        Element content = doc.getElementById("searchres_table");

        if (content!=null){
            Elements table = content.getElementsByTag("tr");

            Element tr = table.get(0);
            Elements td = tr.getElementsByTag("td") ;

            //meaning has been found
            responseCallback.onCallback(
                    new Response(ResponseCodes.SUCCESS,td.get(1).attr("data-term"),true)
            );

        } else {
            responseCallback.onCallback(
                    new Response(ResponseCodes.MEANING_NOT_FOUND,"meaning not found",false)
            );
        }

    }


}
