package com.proofchain.service;

import com.proofchain.Dtos.request.CourseDto;
import com.proofchain.configuration.FormatarTexto;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.exceptions.ValidationException;
import com.proofchain.identities.Course;
import com.proofchain.identities.Instituition;
import com.proofchain.repository.CourseRepository;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.time.Instant.now;


@AllArgsConstructor
@Service
public class CourseService {

    private final ModelMapper mapper;
    private final InstituitionRepository instituitionRepository;
    private final CourseRepository courseRepository;

    public void createCourse(CourseDto newCourse)  {

        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        Optional<Course> courseOptional = courseRepository.findByName(newCourse.getName());
        if(courseOptional.isPresent()){
            throw new BusinessRuleException("Curso já cadatrado.");
        }
        if(newCourse.getHours()<=0){
            throw new ValidationException("Quantidade de horas precisa ser maior que 0");
        }

        Course course = new Course();
        course.setName(FormatarTexto.formatarString(newCourse.getName()));
        course.setDescription(FormatarTexto.formatarString(newCourse.getDescription()));
        course.setHours(newCourse.getHours());
        course.setInstituition(institution);
        course.setCreatedAt(now());
        courseRepository.save(course);
    }

    public void updateCourse(CourseDto courseDto){
        // 🔑 Instituição vem do TOKEN, não do request
        Long institutionId = SecurityUtils.getInstitutionId();

        Instituition institution = instituitionRepository.findByidInstituition(institutionId)
                .orElseThrow(() ->new ResourceNotFoundException("Instituição não encontrada"));

        Optional<Course> courseOptional = courseRepository.findByName(courseDto.getName());
        if(courseOptional.isEmpty()){
            throw new BusinessRuleException("Curso não cadatrado.");
        }

        Course course = new Course();
        course.setIdCourse(courseOptional.get().getIdCourse());
        course.setName(FormatarTexto.formatarString(course.getName()));
        course.setDescription(FormatarTexto.formatarString(course.getDescription()));
        course.setHours(course.getHours());
        course.setUpdatedAt(now());
        course.setCreatedAt(courseOptional.get().getCreatedAt());
        courseRepository.save(course);
    }
}
