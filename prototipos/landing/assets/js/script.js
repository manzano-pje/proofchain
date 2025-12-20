
// Mobile menu toggle
document.getElementById('mobileToggle').addEventListener('click', function() {
    document.getElementById('navMenu').classList.toggle('show');
});

// FAQ Accordion
document.querySelectorAll('.faq-question').forEach(item => {
    item.addEventListener('click', () => {
    const answer = item.nextElementSibling;
    const icon = item.querySelector('i');
    answer.classList.toggle('open');
    icon.classList.toggle('fa-chevron-up');
    icon.classList.toggle('fa-chevron-down');
    });
});

// Validation form
document.getElementById('validationForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const code = document.getElementById('certCode').value.trim().toUpperCase();
    const resultDiv = document.getElementById('result');
    const statusDiv = document.getElementById('status');
    const detailsDiv = document.getElementById('details');

    // Mock data
    const validCert = {
    id: 'CERT-2025-AB12CD',
    course: 'Gestão de Projetos com Metodologias Ágeis',
    institution: 'Escola Técnica Nacional',
    issued: '15/12/2024',
    validUntil: '15/12/2026',
    participant: 'João Silva',
    hours: 60,
    status: 'VÁLIDO'
    };

    const expiredCert = {
    id: 'CERT-2025-EF34GH',
    course: 'Sustentabilidade em ONGs',
    institution: 'Instituto Verde Futuro',
    issued: '03/11/2024',
    validUntil: '03/11/2025',
    participant: 'Maria Oliveira',
    hours: 30,
    status: 'EXPIRADO'
    };

    let cert = null;

    if (code === validCert.id) {
    cert = validCert;
    } else if (code === expiredCert.id) {
    cert = expiredCert;
    }

    resultDiv.className = 'validation-result';
    if (cert) {
    const isValid = cert.status === 'VÁLIDO';
    resultDiv.classList.add(isValid ? 'valid' : 'invalid');
    statusDiv.innerHTML = `<i class="fas fa-${isValid ? 'check-circle' : 'times-circle'}"></i> ${cert.status}`;
    detailsDiv.innerHTML = `
        <p><strong>Curso:</strong> ${cert.course}</p>
        <p><strong>Instituição:</strong> ${cert.institution}</p>
        <p><strong>Participante:</strong> ${cert.participant}</p>
        <p><strong>Emissão:</strong> ${cert.issued}</p>
        <p><strong>Validade:</strong> ${cert.validUntil}</p>
        <p><strong>Carga horária:</strong> ${cert.hours}h</p>
    `;
    } else {
    resultDiv.classList.add('invalid');
    statusDiv.innerHTML = `<i class="fas fa-times-circle"></i> Certificado não encontrado`;
    detailsDiv.innerHTML = `<p>Verifique o código digitado ou entre em contato com a instituição emissora.</p>`;
    }
});
