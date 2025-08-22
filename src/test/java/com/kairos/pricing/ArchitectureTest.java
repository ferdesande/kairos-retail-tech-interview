package com.kairos.pricing;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.DisplayName;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInAnyPackage;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;

@AnalyzeClasses(packages = "com.kairos.pricing")
@DisplayName("Architecture Tests")
public class ArchitectureTest {
    private final static String SPRING_BOOT_SUBPACKAGES = "org.springframework..";
    private final static String PROJECT_ROOT = "com.kairos.pricing";
    private final static String DOMAIN_LAYER = PROJECT_ROOT + ".domain";
    private final static String APPLICATION_LAYER = PROJECT_ROOT + ".application";
    private final static String INFRASTRUCTURE_LAYER = PROJECT_ROOT + ".infrastructure";
    private final static String PACKAGES_ARCH_FILE = "/architecture.puml";
    private final static String INFRASTRUCTURE_ISOLATION_ARCH_FILE = "/infrastructure-isolation.puml";

    @ArchTest
    void plantUmlDiagramIsNotViolated(JavaClasses classes) {
        var plantUmlDiagram = ArchitectureTest.class.getResource(PACKAGES_ARCH_FILE);
        classes().should(
                adhereToPlantUmlDiagram(
                        Objects.requireNonNull(plantUmlDiagram),
                        consideringOnlyDependenciesInAnyPackage(PROJECT_ROOT + "..")
                )
        ).check(classes);
    }

    @ArchTest
    void noCycles(JavaClasses classes) {
        SlicesRuleDefinition.slices()
                .matching(PROJECT_ROOT + ".(*)..")
                .should()
                .beFreeOfCycles()
                .check(classes);
    }

    @ArchTest
    void noClassCanAccessWithStandardStreams(JavaClasses classes) {
        noClasses().should(ACCESS_STANDARD_STREAMS).check(classes);
    }

    @ArchTest
    void domainShouldNotDependOnSpringFramework(JavaClasses classes) {
        noClasses()
                .that().resideInAPackage(DOMAIN_LAYER + "..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(SPRING_BOOT_SUBPACKAGES)
                .check(classes);
    }

    @ArchTest
    void applicationShouldNotDependOnSpringFramework(JavaClasses classes) {
        noClasses()
                .that().resideInAPackage(APPLICATION_LAYER + "..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(SPRING_BOOT_SUBPACKAGES)
                .check(classes);
    }

    @ArchTest
    void infrastructureIsolationIsNotViolated(JavaClasses classes) {
        var isolationDiagram = ArchitectureTest.class.getResource(INFRASTRUCTURE_ISOLATION_ARCH_FILE);
        classes().should(
                adhereToPlantUmlDiagram(
                        Objects.requireNonNull(isolationDiagram),
                        consideringOnlyDependenciesInAnyPackage(INFRASTRUCTURE_LAYER + "..")
                )
        ).check(classes);
    }

    @ArchTest
    void entitiesShouldNotContainBusinessLogic(JavaClasses classes) {
        noClasses()
                .that().resideInAPackage(INFRASTRUCTURE_LAYER + ".persistence.entity..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(DOMAIN_LAYER + ".service..", APPLICATION_LAYER + ".usecase..")
                .check(classes);
    }

    @ArchTest
    void persistenceShouldNotDependOnWeb(JavaClasses classes) {
        noClasses()
                .that().resideInAPackage(INFRASTRUCTURE_LAYER + ".persistence..")
                .should().dependOnClassesThat()
                .resideInAPackage(INFRASTRUCTURE_LAYER + ".web..")
                .check(classes);
    }

    @ArchTest
    void webShouldNotDependOnPersistence(JavaClasses classes) {
        noClasses()
                .that().resideInAPackage(INFRASTRUCTURE_LAYER + ".web..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(INFRASTRUCTURE_LAYER + ".persistence.entity..",
                        INFRASTRUCTURE_LAYER + ".persistence.repository..")
                .check(classes);
    }

    @ArchTest
    void onlyRepositoriesShouldHaveRepositoryAnnotation(JavaClasses classes) {
        classes()
                .that().areAnnotatedWith(Repository.class)
                .should().resideInAPackage(INFRASTRUCTURE_LAYER + ".persistence.repository..")
                .check(classes);
    }

    @ArchTest
    void onlyInfrastructureRepositoryShouldDependOnSpringDataRepositories(JavaClasses classes) {
        noClasses()
                .that().resideOutsideOfPackage("..infrastructure.persistence.repository..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework.data.repository..",
                        "org.springframework.data.jpa.repository..")
                .check(classes);
    }

    @ArchTest
    void onlyControllersShouldHaveControllerAnnotation(JavaClasses classes) {
        classes()
                .that().areAnnotatedWith(RestController.class)
                .or().areAnnotatedWith(Controller.class)
                .should().resideInAPackage(INFRASTRUCTURE_LAYER + ".web..")
                .check(classes);
    }
}
