package com.moostdev.portfolio.services;

import com.moostdev.portfolio.exception.ResourceNotFoundException;
import com.moostdev.portfolio.domain.Project;
import com.moostdev.portfolio.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, FileStorageService fileStorageService) {
        this.projectRepository = projectRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public Project createProject(Project project, MultipartFile[] images, MultipartFile video) throws IOException {
        // Handle images
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String imageName = fileStorageService.storeFile(image);
                    project.getImagesUrl().add(imageName);
                }
            }
        }

        // Handle video
        if (video != null && !video.isEmpty()) {
            String videoName = fileStorageService.storeFile(video);
            project.setVideoUrl(videoName);
        }

        return projectRepository.save(project);
    }

    public Project updateProject(String id, Project projectDetails, MultipartFile[] newImages, MultipartFile newVideo) throws IOException {
        Project project = getProjectById(id);

        project.setTitle(projectDetails.getTitle());
        project.setDescription(projectDetails.getDescription());
        project.setLink(projectDetails.getLink());
        project.setGithub(projectDetails.getGithub());
        project.setTechnologies(projectDetails.getTechnologies());
        project.setImagesUrl(projectDetails.getImagesUrl());
        project.setVideoUrl(projectDetails.getVideoUrl());
        // Handle new images
        if (newImages != null && newImages.length > 0) {
            for (MultipartFile image : newImages) {
                if (!image.isEmpty()) {
                    String imageName = fileStorageService.storeFile(image);
                    project.getImagesUrl().add(imageName);
                }
            }
        }

        // Handle new video
        if (newVideo != null && !newVideo.isEmpty()) {
            // Delete old video if exists
            if (project.getVideoUrl() != null) {
                fileStorageService.deleteFile(project.getVideoUrl());
            }
            String videoName = fileStorageService.storeFile(newVideo);
            project.setVideoUrl(videoName);
        }

        return projectRepository.save(project);
    }

    public void deleteProject(String id) throws IOException {
        Project project = getProjectById(id);

        // Delete all images
        for (String imageName : project.getImagesUrl()) {
            fileStorageService.deleteFile(imageName);
        }

        // Delete video if exists
        if (project.getVideoUrl() != null) {
            fileStorageService.deleteFile(project.getVideoUrl());
        }

        projectRepository.delete(project);
    }

    public void deleteImageFromProject(String projectId, String imageName) throws IOException {
        Project project = getProjectById(projectId);
        if (project.getImagesUrl().remove(imageName)) {
            fileStorageService.deleteFile(imageName);
            projectRepository.save(project);
        }
    }


}