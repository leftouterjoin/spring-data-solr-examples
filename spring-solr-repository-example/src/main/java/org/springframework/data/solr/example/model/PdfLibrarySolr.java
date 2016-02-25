package org.springframework.data.solr.example.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.solr.client.solrj.beans.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfLibrarySolr {

	@Field("id")
	private String id;

	@Field("pdf_library_id")
	private String pdfLibraryId;

	@Field("account_id")
	private String accountId;

	@Field("date_time")
	private Date dateTime;

	@Field("file_name")
	private String fileName;

	@Field("content")
	private String content;
}