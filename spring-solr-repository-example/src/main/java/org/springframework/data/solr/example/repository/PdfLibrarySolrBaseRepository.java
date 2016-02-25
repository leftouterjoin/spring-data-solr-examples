package org.springframework.data.solr.example.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.solr.example.model.PdfLibrarySolr;


public interface PdfLibrarySolrBaseRepository extends CrudRepository<PdfLibrarySolr, String> {
}
