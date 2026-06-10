@echo off
echo ========================================
echo Criando estrutura do ProofChain Frontend
echo ========================================

REM =========================
REM ASSETS
REM =========================

mkdir src\assets

REM =========================
REM STYLES
REM =========================

mkdir src\styles
mkdir src\styles\tokens
mkdir src\styles\base
mkdir src\styles\utilities
mkdir src\styles\themes

type nul > src\styles\tokens\colors.css
type nul > src\styles\tokens\typography.css
type nul > src\styles\tokens\index.css

type nul > src\styles\base\reset.css
type nul > src\styles\base\globals.css
type nul > src\styles\base\animations.css

type nul > src\styles\utilities\flex.css
type nul > src\styles\utilities\grid.css
type nul > src\styles\utilities\spacing.css
type nul > src\styles\utilities\sizing.css
type nul > src\styles\utilities\text.css

type nul > src\styles\themes\light.css
type nul > src\styles\themes\dark.css

type nul > src\styles\main.css

REM =========================
REM DESIGN SYSTEM
REM =========================

mkdir src\design-system
mkdir src\design-system\components
mkdir src\design-system\layouts

type nul > src\design-system\components\BaseButton.vue
type nul > src\design-system\components\BaseInput.vue
type nul > src\design-system\components\BaseCard.vue
type nul > src\design-system\components\BaseBadge.vue
type nul > src\design-system\components\BaseModal.vue
type nul > src\design-system\components\BaseTable.vue
type nul > src\design-system\components\BaseSelect.vue
type nul > src\design-system\components\BaseTextarea.vue
type nul > src\design-system\components\BaseTooltip.vue
type nul > src\design-system\components\BaseDropdown.vue
type nul > src\design-system\components\BaseEmptyState.vue

type nul > src\design-system\layouts\AuthLayout.vue
type nul > src\design-system\layouts\DashboardLayout.vue
type nul > src\design-system\layouts\LandingLayout.vue
type nul > src\design-system\layouts\PublicLayout.vue

REM =========================
REM CORE
REM =========================

mkdir src\core
mkdir src\core\api
mkdir src\core\config
mkdir src\core\constants
mkdir src\core\router
mkdir src\core\stores
mkdir src\core\composables
mkdir src\core\utils

type nul > src\core\api\axios.ts
type nul > src\core\api\interceptors.ts

type nul > src\core\config\env.ts
type nul > src\core\config\app.ts

type nul > src\core\constants\routes.ts
type nul > src\core\constants\permissions.ts
type nul > src\core\constants\plans.ts

type nul > src\core\router\index.ts
type nul > src\core\router\guards.ts
type nul > src\core\router\routes.ts

type nul > src\core\stores\auth.store.ts
type nul > src\core\stores\user.store.ts
type nul > src\core\stores\company.store.ts

type nul > src\core\composables\useToast.ts
type nul > src\core\composables\useModal.ts
type nul > src\core\composables\usePagination.ts
type nul > src\core\composables\useDebounce.ts

type nul > src\core\utils\formatDate.ts
type nul > src\core\utils\formatCpf.ts
type nul > src\core\utils\formatCnpj.ts
type nul > src\core\utils\downloadFile.ts

REM =========================
REM SHARED
REM =========================

mkdir src\shared
mkdir src\shared\components
mkdir src\shared\enums
mkdir src\shared\types
mkdir src\shared\validators
mkdir src\shared\services

type nul > src\shared\components\DataTable.vue
type nul > src\shared\components\SearchInput.vue
type nul > src\shared\components\ConfirmDialog.vue
type nul > src\shared\components\PageHeader.vue
type nul > src\shared\components\EmptyState.vue
type nul > src\shared\components\LoadingState.vue

REM =========================
REM MODULES
REM =========================

mkdir src\modules

mkdir src\modules\auth
mkdir src\modules\dashboard
mkdir src\modules\certificates
mkdir src\modules\validations
mkdir src\modules\issuers
mkdir src\modules\users
mkdir src\modules\company
mkdir src\modules\plans
mkdir src\modules\billing
mkdir src\modules\settings
mkdir src\modules\audit

REM =========================
REM AUTH
REM =========================

mkdir src\modules\auth\pages
mkdir src\modules\auth\components
mkdir src\modules\auth\services
mkdir src\modules\auth\types

type nul > src\modules\auth\pages\LoginPage.vue
type nul > src\modules\auth\pages\ForgotPasswordPage.vue
type nul > src\modules\auth\pages\ResetPasswordPage.vue

type nul > src\modules\auth\components\LoginForm.vue
type nul > src\modules\auth\components\CertificateStatus.vue

type nul > src\modules\auth\services\auth.service.ts
type nul > src\modules\auth\types\auth.types.ts
type nul > src\modules\auth\routes.ts

REM =========================
REM CERTIFICATES
REM =========================

mkdir src\modules\certificates\pages
mkdir src\modules\certificates\components
mkdir src\modules\certificates\services
mkdir src\modules\certificates\types

type nul > src\modules\certificates\pages\CertificatesList.vue
type nul > src\modules\certificates\pages\CertificateDetails.vue
type nul > src\modules\certificates\pages\CreateCertificate.vue
type nul > src\modules\certificates\pages\EditCertificate.vue

type nul > src\modules\certificates\components\CertificateCard.vue
type nul > src\modules\certificates\components\CertificatePreview.vue
type nul > src\modules\certificates\components\CertificateStatusBadge.vue
type nul > src\modules\certificates\components\CertificateFilters.vue

type nul > src\modules\certificates\services\certificates.service.ts
type nul > src\modules\certificates\types\certificate.types.ts
type nul > src\modules\certificates\routes.ts

REM =========================
REM VALIDATIONS
REM =========================

mkdir src\modules\validations\pages
mkdir src\modules\validations\components
mkdir src\modules\validations\services

type nul > src\modules\validations\pages\ValidationPage.vue
type nul > src\modules\validations\pages\PublicValidationPage.vue
type nul > src\modules\validations\pages\ValidationResult.vue

type nul > src\modules\validations\components\ValidationForm.vue
type nul > src\modules\validations\components\ValidationSuccess.vue
type nul > src\modules\validations\components\ValidationExpired.vue
type nul > src\modules\validations\components\ValidationCancelled.vue

type nul > src\modules\validations\services\validation.service.ts

REM =========================
REM PAGES
REM =========================

mkdir src\pages

type nul > src\pages\HomePage.vue
type nul > src\pages\PricingPage.vue
type nul > src\pages\AboutPage.vue
type nul > src\pages\ContactPage.vue
type nul > src\pages\NotFoundPage.vue

echo.
echo ========================================
echo Estrutura criada com sucesso!
echo ========================================
pause
```

Uma melhoria que considero muito útil para você seria gerar não apenas as pastas vazias, mas também os arquivos `.vue` já com um template padrão (`<script setup lang="ts">`, `<template>` e `<style scoped>`), além dos arquivos `.ts` com exportações iniciais. Isso economiza bastante tempo no início do desenvolvimento.
