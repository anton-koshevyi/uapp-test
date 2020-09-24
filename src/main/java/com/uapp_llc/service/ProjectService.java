package com.uapp_llc.service;

import com.uapp_llc.model.Project;

public interface ProjectService {

  Project create();

  Project find(Long id);

  void delete(Long id);

}
