package com.proofchain.shared.domain.model;

import com.proofchain.institution.domain.model.Institution;
import com.proofchain.institution.infrastructure.repository.InstitutionRepository;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.domain.model.UserRole;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static com.proofchain.user.domain.model.UserRole.SUPER_ADMIN;

@Component
public class SystemBootstrap implements CommandLineRunner {

    private static final String CNPJ = "52866268000104";
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SystemBootstrap(InstitutionRepository institutionRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.institutionRepository = institutionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        Institution root = institutionRepository.findByCnpj(CNPJ)
                .orElseGet(() -> {
                    Institution i = new Institution();
                    i.setName("Proofchain");
                    i.setCnpj(CNPJ);
                    i.setEmail("admin@proofchain.com.br");
                    i.setActive(true);
                    i.setCreatedAt(Instant.now());
                    return institutionRepository.save(i);
                });

        boolean existsAdmin = userRepository.existsByRole(SUPER_ADMIN);

        if (!existsAdmin) {
            User admin = new User();
            admin.setName("proofchain");
            admin.setEmail("admin@proofchain.com.br");
            admin.setPassword(passwordEncoder.encode("adm.proof123"));
            admin.setRole(SUPER_ADMIN);
            admin.setCreateAt(Instant.now());
            admin.setActive(true);
            admin.setInstitution(root);
            userRepository.save(admin);
        }
        System.out.println("Super Admin criado");
    }
}
