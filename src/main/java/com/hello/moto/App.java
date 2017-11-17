package com.hello.moto;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage page;
        try
        {

            File ff=new File("resultat.csv"); // définir l'arborescence
            ff.createNewFile();
            FileWriter ffw=new FileWriter(ff);
            ffw.write("nom;annee\n");

            page = webClient.getPage("http://www.anniversaire-celebrite.com/abc-a.html");
           // page = webClient.getPage("http://www.anniversaire-celebrite.com/abc-c-2.html");
            System.out.println(page.getTitleText());
            System.out.println(page.getPage());
            HtmlElement lettresPagination = (HtmlElement) page.getByXPath("//div[contains(@class, 'pagination')]").get(0) ;
            List<HtmlAnchor> lettres=(lettresPagination.getHtmlElementsByTagName("a"));
            HtmlElement numeros = (HtmlElement) page.getByXPath("//div[contains(@class, 'pagination')]").get(0);
            // System.out.println(numeros);
            //  System.exit(0);
            /** parcours des lettres*/
            for (HtmlAnchor i:lettres)
            {
                /**parcours des numeros de chaque lettre*/
                do
                {
                    /**parcours des annonces*/
                    List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[contains(@class, 'item large-2 medium-4 small-6 columns')]") ;
                    for(HtmlElement htmlItem : items)
                    {
                        HtmlAnchor itemAnchor =  ((HtmlAnchor) htmlItem.getFirstByXPath(".//a"));
                        String itemUrl = itemAnchor.getHrefAttribute() ;
                        String theone="http://www.anniversaire-celebrite.com/"+itemUrl;
                        HtmlPage page1 =  webClient.getPage(theone);
                        // System.out.println( page1.getUrl());
                        System.out.println(page1.getElementsByTagName("h1").get(1).asText());
                        System.out.println(page1.getElementsByTagName("time").get(0).getAttribute("datetime"));
                        ffw.write(page1.getElementsByTagName("h1").get(1).asText());  // écrire une ligne dans le fichier resultat.txt
                        ffw.write(";"+page1.getElementsByTagName("time").get(0).getAttribute("datetime"));  // écrire une ligne dans le fichier resultat.txt
                        ffw.write("\n"); // forcer le passage à la ligne

                    }
                    // System.exit(1);
                    /**On passe au numéro suivant*/
                    HtmlAnchor petitAnchois = (HtmlAnchor)numeros.getByXPath("//a[contains(@class, 'next page-numbers')]").get(0);
                    System.out.println(petitAnchois.getHrefAttribute() );
                    String lienAnchoisSuivant="http://www.anniversaire-celebrite.com/"+petitAnchois.getHrefAttribute();
                    System.out.println(lienAnchoisSuivant);
                    page=webClient.getPage(lienAnchoisSuivant);
                    numeros = (HtmlElement) page.getByXPath("//div[contains(@class, 'pagination')]").get(0);
                }while(!numeros.getByXPath("//a[contains(@class, 'next page-numbers')]").isEmpty());

                /** il n 'y a pas de numéro suivant on passe à la page suivante*/
                System.out.println(i.getHrefAttribute());
                String pagesuivante="http://www.anniversaire-celebrite.com/"+i.getHrefAttribute();
                System.out.println(pagesuivante);
                page=webClient.getPage(pagesuivante);
                numeros = (HtmlElement) page.getByXPath("//div[contains(@class, 'pagination')]").get(0);
            }
            ffw.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
