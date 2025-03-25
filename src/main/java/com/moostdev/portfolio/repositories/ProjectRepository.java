package com.moostdev.portfolio.repositories;

import com.moostdev.portfolio.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
