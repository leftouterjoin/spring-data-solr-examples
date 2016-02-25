package org.springframework.data.solr.example.repository;



import java.io.File;
import java.io.IOException;

import javax.activation.FileTypeMap;

import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.example.model.PdfLibrarySolr;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

@NoRepositoryBean
public class PdfLibrarySolrRepository  extends SimpleSolrRepository<PdfLibrarySolr, String> implements PdfLibrarySolrBaseRepository {
	@Autowired
	private SolrOperations solrTemplate;

	public void savePdfLibrary(PdfLibrarySolr param, String path) {
//		PartialUpdate update = new PartialUpdate("id", param.getId());
//		try {
//			update.setValueOfField("content", new FileInputStream(path));
//		} catch (IOException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
//
//		update.setValueOfField("pdf_library_id", param.getPdfLibraryId());
//		update.setValueOfField("account_id", param.getAccountId());
//		update.setValueOfField("date_time", param.getDateTime());
//		update.setValueOfField("file_name", param.getFileName());

		ContentStreamUpdateRequest update = new ContentStreamUpdateRequest("");
		FileTypeMap filetypeMap = FileTypeMap.getDefaultFileTypeMap();
	    String mimetype = filetypeMap.getContentType(path);

	    try {
			update.addFile(new File(path), mimetype);
		} catch (IOException e) {
			e.printStackTrace();
		}
		update.setParam("pdf_library_id", param.getPdfLibraryId());
		update.setParam("account_id", param.getAccountId());
		update.setParam("date_time", param.getDateTime().toString());
		update.setParam("file_name", param.getFileName());
		update.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);

		solrTemplate.saveBean(update);
		solrTemplate.commit();
	}

}
