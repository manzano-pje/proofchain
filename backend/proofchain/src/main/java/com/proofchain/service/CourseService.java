package com.proofchain.service;

import com.proofchain.Dtos.request.CourseRequestDto;
import com.proofchain.Dtos.response.FullCourseResponseDto;
import com.proofchain.configuration.FormatarTexto;
import com.proofchain.exceptions.BusinessRuleException;
import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.exceptions.ValidationException;
import com.proofchain.identities.Course;
import com.proofchain.identities.Instituition;
import com.proofchain.repository.CourseRepository;
import com.proofchain.repository.InstituitionRepository;
import com.proofchain.security.SecurityUtils;
import com.proofchain.util.Validations;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.Instant.now;


@AllArgsConstructor
@Service
public class CourseService {

    private final ModelMapper mapper;
    private final InstituitionRepository instituitionRepository;
    private final CourseRepository courseRepository;
    private final Validations validations;

    public void createCourse(CourseRequestDto newCourse)  {
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);

        validations.validateCourseExist(newCourse.getName(), institution.getIdInstituition());

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

    public void updateCourse(String name, CourseRequestDto courseDto){
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);

        Optional<Course> courseOpt = validations.validateCourseNoExist(courseDto.getName(), institution.getIdInstituition());

        Course course = new Course();
        course.setIdCourse(courseOpt.get().getIdCourse());
        course.setName(FormatarTexto.formatarString(course.getName()));
        course.setDescription(FormatarTexto.formatarString(course.getDescription()));
        course.setHours(course.getHours());
        course.setUpdatedAt(now());
        course.setCreatedAt(courseOpt.get().getCreatedAt());
        courseRepository.save(course);
    }

    public List<FullCourseResponseDto> listAllCourses(){
        Long institutionId = SecurityUtils.getInstitutionId();
        Instituition institution = validations.validateInstituition(institutionId);
        
        List<Course> courseList = courseRepository.findAll();
        if(courseList.isEmpty()){
            throw new ResourceNotFoundException("Não existem cursos cadastrados.");
        }
        return courseList.stream()
                .map(FullCourseResponseDto::new)
                .collect(Collectors
                        .toList());
    }
}
