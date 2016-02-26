package org.springframework.data.solr.example.repository;

import org.springframework.data.solr.example.model.PdfLibrarySolr;
import org.springframework.data.solr.repository.SolrCrudRepository;


public interface PdfLibrarySolrBaseRepository extends SolrCrudRepository<PdfLibrarySolr, String> {
}
