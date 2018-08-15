package test.blockchain.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import test.blockchain.bean.Book;
import test.blockchain.bean.CookBook;
import test.blockchain.bean.Person;
import test.blockchain.bean.PersonTest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/resources/application.xml", "file:src/main/resources/spring-mvc.xml" })
public class TestJava {
	@Autowired
	Mapper mapper;

	@Test
	public void MyFirstDozerDemo() {
		Book book1 = new Book();
		book1.setAuthor("dennis");
		book1.setName("dozer demo");
		DozerBeanMapper mapper = new DozerBeanMapper();
		/*
		 * Book book2 = new Book();
		 * book2=(Book)mapper.map(book1,com.blockchain.bean.Book.class);
		 */
		CookBook cookBook = new CookBook();
		List myMappingFiles = new ArrayList();
		myMappingFiles.add("dozerBeanMapping.xml");
		mapper.setMappingFiles(myMappingFiles);
		cookBook = (CookBook) mapper.map(book1, CookBook.class);
		System.out.println("cookBook's name:" + cookBook.getBookName() + "     cookBook's author:" + cookBook.getAuthor());
	}

	@Test
	public void DozerTestDemo() {
		Person person = new Person();
		person.setId("dennis");
		person.setName("dozer demo");

		/*
		 * Book book2 = new Book();
		 * book2=(Book)mapper.map(book1,com.blockchain.bean.Book.class);
		 */
		PersonTest personTest = new PersonTest();

		personTest = mapper.map(person, PersonTest.class);
		System.out.println("cookBook's name:" + personTest.getMyname() + "     cookBook's author:" + personTest.getMyId());
	}

	@Test
	public void test2() throws IOException {

		File file = new File("E:\\test.csv");
		FileWriter fstream = null;
		if (file.exists() == false) {
			fstream = new FileWriter(file, true);

			BufferedWriter out = new BufferedWriter(fstream);
			out.write("account");
			out.write(",");
			out.write("private_key");
			out.write(",");
			out.write("errmsg");
			out.write("\n");
		} else {
			fstream = new FileWriter(file, true);

		}
		BufferedWriter out = new BufferedWriter(fstream);

		if (vars.get("account") != null) {
			out.write(vars.get("account"));
			out.write(",");
		} else {
			out.write(",");
		}
		if (vars.get("private_key") != null) {
			out.write(vars.get("private_key"));
			out.write(",");
		} else {
			out.write(",");
		}
		if (vars.get("errmsg") != null) {
			out.write(vars.get("errmsg"));
			out.write(",");
		} else {
			out.write(",");
		}
		out.write(System.getProperty("line.separator"));

		out.close();
		fstream.close();

	}
}
