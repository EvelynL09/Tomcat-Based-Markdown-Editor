package project2package;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class RenderMD {
    private Parser parser;
    private HtmlRenderer renderer;
    public RenderMD(){
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }
    protected void finalize(){}
    public String myrender(String markdown){
        //e.g. String markdown = "This is *CS144*";
        String html = renderer.render(parser.parse(markdown));
        /* html has the string "<p>This is <em>CS144</em></p>\n" */
        return html;
    }

}