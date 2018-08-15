package test.blockchain.java;
import java.io.File;
import java.nio.file.Paths;

import io.github.robwin.markup.builder.MarkupLanguage;
import io.github.robwin.swagger2markup.GroupBy;
import io.github.robwin.swagger2markup.Swagger2MarkupConverter;
import io.github.robwin.swagger2markup.config.Swagger2MarkupConfig;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.Placement;
import org.asciidoctor.SafeMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/resources/application.xml", "file:src/main/resources/spring-mvc.xml" })
public class DemoApplicationTests {

	public void generateAsciiDocs() throws Exception {
    	Swagger2MarkupConverter.from("http:/localhost:8080/blockchain/" + "/swagger.json")
        .withPathsGroupedBy(GroupBy.TAGS)
        .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
        .build()
        .intoFolder("doc");
    	
    	Asciidoctor asciidoctor = Asciidoctor.Factory.create();
    	Attributes attributes = new Attributes();
    	attributes.setCopyCss(true);
    	attributes.setLinkCss(false);
    	attributes.setSectNumLevels(3);
    	attributes.setAnchors(true);
    	attributes.setSectionNumbers(true);
    	attributes.setHardbreaks(true);
    	attributes.setTableOfContents(Placement.LEFT);
    	attributes.setAttribute("generated", "F:\\testSrc\\docs");
    	OptionsBuilder optionsBuilder = OptionsBuilder.options()
    	        .backend("html5")
    	        .docType("book")
    	        .eruby("")
    	        .inPlace(true)
    	        .safe(SafeMode.UNSAFE)
    	        .attributes(attributes);
    	String asciiInputFile = "src/docs/index.adoc";
    	asciidoctor.convertFile(
    	        new File(asciiInputFile),
    	        optionsBuilder.get());
    }

}