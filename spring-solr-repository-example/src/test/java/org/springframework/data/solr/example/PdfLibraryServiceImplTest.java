package org.springframework.data.solr.example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.activation.FileTypeMap;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.tika.Tika;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

	@After
	public void tearDown() {
		pdfLibrarySolrRepository.deleteAll();
	}

	@Test
	public void testBeforeCreateTikaText_1() throws Exception {
		Tika tika = new Tika();
		tika.setMaxStringLength(Integer.MAX_VALUE);
		String pa = tika.parseToString(new File("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf"));

		PdfLibrarySolr plse = PdfLibrarySolr.builder()
				.id("1212125")
				.pdfLibraryId("plId003")
				.accountId("user010")
				.dateTime(new Date())
				.fileName("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf")
				.content(pa)
				.build();

		pdfLibrarySolrRepository.save(plse);

		PdfLibrarySolr result = pdfLibrarySolrRepository.findOne(plse.getId());

		String params = "account_id:user102 ファイル";
		Page<PdfLibrarySolr> it = pdfLibrarySolrRepository.findByMetaSearch(params);
		System.out.println(it.getTotalElements());

		assertThat(plse.getPdfLibraryId(), is(result.getPdfLibraryId()));
	}

	@Test
	public void testCreateAndFindByAttrPdfText_1() throws Exception {
		PdfLibrarySolr plse = PdfLibrarySolr.builder()
				.id("100000")
				.pdfLibraryId("plId001")
				.accountId("user001").dateTime(new Date()).fileName("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf")
				.build();

		pdfLibrarySolrRepository.savePdfLibrary(plse, "PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf");

		String params = "account_id:user102 ファイル";
		Page<PdfLibrarySolr> it = pdfLibrarySolrRepository.findByMetaSearch(params);

		assertThat(plse.getPdfLibraryId(), is(it.getContent().get(0).getPdfLibraryId()));
	}

	@Test
	public void testUpdateAndFindByAttrPdfText_1() throws Exception {
		PdfLibrarySolr plse = PdfLibrarySolr.builder()
				.id("200000")
				.pdfLibraryId("plId002")
				.accountId("user002").dateTime(new Date()).fileName("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_2.pdf")
				.build();
		pdfLibrarySolrRepository.savePdfLibrary(plse, "PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_2.pdf");

		PdfLibrarySolr updatePlse = PdfLibrarySolr.builder()
				.id("200000")
				.pdfLibraryId("plId102")
				.accountId("user102").dateTime(new Date()).fileName("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_3.pdf")
				.build();
		pdfLibrarySolrRepository.savePdfLibrary(updatePlse, "PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_3.pdf");

		PdfLibrarySolr result = pdfLibrarySolrRepository.findOne(plse.getId());

		assertThat(updatePlse.getId(), is(result.getId()));
		assertThat(updatePlse.getPdfLibraryId(), is(result.getPdfLibraryId()));
		assertThat(updatePlse.getAccountId(), is(result.getAccountId()));
		assertThat(updatePlse.getDateTime().toString(), is(result.getDateTime().toString()));
		assertThat(updatePlse.getFileName(), is(result.getFileName()));
		assertThat(true, is(result.getContent().contains("あきる野市")));
	}

	@Test
	public void testDelete_1() throws Exception {
		SolrServer solr = new HttpSolrServer("http://localhost:8983/solr/collection1");
		ContentStreamUpdateRequest update = new ContentStreamUpdateRequest("/update/extract");

		// (1)
	    FileTypeMap filetypeMap = FileTypeMap.getDefaultFileTypeMap();
	    String mimetype = filetypeMap.getContentType("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf");

	    try {
	    	// (2)
			update.addFile(new File("PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf"), mimetype);
			update.setParam("literal.id", "10000");
			update.setParam("literal.pdf_library_id", "plId100");
			update.setParam("literal.account_id", "user005");
			update.setParam("literal.date_time", new Date().toString());
			update.setParam("literal.file_name", "PdfLibraryServiceImplTest_testCreateAndFindByAttrPdfText_1.pdf");
			update.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);

			// (3)
			solr.request(update);
			solr.commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		// (1)
		pdfLibrarySolrRepository.delete("10000");

		PdfLibrarySolr result = pdfLibrarySolrRepository.findOne("10000");
		assertNull(result);
		// (2)
	}

	 @Test
	 public void testTokenizeJPN() throws Exception {
		PdfLibrarySolr plse = PdfLibrarySolr.builder()
				 .id("70000")
				.fileName("This is a sample program to test lucene and Kuromoji.私は東京都の国会議事堂に行きました").build();
		pdfLibrarySolrRepository.save(plse);

		// matches
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "this"));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "kUrOmoJI."));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "私の"));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "東京"));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "国会"));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "私は、東京都の国会議事堂に行きました。"));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "東京都 国会 議事堂"));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "東京都　国会　議事堂"));
		assertNotNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "行く"));
		// no matches
		assertNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "京都"));
		assertNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "国"));
		assertNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "の"));
		assertNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "会議"));
		assertNull(pdfLibrarySolrRepository.findByDesignationField("file_name", "議事"));

	 }
}
