package com.proofchain.service;

import com.proofchain.Dtos.request.CourseRequestDto;
import com.proofchain.configuration.FormatarTexto;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.Course;
import com.proofchain.identities.Instituition;
import com.proofchain.repository.CourseRepository;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;


@AllArgsConstructor
@Service
public class CourseService {

    private final ModelMapper mapper;
    private final InstituitionRepository instituitionRepository;
    private final CourseRepository courseRepository;

    public void createCourse(CourseRequestDto newCourse)  {

        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        Optional<Course> courseOptional = courseRepository.findByName(newCourse.getName());
        if(courseOptional.isPresent()){
            throw new BusinessRuleException("Curso já cadatrado.");
        }

        Course course = new Course();
        course.setName(FormatarTexto.formatarString(newCourse.getName()));
        course.setDescription(FormatarTexto.formatarString(newCourse.getDescription()));



    }
}
