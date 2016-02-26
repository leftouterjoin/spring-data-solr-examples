package org.springframework.data.solr.example.repository;



import java.io.File;
import java.io.IOException;

import javax.activation.FileTypeMap;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.example.model.PdfLibrarySolr;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

@NoRepositoryBean
public class PdfLibrarySolrRepository  extends SimpleSolrRepository<PdfLibrarySolr, String> implements PdfLibrarySolrBaseRepository {
	@Autowired
	private SolrOperations solrTemplate;

	public void savePdfLibrary(PdfLibrarySolr param, String path) {
		ContentStreamUpdateRequest update = new ContentStreamUpdateRequest("/update/extract");
		FileTypeMap filetypeMap = FileTypeMap.getDefaultFileTypeMap();
	    String mimetype = filetypeMap.getContentType(path);

	    try {
			update.addFile(new File(path), mimetype);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    update.setParam("literal.id", param.getId());
		update.setParam("literal.pdf_library_id", param.getPdfLibraryId());
		update.setParam("literal.account_id", param.getAccountId());
		update.setParam("literal.date_time", param.getDateTime().toString());
		update.setParam("literal.file_name", param.getFileName());
		update.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);

		SolrServer solrServer = solrTemplate.getSolrServer();
		try {
			solrServer.request(update);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Page<PdfLibrarySolr> findByMetaSearch(String param) {
		// 串刺し検索
		Query query = new SimpleQuery(new Criteria().expression(param));
		return getSolrOperations().queryForPage(query, PdfLibrarySolr.class);
	}

	public PdfLibrarySolr findByDesignationField(String fieldName, String param) {
		// フィールド指定検索
		SimpleQuery query = new SimpleQuery(new Criteria(fieldName).expression(param));
		return getSolrOperations().queryForObject(query, PdfLibrarySolr.class);
	}

}
