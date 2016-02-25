package org.springframework.data.solr.example;

import java.io.File;
import java.io.Reader;
import java.util.Date;

import org.apache.tika.Tika;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.example.model.PdfLibrarySolr;
import org.springframework.data.solr.example.repository.PdfLibrarySolrRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Solrによる全文検索検証用テストケース
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/springframework/data/solr/example/applicationContext.xml")
public class PdfLibraryServiceImplTest {
//	@Autowired
//	private ResourceLoader resourceLoader;
	@Autowired
	private PdfLibrarySolrRepository pdfLibrarySolrRepository;

	@Before
	public void init() {
	}

	@Test
	public void testCreateAndFindByAttrPdfText_1() throws Exception {
		File f = new File("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf");
		Tika tika = new Tika();
		tika.detect(f);
		Reader r = tika.parse(f);
		tika.getParser();

		System.out.println(tika.parseToString(f));

		PdfLibrarySolr plse = PdfLibrarySolr.builder()
				.pdfLibraryId("plId002")
				.accountId("user001").dateTime(new Date()).fileName("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf")
				.content("ドキュメントフィールド").build();

		pdfLibrarySolrRepository.save(plse);		// (2)

		PdfLibrarySolr result = pdfLibrarySolrRepository.findOne(plse.getId());

		Assert.assertEquals(plse.getId(), result.getId());
	}

	@Test
	public void testCreateSolrJ_1() throws Exception {

		PdfLibrarySolr plse = PdfLibrarySolr.builder()
				.id("12584584")
				.pdfLibraryId("plId005")
				.accountId("user010").dateTime(new Date()).fileName("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf").build();
		pdfLibrarySolrRepository.savePdfLibrary(plse, "PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf");
//		SolrServer solr = new HttpSolrServer("http://localhost:8983/solr/collection1");
//		ContentStreamUpdateRequest update = new ContentStreamUpdateRequest("/update/extract");
//
//		// (1)
//	    FileTypeMap filetypeMap = FileTypeMap.getDefaultFileTypeMap();
//	    String mimetype = filetypeMap.getContentType("ApacheSolr入門.pdf");
//
//	    try {
//	    	// (2)
//			update.addFile(new File("ApacheSolr入門.pdf"), mimetype);
////			update.setParam("literal.id", "999258");
//			update.setParam("literal.pdf_library_id", "plId003");
//			update.setParam("literal.account_id", "user001");
//			update.setParam("literal.date_time", new Date().toString());
//			update.setParam("literal.file_name", "ApacheSolr入門.pdf");
//			update.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
//
//			// (3)
//			solr.request(update);
//			solr.commit();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SolrServerException e) {
//			e.printStackTrace();
//		}
	}

	@Test
	public void testUpdateAndFindByAttrPdfText_1() throws Exception {

	}

	@Test
	public void testDelete_1() throws Exception {
		// (1)

		// (2)
	}

	 @Test
	 public void testTokenizeJPN() throws Exception {

	 }
}
