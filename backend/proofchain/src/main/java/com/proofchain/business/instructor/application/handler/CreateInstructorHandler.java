package com.proofchain.business.instructor.application.handler;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.business.instructor.application.command.CreateInstructorCommand;
import com.proofchain.business.instructor.domain.model.Instructor;
import com.proofchain.business.instructor.insfrastructure.repository.InstructorRepository;
import com.proofchain.shared.exception.AlreadyExistsException;
import com.proofchain.shared.exception.NotFoundException;
import com.proofchain.shared.exception.messages.InstitutionMessages;
import com.proofchain.shared.exception.messages.InstructorMessages;
import com.proofchain.shared.exception.messages.UserMessage;
import com.proofchain.shared.security.SecurityUtils;
import com.proofchain.shared.util.TenantValidation;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateInstructorHandler {

    private final TenantValidation tenantValidation;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final CreateInstructorCommand command;
    private final InstitutionRepository institutionRepository;

    public void createInstructor(CreateInstructorCommand command){

        /*
         * =========================================================
         * CONTEXTO DE INSTITUIÇÃO (TENANT)
         * =========================================================
         */

        Long institutionId = SecurityUtils.getInstitutionId();
        tenantValidation.validateInstitution(institutionId);

        /*
         * =========================================================
         * CARREGAR DADOS DE USUÁRIO
         * =========================================================
         */

        Optional<User> userOptional = userRepository
                .findByIdAndInstitution_Id(command.getId_User(), institutionId);
        if(userOptional.isEmpty()){
            throw new NotFoundException(UserMessage.USER_NOT_FOUND);
        }

        /*
         * =========================================================
         * CARREGAR DADOS DE INSTITUIÇÃO
         * =========================================================
         */

        Optional<Institution> institutionOptional = institutionRepository
                .findByIdAndDeletedAtIsNull(institutionId);
        if(institutionOptional.isEmpty()){
            throw new NotFoundException(InstitutionMessages.INSTITUTION_NOT_FOUND);
        }

        /*
         * =========================================================
         * VERIFICAR SE INSTRUTOR JÁ EXISTE
         * =========================================================
         */

          boolean existInstructor = instructorRepository
                  .existsByUserIdAndInstitutionIdAndInstitutionDeletedAtIsNull(
                          userOptional.get().getId(),
                          institutionId);

        var teste = instructorRepository
                .findByUserIdAndInstitutionIdAndInstitutionDeletedAtIsNull(
                        1L,
                        1L);
        System.out.println("\n========================================");
        System.out.println("teste: " + teste);
        System.out.println("\n========================================");

          if(existInstructor){
              throw new AlreadyExistsException(InstructorMessages.INSTRUCTOR_ALREAY_EXISTS);
          }

        /*
         * =========================================================
         * CRIAÇÃO E PERSISTÊNCIA DE INSCTRUTOR
         * =========================================================
         */
           Instructor instructor = new Instructor();
          instructor.setUser(userOptional.get());
          instructor.setInstitution(institutionOptional.get());
          instructor.setSpecialty(command.getSpecialty());
          instructor.setHiringDate(command.getHiringDate());
          instructor.setActive(true);
          instructor.setCreatedAt(Instant.now());
          instructor.setDeletedAt(null);

          instructorRepository.save(instructor);

    }
}
