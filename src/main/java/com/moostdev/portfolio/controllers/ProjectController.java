package com.moostdev.portfolio.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moostdev.portfolio.domain.Project;
import com.moostdev.portfolio.services.FileStorageService;
import com.moostdev.portfolio.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProjectController(ProjectService projectService,
                             FileStorageService fileStorageService,
                             ObjectMapper objectMapper) {
        this.projectService = projectService;
        this.fileStorageService = fileStorageService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable String id) {
        return projectService.getProjectById(id);
    }

    // Endpoint para JSON puro
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Project createProjectWithJson(@RequestBody Project project) throws IOException {
        return projectService.createProject(project, null, null);
    }

    // Endpoint para multipart/form-data
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Project createProjectWithFiles(
            @RequestPart("project") String projectJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "video", required = false) MultipartFile video) throws IOException {

        Project project = objectMapper.readValue(projectJson, Project.class);
        return projectService.createProject(project, images, video);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Project updateProject(
            @PathVariable String id,
            @RequestPart("project") String projectJson,
            @RequestPart(value = "newImages", required = false) MultipartFile[] newImages,
            @RequestPart(value = "newVideo", required = false) MultipartFile newVideo) throws IOException {

        Project projectDetails = objectMapper.readValue(projectJson, Project.class);
        return projectService.updateProject(id, projectDetails, newImages, newVideo);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable String id) throws IOException {
        projectService.deleteProject(id);
    }

    @DeleteMapping("/{projectId}/images/{imageName}")
    public void deleteImageFromProject(
            @PathVariable String projectId,
            @PathVariable String imageName) throws IOException {
        projectService.deleteImageFromProject(projectId, imageName);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Resource file = fileStorageService.loadFileAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}